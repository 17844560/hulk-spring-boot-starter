package me.jinuo.imf.websocket.definition

import java.lang.reflect.Type

/**
 * @author frank
 * @date 2019-10-25 16:24
 * @desc
 **/
class ArgMeta<T>(var body: T) {

    /** 参数处理器*/
    lateinit var parameter: Parameter<*>
    /** 参数类型*/
    lateinit var type: Type

    companion object {
        fun valueOf(type: Type, anno: Any?, parameter: Parameter<*>): ArgMeta<Any?> {
            val meta = ArgMeta<Any?>(anno)
            meta.parameter = parameter
            meta.type = type
            return meta
        }
    }

}