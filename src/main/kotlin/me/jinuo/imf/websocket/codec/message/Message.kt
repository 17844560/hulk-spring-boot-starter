package me.jinuo.imf.websocket.codec.message

import me.jinuo.imf.websocket.definition.Command

class Message {

    companion object {
        fun valueOf(header: Header, body: ByteArray): Message {
            val message = Message()
            message.header = header
            message.body = body
            return message
        }
    }

    lateinit var header: Header
    lateinit var body: ByteArray
    var bodyJson: String = ""


    fun getCommand(): Command {
        return header.getCommand()
    }

}