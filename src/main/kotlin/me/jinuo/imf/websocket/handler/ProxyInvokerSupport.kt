import me.jinuo.imf.websocket.anno.WSController
import me.jinuo.imf.websocket.anno.WSMapping
import me.jinuo.imf.websocket.definition.Command
import me.jinuo.imf.websocket.parameter.InBody
import me.jinuo.imf.websocket.parameter.InSession
import me.jinuo.imf.websocket.session.Session
import org.slf4j.LoggerFactory
import java.lang.reflect.Method
import java.util.*
import java.util.concurrent.ConcurrentHashMap

open class ProxyInvokerSupport<T> {
    protected var logger = LoggerFactory.getLogger(javaClass)
    /**
     * 构建请求　
     * @param method
     * @param args
     * @return
     */
    protected fun buildRequest(method: Method, args: Array<Any?>?): Any? {
        var info = caches[method]
        if (info == null) {
            val declaringClass = method.declaringClass
            val socketModule: WSController = declaringClass.getAnnotation(WSController::class.java)
            val module: Short = socketModule.value
            val socketCommand: WSMapping = method.getAnnotation(WSMapping::class.java)
            // 创建指令对象
            val command: Command = Command(socketCommand.value, module)
            val params: MutableMap<Int, ParamMeta> = HashMap()
            val paramAnnotations = method.parameterAnnotations
            val paramTypes = method.parameterTypes
            if (paramTypes.size == 1) {
                val type = paramTypes[0]
                if (!type.isAssignableFrom(Session::class.java)) {
                    var inrequest = true
                    for (a in paramAnnotations[0]) {
                        if (a is InSession) {
                            inrequest = false
                            break
                        }
                        if (a is InBody) {
                            val name: String = (a as InBody).value
                            params[0] = ParamMeta.valueOf(name, type)
                            inrequest = false
                            break
                        }
                    }
                    if (inrequest) {
                        params[0] = ParamMeta.valueOf(null, type)
                    }
                }
            } else if (paramTypes.size > 1) {
                for (index in paramAnnotations.indices) {
                    val type = paramTypes[index]
                    if (type.isAssignableFrom(Session::class.java)) {
                        continue
                    }
                    var inrequest = true
                    for (a in paramAnnotations[index]) {
                        if (a is InSession) {
                            inrequest = false
                            break
                        }
                        if (a is InBody) {
                            val name: String = (a as InBody).value
                            params[index] = ParamMeta.valueOf(name, type)
                            inrequest = false
                            break
                        }
                    }
                    if (inrequest) {
                        params[index] = ParamMeta.valueOf(null, type)
                    }
                }
            }
            info = InvokerMeta.valueOf(command, params)
            caches.putIfAbsent(method, info)
        }
        val command: Command? = info.getCommand()
        val params = info.params
        val body: Any?
        if (args == null) {
            body = null
        } else {
            val length = args.size
            if (length == 0) {
                body = null
            } else if (length == 1) {
                val paramMeta = params!![0]
                if (paramMeta == null || paramMeta.name == null) {
                    body = args[0]
                } else {
                    val p: MutableMap<String?, Any?> = HashMap()
                    for (i in args.indices) {
                        p[paramMeta.name] = args[i]
                    }
                    body = p
                }
            } else {
                val p: MutableMap<String?, Any?> = HashMap()
                var inrequest: Any? = null
                for (i in args.indices) {
                    val paramMeta = params!![i] ?: continue
                    if (paramMeta.name == null) {
                        inrequest = args[i]
                        break
                    }
                    p[paramMeta.name] = args[i]
                }
                body = inrequest ?: p
            }
        }
        return body
    }

    class InvokerMeta {
        private var command: Command? = null
        var params: Map<Int, ParamMeta>? = null
            private set

        fun getCommand(): Command? {
            return command
        }

        companion object {
            fun valueOf(command: Command?, params: Map<Int, ParamMeta>?): InvokerMeta {
                val e = InvokerMeta()
                e.command = command
                e.params = params
                return e
            }
        }
    }

     class ParamMeta {
        var name: String? = null
            private set
        var type: Class<*>? = null
            private set

        companion object {
            fun valueOf(name: String?, type: Class<*>?): ParamMeta {
                val e = ParamMeta()
                e.name = name
                e.type = type
                return e
            }
        }
    }

    companion object {
        /** 方法对应指令缓存对象  */
        val caches = ConcurrentHashMap<Method, InvokerMeta?>()
    }
}