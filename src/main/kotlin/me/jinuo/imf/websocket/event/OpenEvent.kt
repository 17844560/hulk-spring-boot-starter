package me.jinuo.imf.websocket.event

import me.jinuo.imf.websocket.session.Session
import org.springframework.context.ApplicationEvent

/**
 * @author frank
 * @date 2019-10-28 10:15
 * @desc 会话建立事件
 **/
class OpenEvent(source: Any, var session: Session) : ApplicationEvent(source)