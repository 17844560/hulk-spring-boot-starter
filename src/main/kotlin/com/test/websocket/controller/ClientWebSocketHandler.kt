package com.test.websocket.controller

import me.jinuo.imf.websocket.event.OpenEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.web.socket.BinaryMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.BinaryWebSocketHandler

/**
 * @author frank
 * @date 2019-10-28 14:25
 * @desc
 **/
@Component
class ClientWebSocketHandler : BinaryWebSocketHandler() {

    override fun handleBinaryMessage(session: WebSocketSession, message: BinaryMessage) {
        //收到消息
        super.handleBinaryMessage(session, message)
    }

    @EventListener
    fun openConnection(openEvent: OpenEvent) {

    }

}