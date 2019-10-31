package me.jinuo.imf.websocket.handler

import me.jinuo.imf.websocket.event.ProxyOpenEvent
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.web.socket.BinaryMessage
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.BinaryWebSocketHandler
import javax.annotation.Resource

/**
 * @author frank
 * @date 2019-10-31 10:11
 * @desc
 **/
class TargetWebSocketHandler : BinaryWebSocketHandler() {
    private val logger = LoggerFactory.getLogger(javaClass)
    @Resource
    private lateinit var applicationContext: ApplicationContext

    override fun afterConnectionEstablished(target: WebSocketSession) {
        val source = target.attributes[ProxyWebSocketHandler.SOURCE_SESSION] as WebSocketSession
        //代理服务器连接成功事件
        applicationContext.publishEvent(ProxyOpenEvent(this, source, target))
        super.afterConnectionEstablished(target)
    }

    override fun handleBinaryMessage(target: WebSocketSession, message: BinaryMessage) {
        val source = target.attributes[ProxyWebSocketHandler.SOURCE_SESSION] as WebSocketSession
        //目标服务器的消息转发到客户端
        if (source.isOpen) {
            source.sendMessage(message)
        }
        super.handleBinaryMessage(target, message)
    }

    override fun afterConnectionClosed(target: WebSocketSession, status: CloseStatus) {
        logger.debug("目标服务器[{}]会话关闭", target.remoteAddress)
        val source = target.attributes[ProxyWebSocketHandler.SOURCE_SESSION] as WebSocketSession
        source.close()
        super.afterConnectionClosed(target, status)
    }

}