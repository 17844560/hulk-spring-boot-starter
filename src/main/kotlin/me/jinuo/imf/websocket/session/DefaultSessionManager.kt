package me.jinuo.imf.websocket.session

import me.jinuo.imf.websocket.event.KickEvent
import me.jinuo.imf.websocket.event.TimeoutEvent
import me.jinuo.imf.websocket.handler.DefaultDispatcher
import me.jinuo.imf.websocket.properties.GlobalConfig
import me.jinuo.imf.websocket.thread.DelayedElement
import me.jinuo.imf.websocket.utils.ByteUtils
import me.jinuo.imf.websocket.utils.RandomUtils
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationContext
import org.springframework.web.socket.WebSocketSession
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.DelayQueue
import java.util.concurrent.atomic.AtomicLong
import javax.annotation.PostConstruct
import javax.annotation.Resource

/**
 * @author frank
 * @date 2019-10-26 15:12
 * @desc
 **/
@EnableConfigurationProperties(GlobalConfig::class)
class DefaultSessionManager : SessionManager<WebSocketSession, Session> {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    /** SessionID 生成序列*/
    private val sequence = AtomicLong(0x00FFFF00L)
    /** 消息序号生成器*/
    private val snGenerator = SnGenerator()
    @Resource
    private lateinit var dispatcher: DefaultDispatcher
    @Resource
    private lateinit var applicationContext: ApplicationContext
    @Resource
    private lateinit var globalConfig: GlobalConfig
    /** SESSION 列表(包含匿名会话和已经鉴别身份的)  */
    private val sessions = ConcurrentHashMap<Long, Session>(INIT_SIZE)

    /** 已经鉴别用户身份的会话，Key:用户身份标识，Value:[Session]  */
    private val identities = ConcurrentHashMap<Any, Session>()

    /** 匿名会话，Key:[Session.getId]，Value:[Session]  */
    private val anonymous = ConcurrentHashMap<Long, Session>()
    /** 超时删除队列*/
    private var removeQueue = DelayQueue<DelayedElement<Long>>()

    @PostConstruct
    fun initializer() {
        val thread = Thread({
            while (true) {
                val element = removeQueue.take()
                val sid = element.content
                val session = getSession(sid)
                session ?: continue
                val now = element.end.getTime()
                if (now - globalConfig.timeout >= session.lastTime()) {
                    logger.debug("SESSION[{}]->[{}]超时关闭！", session.getId(), session.getRemoteAddress())
                    //超时
                    applicationContext.publishEvent(TimeoutEvent(this, session))
                    destroySession(session)
                } else {
                    //下一次检测
                    addDelayQueue(sid, Date(System.currentTimeMillis() + session.lastTime() + globalConfig.timeout))
                }
            }
        }, "SESSION守护线程")
        thread.start()
    }

    /**
     * 添加到延时检查队列
     */
    private fun addDelayQueue(sid: Long, end: Date) {
        if (!globalConfig.heartbeat) return
        removeQueue.add(DelayedElement(sid, end))
    }

    companion object {
        private val INIT_SIZE = 4096
    }

    override fun getSession(): Collection<Session> {
        return sessions.values
    }

    override fun createSession(websocket: WebSocketSession): Session {
        var id = nextId()
        if (sessions.containsKey(id)) {
            logger.warn("SESSION ID [{}] 已被使用 , 重新生成...", id)
            id = nextId()
        }
        var session = DefaultSession.valueOf(id, websocket, dispatcher, this)
        var absent: Session? = sessions.putIfAbsent(id, session)
        while (absent != null && absent !== session) {
            logger.warn("SESSION ID [{}] 已被使用 , 重新生成...", id)
            id = nextId()
            session = DefaultSession.valueOf(id, websocket, dispatcher, this)
            absent = sessions.putIfAbsent(id, session)
        }
        anonymous.put(session.getId(), session)
        //加入超时检查队列
        addDelayQueue(session.getId(), Date(System.currentTimeMillis() + globalConfig.timeout))
        return session
    }

    override fun getSession(id: Long): Session? {
        return sessions[id]
    }

    override fun destroySession(session: Session) {
        val address = session.getRemoteAddress()
        val sid = session.getId()
        // 销毁SESSION
        session.close()
        // 移除SESSION
        sessions.remove(sid, session)
        anonymous.remove(sid, session)
        if (session.getIdentity() != null) {
            identities.remove(session.getIdentity()!!)
        }
        logger.debug("SESSION[{}]销毁, 连接[{}]", sid, address)
    }

    /**
     * 获取下一个序列号
     * @return
     */
    private fun nextId(): Long {
        val prefix = System.currentTimeMillis() / 1000 % 0xFFFFFFFFL
        val random = RandomUtils.nextInt(0x0F).toLong()
        val sn = sequence.getAndIncrement() % 0x00FFFFFFL
        var result = (prefix shl 32) or (sn shl 8) or (random shl 4)
        val b1 = ByteUtils.longToByte(result)
        var suffix: Long = 0
        for (b in b1) {
            suffix += (b.toInt() and 0xFF).toLong()
        }
        suffix = suffix % 0x0FL
        result = result or suffix
        return result
    }

    override fun addSession(session: Session) {
        sessions[session.getId()] = session
    }

    override fun nextSn(): Int {
        return snGenerator.next()
    }

    override fun bind(session: Session, identity: Any) {
        val prev = identities.remove(identity)
        if (prev != null && prev != session) {
            applicationContext.publishEvent(KickEvent(this, prev))
            //销毁
            destroySession(prev)
        }
        session.setIdentity(identity)
        identities.put(identity, session)
    }
}