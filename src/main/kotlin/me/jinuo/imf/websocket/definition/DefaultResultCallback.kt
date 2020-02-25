package me.jinuo.imf.websocket.definition

import me.jinuo.imf.websocket.codec.message.Message
import me.jinuo.imf.websocket.codec.message.MessageState
import me.jinuo.imf.websocket.handler.Dispatcher
import me.jinuo.imf.websocket.session.Session

/**
 * @author frank
 * @date 2019-10-28 10:46
 * @desc 异步反回结果
 **/
class  DefaultResultCallback<T> : ResultCallback<T> {

    lateinit var dispatcher: Dispatcher
    lateinit var session: Session
    lateinit var message: Message

    companion object {
        fun valueOf(session: Session, message: Message, dispatcher: Dispatcher): ResultCallback<Any?> {
            val callback = DefaultResultCallback<Any?>()
            callback.dispatcher = dispatcher
            callback.session = session
            callback.message = message
            return callback
        }
    }

    override fun call(body: T?) {
        val header = message.header
        //这是一条回复消息
        header.addState(MessageState.RESPONSE.state)
        body ?: return dispatcher.send(session, Message.valueOf(header, ByteArray(0)))
        val byteArray = dispatcher.encode(body)
        val message = Message.valueOf(header, byteArray)
        dispatcher.send(session, message)
    }

}