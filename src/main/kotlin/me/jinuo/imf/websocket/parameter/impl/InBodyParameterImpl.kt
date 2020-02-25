package me.jinuo.imf.websocket.parameter.impl

import me.jinuo.imf.websocket.codec.message.Message
import me.jinuo.imf.websocket.definition.ArgMeta
import me.jinuo.imf.websocket.definition.Parameter
import me.jinuo.imf.websocket.definition.ResultCallback
import me.jinuo.imf.websocket.execption.DecodeException
import me.jinuo.imf.websocket.execption.RequireException
import me.jinuo.imf.websocket.handler.DefaultDispatcher
import me.jinuo.imf.websocket.parameter.InBody
import me.jinuo.imf.websocket.session.Session

/**
 * @author frank
 * @date 2019-10-25 10:58
 * @desc
 **/

class InBodyParameterImpl : Parameter<InBody> {

    override fun getValue(meta: ArgMeta<InBody>, session: Session, message: Message, callback: ResultCallback<Any?>): Any? {
        try {
            val map = DefaultDispatcher.mapper.readValue(message.bodyJson, Map::class.java)
            val body = meta.body
            val value = map[body.value]
            if (value == null && body.require) {
                throw RequireException("JSON参数[${body.value}]不存在")
            }
            return value
        } catch (e: Exception) {
            throw DecodeException("序列化时发生错误[${message.bodyJson}]", e)
        }
    }

}