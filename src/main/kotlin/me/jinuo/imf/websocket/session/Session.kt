package me.jinuo.imf.websocket.session

import com.sun.jndi.toolkit.url.Uri
import me.jinuo.imf.websocket.codec.message.Message
import me.jinuo.imf.websocket.definition.Command
import org.springframework.web.socket.BinaryMessage
import org.springframework.web.socket.WebSocketSession
import java.net.InetSocketAddress
import java.net.URI

/**
 * @author frank
 * @date 2019-10-25 20:58
 * @desc
 **/
interface Session {

    companion object {
        const val SESSION_KEY = "SESSION_KEY"
        const val IDENTITY = "identity"
    }

    /**
     * Session Id
     */
    fun getId(): Long

    /**
     * 获取身份标识
     */
    fun getIdentity(): Any?

    fun setIdentity(identity: Any)

    /**
     * 获取session参数
     */
    fun <T> getAttr(key: Any): T?

    /**
     * 设置参数
     */
    fun setAttr(key: Any, value: Any)

    /**
     * 发送消息
     */
    fun send(binaryMessage: BinaryMessage)

    /**
     * 发送消息
     */
    fun send(message: Message)

    /**
     * 发送消息
     */
    fun send(command: Command, message: Any)

    /**
     * 发送消息
     */
    fun send(module: Short, cmd: Short, message: Any)

    /**
     * 最后消息时间
     */
    fun lastTime(): Long

    fun setLastTime(lastTime: Long)

    /**
     * 连接是否是有效
     */
    fun isActive(): Boolean

    /**
     * 连接的远端地址
     */
    fun getRemoteAddress(): InetSocketAddress

    /**
     * 关闭当前通信会话
     */
    fun close()

    /**
     * 获取原生session
     */
    fun getWebSocketSession(): WebSocketSession

    fun getUri(): URI?

}