package me.jinuo.imf.websocket.session

import me.jinuo.imf.websocket.codec.message.Header
import me.jinuo.imf.websocket.codec.message.Message
import me.jinuo.imf.websocket.codec.message.MessageState
import me.jinuo.imf.websocket.definition.Command
import me.jinuo.imf.websocket.handler.Dispatcher
import org.springframework.web.socket.BinaryMessage
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketSession
import java.net.InetSocketAddress
import java.net.URI
import java.util.concurrent.ConcurrentHashMap

/**
 * @author frank
 * @date 2019-10-25 21:01
 * @desc
 **/
class DefaultSession : Session {

    private var id: Long = -1L
    /** 用户身份标识*/
    private var identity: Any? = null
    /** 存放参数*/
    private var attr = ConcurrentHashMap<Any, Any>()
    /** 最后一次收包时间*/
    private var lastTime: Long = -1
    /** 远端地址*/
    private lateinit var remoteAddress: InetSocketAddress
    /** webSocket 会话*/
    private lateinit var session: WebSocketSession
    /** 指令分发器*/
    private lateinit var dispatcher: Dispatcher
    /** sessionManager 管理器*/
    private lateinit var sessionManager: SessionManager<*, *>

    companion object {
        fun valueOf(id: Long, session: WebSocketSession, dispatcher: Dispatcher, sessionManager: SessionManager<*, *>): Session {
            val instance = DefaultSession()
            instance.session = session
            instance.id = id
            instance.remoteAddress = session.remoteAddress!!
            instance.dispatcher = dispatcher
            instance.sessionManager = sessionManager
            return instance
        }
    }

    override fun getId(): Long {
        return id
    }

    override fun getIdentity(): Any? {
        return identity
    }

    override fun setIdentity(identity: Any) {
        this.identity = identity
    }

    override fun <T> getAttr(key: Any): T? {
        attr[key] ?: return null
        @Suppress("UNCHECKED_CAST")
        return attr[key] as T
    }

    override fun setAttr(key: Any, value: Any) {
        require(!attr.containsKey(key)) { "KEY[${key}]已存在" }
        attr.put(key, value)
    }

    override fun send(message: Message) {
        val codec = dispatcher.getCodec()
        val send = codec.encode(message)
        send(send)
    }

    override fun send(command: Command, message: Any) {
        val sn = sessionManager.nextSn()
        val header = Header.valueOf(sn, MessageState.NORMAL.state, command.module, command.command)
        val bodyArray = dispatcher.encode(message)
        val send = Message.valueOf(header, bodyArray)
        send(send)
    }

    override fun send(module: Short, cmd: Short, message: Any) {
        val sn = sessionManager.nextSn()
        val header = Header.valueOf(sn, MessageState.NORMAL.state, module, cmd)
        val bodyArray = dispatcher.encode(message)
        val send = Message.valueOf(header, bodyArray)
        send(send)
    }

    override fun send(binaryMessage: BinaryMessage) {
        if (session.isOpen) {
            try {
                session.sendMessage(binaryMessage)
            } catch (e: IllegalStateException) {
            }
        }
    }

    override fun lastTime(): Long {
        return lastTime
    }

    override fun setLastTime(lastTime: Long) {
        this.lastTime = lastTime
    }

    override fun isActive(): Boolean {
        return session.isOpen
    }

    override fun getRemoteAddress(): InetSocketAddress {
        return remoteAddress
    }

    override fun close() {
        if (session.isOpen) {
            session.close(CloseStatus.NORMAL)
        }
    }

    override fun getWebSocketSession(): WebSocketSession {
        return session
    }

    override fun getUri(): URI? {
        return session.uri
    }

}