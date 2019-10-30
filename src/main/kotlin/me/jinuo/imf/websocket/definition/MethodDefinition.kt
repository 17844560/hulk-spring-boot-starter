package me.jinuo.imf.websocket.definition

import me.jinuo.imf.websocket.codec.message.Message
import me.jinuo.imf.websocket.execption.ParameterException
import me.jinuo.imf.websocket.parameter.Body
import me.jinuo.imf.websocket.parameter.Callback
import me.jinuo.imf.websocket.parameter.ISession
import me.jinuo.imf.websocket.parameter.impl.CallbackParameterImpl
import me.jinuo.imf.websocket.session.Session
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType

/**
 * frank
 */
class MethodDefinition : TypeDefinition {

    private lateinit var clazz: Class<*>
    private lateinit var method: Method
    private var callback: Boolean = false
    private lateinit var argMetas: Array<ArgMeta<*>?>

    companion object {
        fun valueOf(clazz: Class<*>, method: Method, name2Parameters: Map<String, Parameter<*>>): TypeDefinition {
            val typeDefinition = MethodDefinition()
            typeDefinition.argMetas = buildParameters(method, name2Parameters)
            typeDefinition.clazz = clazz
            typeDefinition.method = method
            typeDefinition.callback = findCallbackExist(typeDefinition.argMetas)
            return typeDefinition
        }

        /** 结果反回方式（return/callback）*/
        private fun findCallbackExist(metas: Array<ArgMeta<*>?>): Boolean {
            if (metas.size == 0) {
                return false
            }
            for (i in metas.indices) {
                val p = metas[i]
                if (p?.parameter is CallbackParameterImpl) {
                    return true
                }
            }
            return false
        }


        /**
         * 创建参数数组
         * @param method 方法
         * @return
         */
        private fun buildParameters(method: Method, name2Parameters: Map<String, Parameter<*>>): Array<ArgMeta<*>?> {
            val types = method.genericParameterTypes
            val result = arrayOfNulls<ArgMeta<*>>(types.size)
            for (i in types.indices) {
                result[i] = valueOf(method, i, name2Parameters)
            }
            return result
        }

        private fun valueOf(method: Method, index: Int, name2Parameters: Map<String, Parameter<*>>): ArgMeta<*>? {
            require(index < method.genericParameterTypes.size) { "索引超过参数下标" }
            val type = method.genericParameterTypes[index]

            val callbackParameter = type is Class<*>
                    && ResultCallback::class.java.isAssignableFrom(type)
                    || type is ParameterizedType
                    && ResultCallback::class.java.isAssignableFrom(type.rawType as Class<*>)
            if (callbackParameter) {
                return name2Parameters[Callback::class.java.name]?.let { ArgMeta.valueOf(type, null, it) }
            }
            if (type is Class<*> && Session::class.java.isAssignableFrom(type)) {
                return name2Parameters[ISession::class.java.name]?.let { ArgMeta.valueOf(type, null, it) }
            }
            val annotations = method.parameterAnnotations[index]
            var parameter: Parameter<*>? = null
            var anno: Annotation? = null
            for (annotation in annotations) {
                val name = annotation.annotationClass.qualifiedName
                val current = name2Parameters[name]
                current ?: continue
                if (parameter != null) {
                    throw ParameterException("Method[${method.name}]，存在多个参数处理器！")
                }
                parameter = current
                anno = annotation
            }
            if (parameter != null && anno != null) {
                return ArgMeta.valueOf(type, anno, parameter)
            }
            val name = Body::class.java.name
            val target = name2Parameters[name]
            return ArgMeta.valueOf(type, null, target!!)
        }
    }

    /**
     * 解析方法参数
     */
    override fun resolveArg(session: Session, message: Message, callback: ResultCallback): Array<Any?> {
        val args = arrayOfNulls<Any?>(argMetas.size)
        for (index in argMetas.indices) {
            val meta = argMetas[index]
            args[index] = meta?.parameter?.getV(meta, session, message, callback)
        }
        return args
    }

    override fun isCallback(): Boolean {
        return callback
    }
}