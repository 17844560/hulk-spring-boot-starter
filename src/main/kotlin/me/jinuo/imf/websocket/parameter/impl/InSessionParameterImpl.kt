package me.jinuo.imf.websocket.parameter.impl

import me.jinuo.imf.websocket.codec.message.Message
import me.jinuo.imf.websocket.definition.ArgMeta
import me.jinuo.imf.websocket.definition.Parameter
import me.jinuo.imf.websocket.definition.ResultCallback
import me.jinuo.imf.websocket.execption.RequireException
import me.jinuo.imf.websocket.parameter.InSession
import me.jinuo.imf.websocket.session.Session

/**
 * @author frank
 * @date 2019-10-25 10:58
 * @desc [Session.getAttr]参数
 **/
class InSessionParameterImpl : Parameter<InSession> {

    override fun getValue(meta: ArgMeta<InSession>, session: Session, message: Message, callback: ResultCallback<Any?>): Any? {
        val inSession = meta.body
        val value = if (inSession.value.equals(Session.IDENTITY)) {
            session.getIdentity()
        } else {
            session.getAttr<Any?>(inSession.value)
        }
        if (value == null && inSession.require) {
            throw RequireException("SESSION参数[${inSession.value}]不存在！")
        }
        return value
    }

}