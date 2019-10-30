package me.jinuo.imf.websocket.handler

import me.jinuo.imf.websocket.codec.Codec
import me.jinuo.imf.websocket.codec.message.Message
import me.jinuo.imf.websocket.session.Session
import org.springframework.web.socket.BinaryMessage

/**
 * @author frank
 * @date 2019-10-25 20:53
 * @desc
 **/
interface Dispatcher {
    /**
     * 收到客户端的消息
     */
    fun receiver(session: Session, binaryMessage: BinaryMessage)

    /**
     * 向客户端发送消息
     */
    fun send(session: Session, message: Message)

    /**
     * 消息编码
     */
    fun encode(any: Any): ByteArray

    /**
     * 消息解码
     */
    fun decode(message: BinaryMessage): Message?

    fun getCodec(): Codec
}