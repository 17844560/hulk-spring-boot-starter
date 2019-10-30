package me.jinuo.imf.websocket.codec

import me.jinuo.imf.websocket.codec.message.Header
import me.jinuo.imf.websocket.codec.message.Message
import org.springframework.web.socket.BinaryMessage
import org.springframework.web.socket.WebSocketMessage
import java.nio.ByteBuffer

/**
 * @author frank
 * @date 2019-10-25 20:15
 * @desc
 **/
class BinaryCodec : Codec {

    override fun decode(message: WebSocketMessage<ByteBuffer>): Message? {
        val buffer = message.payload
        if (buffer.remaining() < Header.HEAD_LENGTH) {
            return null
        }
        val header: Header = Header.read(buffer)
        val body = ByteArray(buffer.remaining())
        buffer.get(body)
        return Message.valueOf(header, body)
    }

    override fun encode(message: Message): BinaryMessage {
        val length = Header.HEAD_LENGTH + message.body.size
        val byteBuffer = ByteBuffer.allocate(length)
        //sn
        byteBuffer.putInt(message.header.sn)
        //state
        byteBuffer.putShort(message.header.state)
        //module
        byteBuffer.putShort(message.header.module)
        //cmd
        byteBuffer.putShort(message.header.cmd)
        if (message.body.size != 0) {
            byteBuffer.put(message.body)
        }
        val byteArray = byteBuffer.array()
        return BinaryMessage(byteArray)
    }
}