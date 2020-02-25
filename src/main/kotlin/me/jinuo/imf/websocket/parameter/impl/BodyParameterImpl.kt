package me.jinuo.imf.websocket.parameter.impl

import me.jinuo.imf.websocket.codec.message.Message
import me.jinuo.imf.websocket.definition.ArgMeta
import me.jinuo.imf.websocket.definition.Parameter
import me.jinuo.imf.websocket.definition.ResultCallback
import me.jinuo.imf.websocket.handler.DefaultDispatcher
import me.jinuo.imf.websocket.parameter.Body
import me.jinuo.imf.websocket.session.Session

/**
 * @author frank
 * @date 2019-10-25 14:40
 * @desc
 **/
class BodyParameterImpl : Parameter<Body> {

    override fun getValue(meta: ArgMeta<Body>, session: Session, message: Message, callback: ResultCallback<Any?>): Any {
        return if (meta.type.equals(String::class.java)) {
            @Suppress("USELESS_CAST")
            message.bodyJson as Any
        } else {
            DefaultDispatcher.mapper.readValue(message.bodyJson, DefaultDispatcher.typeFactory.constructType(meta.type))
        }
    }
}