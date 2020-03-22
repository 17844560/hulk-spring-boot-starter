package me.jinuo.imf.websocket.configure

import me.jinuo.imf.websocket.anno.EnableWebSocket
import me.jinuo.imf.websocket.codec.BinaryCodec
import me.jinuo.imf.websocket.event.EventBus
import me.jinuo.imf.websocket.event.EventBusImpl
import me.jinuo.imf.websocket.factory.RegisterFactory
import me.jinuo.imf.websocket.handler.DefaultDispatcher
import me.jinuo.imf.websocket.handler.DefaultWebSocketHandler
import me.jinuo.imf.websocket.handler.ProxyWebSocketHandler
import me.jinuo.imf.websocket.handler.TargetWebSocketHandler
import me.jinuo.imf.websocket.parameter.impl.*
import me.jinuo.imf.websocket.session.DefaultSessionManager
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.type.AnnotationMetadata

@Configuration
@Suppress("UNUSED_PARAMETER")
class RegisterConfiguration : ImportBeanDefinitionRegistrar {


    override fun registerBeanDefinitions(meta: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        //注册参数处理器
        registerParameter(meta, registry)
        //注册编解码器
        registerCodec(meta, registry)
        //注册sessionManager
        registerSessionManager(meta, registry)
        //注册指令分发器
        registerDispatcher(meta, registry)
        //注册beanFactory(指令注册)
        registerRegisterFactory(registry)
        //注册handler
        registerWebSocketHandler(registry)
        //注册代理相关配置
        registryProxy(meta, registry)
    }

    private fun registryProxy(meta: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        val attr = meta.getAnnotationAttributes(EnableWebSocket::class.java.name)
        val proxy = attr!!["proxy"] as Boolean
        if (!proxy) {
            return
        }
        if (!registry.containsBeanDefinition("targetWebSocketHandler")) {
            val targetHandlerBuilder = BeanDefinitionBuilder.genericBeanDefinition(TargetWebSocketHandler::class.java)
            registry.registerBeanDefinition("targetWebSocketHandler", targetHandlerBuilder.beanDefinition)
        }
        if (!registry.containsBeanDefinition("proxyWebSocketHandler")) {
            val proxyHandlerBuilder = BeanDefinitionBuilder.genericBeanDefinition(ProxyWebSocketHandler::class.java)
            registry.registerBeanDefinition("proxyWebSocketHandler", proxyHandlerBuilder.beanDefinition)
        }
    }

    /**
     * 注册webSocket处理器
     */
    private fun registerWebSocketHandler(registry: BeanDefinitionRegistry) {
        if (!registry.containsBeanDefinition("defaultWebSocketHandler")) {
            val builder = BeanDefinitionBuilder.genericBeanDefinition(DefaultWebSocketHandler::class.java)
            registry.registerBeanDefinition("defaultWebSocketHandler", builder.beanDefinition)
        }
    }

    /**
     * 注册指令工厂
     */
    private fun registerRegisterFactory(registry: BeanDefinitionRegistry) {
        if (!registry.containsBeanDefinition("registerFactory")) {
            val registerFactoryBuilder = BeanDefinitionBuilder.genericBeanDefinition(RegisterFactory::class.java)
            registry.registerBeanDefinition("registerFactory", registerFactoryBuilder.beanDefinition)
        }
    }

    /**
     * 注册指令分发器
     */
    private fun registerDispatcher(meta: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        if (!registry.containsBeanDefinition("defaultDispatcher")) {
            val defaultDispatcherBuilder = BeanDefinitionBuilder.genericBeanDefinition(DefaultDispatcher::class.java)
            registry.registerBeanDefinition("defaultDispatcher", defaultDispatcherBuilder.beanDefinition)
        }
    }

    /**
     * 注册Session管理器
     */
    private fun registerSessionManager(meta: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        if (!registry.containsBeanDefinition("defaultSessionManager")) {
            val sessionManagerBuilder = BeanDefinitionBuilder.genericBeanDefinition(DefaultSessionManager::class.java)
            registry.registerBeanDefinition("defaultSessionManager", sessionManagerBuilder.beanDefinition)
        }
    }

    /**
     * 注册编解码器
     */
    private fun registerCodec(meta: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        if (!registry.containsBeanDefinition("binaryCodec")) {
            val binaryCodecBuilder = BeanDefinitionBuilder.genericBeanDefinition(BinaryCodec::class.java)
            registry.registerBeanDefinition("binaryCodec", binaryCodecBuilder.beanDefinition)
        }
    }

    /**
     * 注册参数处理器
     */
    private fun registerParameter(meta: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        if (!registry.containsBeanDefinition("inSessionParameterImpl")) {
            val inSessionBuilder = BeanDefinitionBuilder.genericBeanDefinition(InSessionParameterImpl::class.java)
            registry.registerBeanDefinition("inSessionParameterImpl", inSessionBuilder.beanDefinition)
        }
        if (!registry.containsBeanDefinition("inBodyParameterImpl")) {
            val inBodyBuilder = BeanDefinitionBuilder.genericBeanDefinition(InBodyParameterImpl::class.java)
            registry.registerBeanDefinition("inBodyParameterImpl", inBodyBuilder.beanDefinition)
        }
        if (!registry.containsBeanDefinition("defaultParameterImpl")) {
            val defaultBuilder = BeanDefinitionBuilder.genericBeanDefinition(BodyParameterImpl::class.java)
            registry.registerBeanDefinition("defaultParameterImpl", defaultBuilder.beanDefinition)
        }
        if (!registry.containsBeanDefinition("callbackParameterImpl")) {
            val callbackBuilder = BeanDefinitionBuilder.genericBeanDefinition(CallbackParameterImpl::class.java)
            registry.registerBeanDefinition("callbackParameterImpl", callbackBuilder.beanDefinition)
        }
        if (!registry.containsBeanDefinition("sessionParameterImpl")) {
            val sessionBuilder = BeanDefinitionBuilder.genericBeanDefinition(SessionParameterImpl::class.java)
            registry.registerBeanDefinition("sessionParameterImpl", sessionBuilder.beanDefinition)
        }
    }

    /*fun registerCommand(metadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        val scanner = ClassPathScanningCandidateComponentProvider(false);
        scanner.setResourceLoader(this.resourceLoader)
        val annotationTypeFilter = AnnotationTypeFilter(WSController::class.java)
        scanner.addIncludeFilter(annotationTypeFilter)

        val attrs = metadata
                .getAnnotationAttributes(me.jinuo.imf.websocket.anno.EnableWebSocket::class.java.getName())
        val basePackages = HashSet<String>()
        basePackages.add(
                ClassUtils.getPackageName(metadata.className))
        for (basePackage in basePackages) {
            val components = scanner.findCandidateComponents(basePackage)
            for (beanDefinition in components) {
                val clzName = beanDefinition.beanClassName
                val clz = Class.forName(clzName)
            }
        }
    }*/
    /**
     * 注册事件处理器
     */
    @Bean
    fun registerEventBus():EventBusImpl{
        return  EventBusImpl()
    }
}
