package me.jinuo.imf.websocket.definition

import me.jinuo.imf.websocket.codec.message.Message
import me.jinuo.imf.websocket.session.Session
import org.slf4j.LoggerFactory
import java.lang.reflect.Method

class MethodProcessor : Processor {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    private lateinit var target: Any

    private lateinit var method: Method

    private lateinit var definition: TypeDefinition

    companion object {
        //构造方法
        fun valueOf(target: Any, method: Method, name2Parameters: Map<String, Parameter<*>>): MethodProcessor {
            val methodProcessor = MethodProcessor()
            methodProcessor.definition = MethodDefinition.valueOf(target::class.java, method, name2Parameters)
            methodProcessor.target = target
            methodProcessor.method = method
            return methodProcessor
        }
    }

    override fun process(session: Session, message: Message, callback: ResultCallback) {
        val args = definition.resolveArg(session, message, callback)
        try {
            val back = method.invoke(target, *args)
            //业务层通过return反回，把消息体发送给客户端口
            if (!definition.isCallback()) {
                callback.call(back)
            }
        } catch (e: Exception) {
            logger.error("执行方法[{}]发生未知错误！", method.name, e)
            throw e
        }
    }

}