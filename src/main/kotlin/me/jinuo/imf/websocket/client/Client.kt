package me.jinuo.imf.websocket.client

import me.jinuo.imf.websocket.codec.message.Header
import me.jinuo.imf.websocket.codec.message.Message
import me.jinuo.imf.websocket.codec.message.MessageState
import me.jinuo.imf.websocket.definition.Command
import me.jinuo.imf.websocket.handler.DefaultDispatcher
import me.jinuo.imf.websocket.session.DefaultSessionManager
import me.jinuo.imf.websocket.session.Session
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

/**
 * @author frank
 * @date 2019-11-14 17:28
 * @desc
 **/
class Client(
        var session: Session,
        var sessionManager: DefaultSessionManager,
        var dispatcher: DefaultDispatcher,
        var callbacks: ConcurrentMap<Int, () -> Unit> = ConcurrentHashMap()
) {

    /**
     * 发送消息，有回调
     */
    fun send(command: Command, message: Any, callback: () -> Unit) {
        val sn = sessionManager.nextSn()
        callbacks.put(sn, callback)
        val header = Header.valueOf(sn, MessageState.NORMAL.state, command.module, command.command)
        val bodyArray = dispatcher.encode(message)
        val send = Message.valueOf(header, bodyArray)
        session.send(send)

    }

    /**
     * 发送消息，无回调
     */
    fun send(command: Command, message: Any) {
        session.send(command, message)
    }

    /**
     * 回复消息
     */
    fun receiver(message: Message) {
        val callback = callbacks[message.header.sn]
        callback ?: return
        callback.invoke()
    }
}