package me.jinuo.imf.websocket.factory

import me.jinuo.imf.websocket.anno.WSController
import me.jinuo.imf.websocket.anno.WSMapping
import me.jinuo.imf.websocket.definition.Command
import me.jinuo.imf.websocket.definition.MethodProcessor
import me.jinuo.imf.websocket.definition.Parameter
import me.jinuo.imf.websocket.execption.ParameterException
import org.springframework.beans.BeansException
import org.springframework.beans.factory.FactoryBean
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import java.lang.reflect.ParameterizedType
import javax.annotation.PostConstruct

@Suppress("UNCHECKED_CAST")
class RegisterFactory : FactoryBean<CommandRegister>, BeanPostProcessor, ApplicationContextAware {

    private lateinit var applicationContext: ApplicationContext

    private var register: CommandRegister? = null

    private var parameters = HashMap<String, Parameter<Any?>>()

    private var init = false
    private var postProcessAfterInitialization = false

    override fun getObjectType(): Class<*> {
        return CommandRegister::class.java
    }

    @PostConstruct
    fun initializer() {
        if (init) {
            return
        }
        init = true
        val parameters = applicationContext.getBeansOfType(Parameter::class.java)
        for (entry in parameters) {
            val target = entry.value
            val types = target::class.java.genericInterfaces
            if (types.size > 1) {
                throw ParameterException("参数处理器只支持实现一个接口[Parameter]")
            }
            val type = types[0]
//            val type = target::class.java.genericSuperclass
            if (type is ParameterizedType) {
                val clz = type.actualTypeArguments[0]
                if (this.parameters.putIfAbsent(clz.typeName, target as Parameter<Any?>) != null) {
                    throw ParameterException("参数处理器[$type]重复")
                }
            }
        }
    }

    @Throws(BeansException::class)
    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any? {
        return super.postProcessBeforeInitialization(bean, beanName)
    }

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
        if (postProcessAfterInitialization) {
            return bean
        }
        postProcessAfterInitialization = true
        val controller = bean::class.java.getAnnotation(WSController::class.java) ?: return bean
        //模块
        val module = controller.value
        val methods = bean::class.java.declaredMethods
        for (method in methods) {
            val wsMapping = method.getAnnotation(WSMapping::class.java) ?: continue
            val cmd = wsMapping.value
            //指令
            val command = Command(module, cmd)
            val processor = MethodProcessor.valueOf(bean, method, parameters)
            `object`.register(command, processor)
        }
        return bean
    }


    override fun getObject(): CommandRegister {
        if (register != null) {
            return register!!
        }
        synchronized(this) {
            if (register != null) {
                return register!!
            }
            register = CommandRegister()
            return register!!
        }
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }
}