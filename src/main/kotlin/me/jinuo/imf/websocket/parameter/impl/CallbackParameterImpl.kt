package me.jinuo.imf.websocket.parameter.impl

import me.jinuo.imf.websocket.codec.message.Message
import me.jinuo.imf.websocket.definition.ArgMeta
import me.jinuo.imf.websocket.definition.Parameter
import me.jinuo.imf.websocket.definition.ResultCallback
import me.jinuo.imf.websocket.parameter.Callback
import me.jinuo.imf.websocket.session.Session


/**
 * @author frank
 * @date 2019-10-25 14:40
 * @desc
 **/
class CallbackParameterImpl : Parameter<Callback>() {

    override fun getValue(meta: ArgMeta<Callback>, session: Session, message: Message, callback: ResultCallback): Any {
        return callback
    }
}