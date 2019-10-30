package me.jinuo.imf.websocket.configure

import me.jinuo.imf.websocket.anno.EnableWebSocket
import me.jinuo.imf.websocket.handler.DefaultWebSocketHandler
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportAware
import org.springframework.core.type.AnnotationMetadata
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import javax.annotation.Resource

@Configuration
@org.springframework.web.socket.config.annotation.EnableWebSocket
class WebSocketConfiguration : WebSocketConfigurer, ImportAware {

    private lateinit var annotationMetadata: AnnotationMetadata

    override fun setImportMetadata(annotationMetadata: AnnotationMetadata) {
        this.annotationMetadata = annotationMetadata
    }

    @Resource
    private lateinit var defaultWebSocketHandler: DefaultWebSocketHandler

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        val attr = annotationMetadata.getAnnotationAttributes(EnableWebSocket::class.java.name)
        val path = attr!!["value"].toString()
        registry.addHandler(defaultWebSocketHandler, path).setAllowedOrigins("*")
    }

}