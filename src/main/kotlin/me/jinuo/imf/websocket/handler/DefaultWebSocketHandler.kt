package me.jinuo.imf.websocket.handler

import me.jinuo.imf.websocket.event.CloseEvent
import me.jinuo.imf.websocket.event.ErrorEvent
import me.jinuo.imf.websocket.event.OpenEvent
import me.jinuo.imf.websocket.session.Session
import me.jinuo.imf.websocket.session.SessionManager
import org.springframework.context.ApplicationContext
import org.springframework.web.socket.BinaryMessage
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.BinaryWebSocketHandler
import javax.annotation.Resource


class DefaultWebSocketHandler : BinaryWebSocketHandler() {

    @Resource
    private lateinit var dispatcher: Dispatcher

    @Resource
    private lateinit var sessionManager: SessionManager<WebSocketSession, Session>


    @Resource
    private lateinit var applicationContext: ApplicationContext

    override fun handleBinaryMessage(webSocket: WebSocketSession, message: BinaryMessage) {
        val session = webSocket.attributes[Session.SESSION_KEY] as Session
        dispatcher.receiver(session, message)
    }


    override fun afterConnectionClosed(webSocket: WebSocketSession, status: CloseStatus) {
        val session = webSocket.attributes[Session.SESSION_KEY] as Session
        //发出关闭事件
        applicationContext.publishEvent(CloseEvent(this, session))
        sessionManager.destroySession(session)
        super.afterConnectionClosed(webSocket, status)
    }

    override fun afterConnectionEstablished(webSocket: WebSocketSession) {
        val session = sessionManager.createSession(webSocket)
        webSocket.attributes[Session.SESSION_KEY] = session
        //发出连接事件
        applicationContext.publishEvent(OpenEvent(this, session))
        super.afterConnectionEstablished(webSocket)
    }

    override fun handleTransportError(webSocket: WebSocketSession, exception: Throwable) {
        val session = webSocket.attributes[Session.SESSION_KEY] as Session
        //发出连接事件
        applicationContext.publishEvent(ErrorEvent(this, session, exception))
        super.handleTransportError(webSocket, exception)
    }
}