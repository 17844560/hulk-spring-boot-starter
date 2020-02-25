package me.jinuo.imf.websocket.client

import me.jinuo.imf.websocket.codec.message.MessageState
import me.jinuo.imf.websocket.handler.DefaultDispatcher
import me.jinuo.imf.websocket.session.DefaultSessionManager
import me.jinuo.imf.websocket.session.Session
import org.springframework.web.socket.BinaryMessage
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.BinaryWebSocketHandler
import javax.annotation.Resource

/**
 * @author frank
 * @date 2019-11-14 17:24
 * @desc
 **/
class WebSocketClientHandler : BinaryWebSocketHandler() {

    companion object {
        val CLIENT_KEY = "WEBSOCKET_CLIENT_KEY"
    }

    @Resource
    private lateinit var sessionManager: DefaultSessionManager

    @Resource
    private lateinit var dispatcher: DefaultDispatcher

    @Resource
    private lateinit var clientFactory: ClientFactory

    override fun handleBinaryMessage(webSession: WebSocketSession, binaryMessage: BinaryMessage) {
        val session = webSession.attributes[Session.SESSION_KEY] as Session
        val message = dispatcher.decode(binaryMessage)
        message ?: return
        if (message.header.hasState(MessageState.RESPONSE.state)) {
            val client = session.getAttr<Client>(CLIENT_KEY)
            client ?: return
            return client.receiver(message)
        }
        dispatcher.receiver(session, binaryMessage)
    }

    override fun afterConnectionClosed(webSession: WebSocketSession, status: CloseStatus) {
        val session = webSession.attributes[Session.SESSION_KEY] as Session
        sessionManager.destroySession(session)
    }

}