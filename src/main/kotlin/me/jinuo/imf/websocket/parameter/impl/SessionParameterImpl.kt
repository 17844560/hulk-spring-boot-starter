package me.jinuo.imf.websocket.parameter.impl

import me.jinuo.imf.websocket.codec.message.Message
import me.jinuo.imf.websocket.definition.ArgMeta
import me.jinuo.imf.websocket.definition.Parameter
import me.jinuo.imf.websocket.definition.ResultCallback
import me.jinuo.imf.websocket.parameter.ISession
import me.jinuo.imf.websocket.session.Session

/**
 * @author frank
 * @date 2019-10-25 10:58
 * @desc 注入[Session]
 **/
class SessionParameterImpl : Parameter<ISession> {

    override fun getValue(meta: ArgMeta<ISession>, session: Session, message: Message, callback: ResultCallback<Any?>): Any? {
        return session
    }

}