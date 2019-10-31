package me.jinuo.imf.websocket.configure

import me.jinuo.imf.websocket.anno.EnableWebSocket
import me.jinuo.imf.websocket.handler.DefaultWebSocketHandler
import me.jinuo.imf.websocket.handler.ProxyWebSocketHandler
import me.jinuo.imf.websocket.properties.GlobalConfig
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportAware
import org.springframework.core.type.AnnotationMetadata
import org.springframework.util.StringUtils
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import javax.annotation.Resource

@Configuration
@org.springframework.web.socket.config.annotation.EnableWebSocket
@EnableConfigurationProperties(GlobalConfig::class)
class WebSocketConfiguration : WebSocketConfigurer, ImportAware {
    private val logger = LoggerFactory.getLogger(javaClass)
    private lateinit var annotationMetadata: AnnotationMetadata

    override fun setImportMetadata(annotationMetadata: AnnotationMetadata) {
        this.annotationMetadata = annotationMetadata
    }

    @Resource
    private lateinit var globalConfig: GlobalConfig

    @Resource
    private lateinit var defaultWebSocketHandler: DefaultWebSocketHandler

    @Autowired(required = false)
    private lateinit var proxyWebSocketHandler: ProxyWebSocketHandler

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        val attr = annotationMetadata.getAnnotationAttributes(EnableWebSocket::class.java.name)
        val path = attr!!["value"].toString()
        val proxy = attr["proxy"] as Boolean
        var handler = defaultWebSocketHandler as WebSocketHandler
        if (proxy) {
            require(!(StringUtils.isEmpty(globalConfig.proxyHost) || globalConfig.proxyPort == 0)) { "proxy=true, 缺少必须配置项[websocket.proxyHost]或[websocket.proxyPort]" }
            handler = proxyWebSocketHandler
        }
        registry.addHandler(handler, path).setAllowedOrigins("*")
        // sockJs通道
        registry.addHandler(handler, path)
                // 指定自定义拦截器
                // .addInterceptors(new WebSocketInterceptor())
                .setAllowedOrigins("*")
                // 开启sockJs支持
                .withSockJS()
    }
}