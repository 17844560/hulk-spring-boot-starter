package com.test.websocket.controller

import me.jinuo.imf.websocket.event.OpenEvent
import me.jinuo.imf.websocket.session.DefaultSessionManager
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import javax.annotation.Resource

/**
 * @author frank
 * @date 2019-10-28 14:27
 * @desc
 **/
@Controller
class WebSocketTest {

    @Resource
    private lateinit var clientFactory: ClientFactory

    @Resource
    private lateinit var sessionManager: DefaultSessionManager

    @GetMapping("/test")
    @ResponseBody
    fun test() {
//        val session = clientFactory.getConn("ws://127.0.0.1:8080/ws")
        val map = HashMap<String, Any>()
        map.put("a", "1")
        map.put("b", "2")
        map.put("c", "3")
        //广播消息
        sessionManager.getSession().forEach {
            it.send(0, 0, map)
        }
    }

    @EventListener
    fun openEvent(event: OpenEvent) {
        val map = HashMap<String, Any>()
        map.put("a1", "1")
        map.put("b1", "2")
        map.put("c1", "3")
        event.session.send(0, 0, map)
    }

}