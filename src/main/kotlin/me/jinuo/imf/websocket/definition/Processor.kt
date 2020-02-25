package me.jinuo.imf.websocket.definition

import me.jinuo.imf.websocket.codec.message.Message
import me.jinuo.imf.websocket.session.Session

interface Processor {

    fun process(session: Session, message: Message, callback: ResultCallback<Any?>)

}