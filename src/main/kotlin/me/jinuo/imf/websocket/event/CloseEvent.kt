package me.jinuo.imf.websocket.event

import me.jinuo.imf.websocket.session.Session
import org.springframework.context.ApplicationEvent

/**
 * @author frank
 * @date 2019-10-28 10:11
 * @desc 会话关闭
 **/
class CloseEvent(source: Any, var session: Session) : ApplicationEvent(source)