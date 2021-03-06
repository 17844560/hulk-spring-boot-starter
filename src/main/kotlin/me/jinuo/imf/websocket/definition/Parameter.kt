package me.jinuo.imf.websocket.definition

import me.jinuo.imf.websocket.codec.message.Message
import me.jinuo.imf.websocket.session.Session

/**
 * @author frank
 * @date 2019-10-25 10:36
 * @desc
 **/
interface Parameter<T> {

    fun getValue(meta: ArgMeta<T>, session: Session, message: Message, callback: ResultCallback<Any?>): Any?

    fun getV(meta: ArgMeta<*>, session: Session, message: Message, callback: ResultCallback<Any?>): Any? {
        @Suppress("UNCHECKED_CAST")
        return getValue(meta as ArgMeta<T>, session, message, callback)
    }

}