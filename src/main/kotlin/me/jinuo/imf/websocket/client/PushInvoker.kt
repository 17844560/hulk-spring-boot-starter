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

}