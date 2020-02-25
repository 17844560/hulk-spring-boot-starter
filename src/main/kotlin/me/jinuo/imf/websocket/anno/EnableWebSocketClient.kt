package me.jinuo.imf.websocket.anno

import me.jinuo.imf.websocket.client.ClientConfigure
import me.jinuo.imf.websocket.configure.RegisterConfiguration
import me.jinuo.imf.websocket.configure.WebSocketConfiguration
import org.springframework.context.annotation.Import

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@MustBeDocumented
annotation
@Import(ClientConfigure::class)
class EnableWebSocketClient(val value: String = "/ws", val proxy: Boolean = false)

