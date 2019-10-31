package me.jinuo.imf.websocket.event

import org.springframework.context.ApplicationEvent
import org.springframework.web.socket.WebSocketSession

/**
 * @author frank
 * @date 2019-10-31 10:29
 * @desc
 **/
class ProxyOpenEvent(p: Any, var source: WebSocketSession, var target: WebSocketSession) : ApplicationEvent(p)