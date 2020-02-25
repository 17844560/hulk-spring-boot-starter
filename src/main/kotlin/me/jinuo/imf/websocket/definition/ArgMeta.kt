package me.jinuo.imf.websocket.definition

import java.lang.reflect.Type

/**
 * @author frank
 * @date 2019-10-25 16:24
 * @desc
 **/
class ArgMeta<T>(var body: T, var callback: Boolean) {

    /** 参数处理器*/
    lateinit var parameter: Parameter<Any?>
    /** 参数类型*/
    lateinit var type: Type

    companion object {
        fun valueOf(type: Type, anno: Any?, parameter: Parameter<Any?>): ArgMeta<Any?> {
            return valueOf(type, anno, parameter, false)
        }

        fun valueOf(type: Type, anno: Any?, parameter: Parameter<Any?>, callback: Boolean): ArgMeta<Any?> {
            val meta = ArgMeta<Any?>(anno, callback)
            meta.parameter = parameter
            meta.type = type
            return meta
        }
    }

}