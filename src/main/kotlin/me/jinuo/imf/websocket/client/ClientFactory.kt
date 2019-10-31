package me.jinuo.imf.websocket.client

import me.jinuo.imf.websocket.handler.TargetWebSocketHandler
import org.slf4j.LoggerFactory
import org.springframework.web.socket.WebSocketSession
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
    private lateinit var targetWebSocketHandler: TargetWebSocketHandler

    fun getConn(url: String): WebSocketSession? {
        val client = StandardWebSocketClient()
        val future = client.doHandshake(targetWebSocketHandler, url)
        try {
            return future.get()
        } catch (e: Exception) {
            logger.error("连接游戏服务器[{}]失败,发生未知错误[{}]", url, e.message)
            throw e
        }
    }
}