package com.test.websocket.controller

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import javax.annotation.Resource


@Component
class ClientFactory {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Resource
    private lateinit var clientWebSocketHandler: ClientWebSocketHandler

    fun getConn(url: String): WebSocketSession? {
        val client = StandardWebSocketClient()
        val future = client.doHandshake(clientWebSocketHandler, url)
        try {
            return future.get()
        } catch (e: Exception) {
            logger.error("连接WebSocket服务器[{}]失败", url, e)
        }
        return null
    }
}