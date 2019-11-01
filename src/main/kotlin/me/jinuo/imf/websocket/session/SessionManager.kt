package me.jinuo.imf.websocket.session

import me.jinuo.imf.websocket.codec.message.Message
import me.jinuo.imf.websocket.definition.Command

/**
 * @author frank
 * @date 2019-10-26 15:10
 * @desc
 **/
interface SessionManager<S, T : Session> {

    /**
     * 获取全部SESSION
     * @return
     */
    fun allSessions(): Collection<T>

    /**
     * 获取SESSION
     * @param sessionId
     * @return
     */
    fun findSession(sessionId: Long): T?

    /**
     * 通过玩家标识获得已鉴权的session
     */
    fun getSession(identity: Any): T?

    /**
     * 所有已鉴权的会话
     */
    fun getSessions(): Collection<T>

    /**
     * 创建 SESSION
     * @param websocket
     * @return
     */
    fun createSession(websocket: S): T

    /**
     * 销毁SESSION
     * @param session
     * @return
     */
    fun destroySession(session: T)

    /**
     *
     */
    fun addSession(session: T)

    /**
     * 获取下一个发包序号
     */
    fun nextSn(): Int

    /**
     * 会话鉴权，绑定用户身份(uid)
     */
    fun bind(session: T, identity: Any)

    //推送消息---start
    fun push(message: Message, vararg identity: Any)

    fun push(message: Message, vararg sessions: T)

    fun pushAllIdentified(message: Message)

    fun push(command: Command, message: Any, vararg identity: Any)

    fun push(command: Command, message: Any, vararg sessions: T)

    fun pushAllIdentified(command: Command, message: Any)
    //推送消息---end-----
}