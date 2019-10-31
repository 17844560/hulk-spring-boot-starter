package me.jinuo.imf.websocket.handler

import me.jinuo.imf.websocket.client.ClientFactory
import me.jinuo.imf.websocket.properties.GlobalConfig
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.web.socket.BinaryMessage
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.BinaryWebSocketHandler
import javax.annotation.Resource

/**
 * @author frank
 * @date 2019-10-31 10:04
 * @desc 代理handler,消息转发到远程服务器
 **/
@EnableConfigurationProperties(GlobalConfig::class)
class ProxyWebSocketHandler : BinaryWebSocketHandler() {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Resource
    private lateinit var clientFactory: ClientFactory
    @Resource
    private lateinit var globalConfig: GlobalConfig

    companion object {
        val SOURCE_SESSION = "SOURCE_SESSION"
        val TARGET_SESSION = "TARGET_SESSION"
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val targetUrl = "ws://" + globalConfig.proxyHost + ":" + globalConfig.proxyPort + session.uri!!.query
        logger.debug("开始连接代理服务器[{}]", targetUrl)
        val target = clientFactory.getConn(targetUrl)
        if (target == null || !target.isOpen) {
            logger.debug("代理服务器[{}]连接失败", targetUrl)
            session.close()
            return
        }
        //绑定通道关系
        session.attributes.put(TARGET_SESSION, target)
        target.attributes.put(SOURCE_SESSION, session)
        super.afterConnectionEstablished(session)
    }

    override fun handleBinaryMessage(session: WebSocketSession, message: BinaryMessage) {
        val target = session.attributes[TARGET_SESSION] as WebSocketSession
        if (!target.isOpen) {
            //与目标服务器连接已断开
            session.close()
            return
        }
        //消息转发到目标服务器
        if (session.isOpen) {
            target.sendMessage(message)
        }
        super.handleBinaryMessage(session, message)
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        logger.debug("代理来源[{}]会话关闭", session.remoteAddress)
        val target = session.attributes[TARGET_SESSION] as WebSocketSession
        target.close()
        super.afterConnectionClosed(session, status)
    }

}