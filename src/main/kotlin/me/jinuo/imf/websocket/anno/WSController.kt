package me.jinuo.imf.websocket.anno

import org.springframework.stereotype.Component

@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
@Component
annotation class WSController(val value: Short = 0)