package me.jinuo.imf.websocket.event

import org.springframework.context.ApplicationEvent
import org.springframework.context.ApplicationEventPublisher

/**
 * 对Spring事件的封装
 */
class EventBusImpl :EventBus{

    private lateinit var publisher:ApplicationEventPublisher


    override fun post(event: ApplicationEvent) {
        publisher.publishEvent(event)
    }


}