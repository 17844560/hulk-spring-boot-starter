package me.jinuo.imf.websocket.client

import me.jinuo.imf.websocket.handler.DefaultDispatcher
import me.jinuo.imf.websocket.session.DefaultSessionManager
import me.jinuo.imf.websocket.session.Session
import org.slf4j.LoggerFactory
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import javax.annotation.Resource

/**
 * @author frank
 * @date 2019-10-31 10:11
 * @desc
 **/
class ClientFactory {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Resource
    private lateinit var webSocketClientHandler: WebSocketClientHandler

    @Resource
    private lateinit var sessionManager: DefaultSessionManager

    @Resource
    private lateinit var dispatcher: DefaultDispatcher

    fun getClient(url: String): Client {
        val connection = StandardWebSocketClient()
        val future = connection.doHandshake(webSocketClientHandler, url)
        try {
            val webSession = future.get()
            val session = sessionManager.createSession(webSession)
            webSession.attributes[Session.SESSION_KEY] = session
            val client = Client(session, sessionManager, dispatcher)
            session.setAttr(WebSocketClientHandler.CLIENT_KEY, client)
            return client
        } catch (e: Exception) {
            logger.error("连接服务器[{}]失败,发生未知错误[{}]", url, e.message)
            throw e
        }
    }

}