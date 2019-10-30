package me.jinuo.imf.websocket.event

import me.jinuo.imf.websocket.session.Session
import org.springframework.context.ApplicationEvent

/**
 * @author frank
 * @date 2019-10-28 10:15
 * @desc 心跳（长时间没有数据交换）超时事件
 **/
class TimeoutEvent(source: Any, var session: Session) : ApplicationEvent(source)