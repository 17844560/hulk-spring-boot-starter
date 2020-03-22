package me.jinuo.imf.websocket.event

import org.springframework.context.ApplicationEvent


interface EventBus {

    /** 发出事件*/
    fun post(event: ApplicationEvent)

}