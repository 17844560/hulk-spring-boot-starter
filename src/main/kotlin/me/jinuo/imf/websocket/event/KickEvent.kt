package me.jinuo.imf.websocket.event

import me.jinuo.imf.websocket.session.Session
import org.springframework.context.ApplicationEvent

/**
 * @author frank
 * @date 2019-10-28 10:15
 * @desc 重复绑定会话身份，被踢下线
 **/
class KickEvent(source: Any, var session: Session) : ApplicationEvent(source)