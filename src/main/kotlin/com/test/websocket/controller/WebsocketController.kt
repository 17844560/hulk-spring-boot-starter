package com.test.websocket.controller

import me.jinuo.imf.websocket.anno.WSController
import me.jinuo.imf.websocket.anno.WSMapping
import me.jinuo.imf.websocket.definition.ResultCallback
import me.jinuo.imf.websocket.parameter.ISession
import me.jinuo.imf.websocket.session.Session

/**
 * @author frank
 * @date 2019-10-24 20:16
 * @desc
 */
@WSController(0)
class WebsocketController {


    @WSMapping(0)
    fun test1(session: Session, callback: ResultCallback, body: String): String {
        println("test1收到消息：$body")
        return "服务器回复消息"
    }

    @WSMapping(1)
    fun test2(@ISession session: Session, body: String) {
        println("test2收到消息：$body")
    }

}
