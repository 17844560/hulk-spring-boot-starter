package me.jinuo.imf.websocket.codec

import me.jinuo.imf.websocket.codec.message.Message
import org.springframework.web.socket.BinaryMessage
import org.springframework.web.socket.WebSocketMessage
import java.nio.ByteBuffer

/**
 * @author frank
 * @date 2019-10-25 18:30
 * @desc
 **/
interface Codec {


    /**
     * 消息解码
     */
    fun decode(message: WebSocketMessage<ByteBuffer>): Message?

    /**
     * 消息编码
     */
    fun encode(message: Message): BinaryMessage
}