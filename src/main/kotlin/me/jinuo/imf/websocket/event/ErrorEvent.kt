package me.jinuo.imf.websocket.event

import me.jinuo.imf.websocket.session.Session
import org.springframework.context.ApplicationEvent

/**
 * @author frank
 * @date 2019-10-29 10:15
 * @desc
 **/
class ErrorEvent(source: Any, var session: Session, var exception: Throwable) : ApplicationEvent(source)