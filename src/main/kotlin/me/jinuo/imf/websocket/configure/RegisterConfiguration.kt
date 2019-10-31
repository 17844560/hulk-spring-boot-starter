package me.jinuo.imf.websocket.configure

import me.jinuo.imf.websocket.anno.EnableWebSocket
import me.jinuo.imf.websocket.client.ClientFactory
import me.jinuo.imf.websocket.codec.BinaryCodec
import me.jinuo.imf.websocket.factory.RegisterFactory
import me.jinuo.imf.websocket.handler.DefaultDispatcher
import me.jinuo.imf.websocket.handler.DefaultWebSocketHandler
import me.jinuo.imf.websocket.handler.ProxyWebSocketHandler
import me.jinuo.imf.websocket.handler.TargetWebSocketHandler
import me.jinuo.imf.websocket.parameter.impl.*
import me.jinuo.imf.websocket.session.DefaultSessionManager
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.type.AnnotationMetadata

@Configuration
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
        val targetHandlerBuilder = BeanDefinitionBuilder.genericBeanDefinition(TargetWebSocketHandler::class.java)
        registry.registerBeanDefinition("targetWebSocketHandler", targetHandlerBuilder.beanDefinition)
        val clientFactoryBuilder = BeanDefinitionBuilder.genericBeanDefinition(ClientFactory::class.java)
        registry.registerBeanDefinition("clientFactory", clientFactoryBuilder.beanDefinition)
        val proxyHandlerBuilder = BeanDefinitionBuilder.genericBeanDefinition(ProxyWebSocketHandler::class.java)
        registry.registerBeanDefinition("proxyWebSocketHandler", proxyHandlerBuilder.beanDefinition)
    }

    /**
     * 注册webSocket处理器
     */
    private fun registerWebSocketHandler(registry: BeanDefinitionRegistry) {
        val builder = BeanDefinitionBuilder.genericBeanDefinition(DefaultWebSocketHandler::class.java)
        registry.registerBeanDefinition("defaultWebSocketHandler", builder.beanDefinition)
    }

    /**
     * 注册指令工厂
     */
    private fun registerRegisterFactory(registry: BeanDefinitionRegistry) {
        val registerFactoryBuilder = BeanDefinitionBuilder.genericBeanDefinition(RegisterFactory::class.java)
        registry.registerBeanDefinition("registerFactory", registerFactoryBuilder.beanDefinition)
    }

    /**
     * 注册指令分发器
     */
    private fun registerDispatcher(meta: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        val defaultDispatcherBuilder = BeanDefinitionBuilder.genericBeanDefinition(DefaultDispatcher::class.java)
        registry.registerBeanDefinition("defaultDispatcher", defaultDispatcherBuilder.beanDefinition)
    }

    /**
     * 注册Session管理器
     */
    private fun registerSessionManager(meta: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        val sessionManagerBuilder = BeanDefinitionBuilder.genericBeanDefinition(DefaultSessionManager::class.java)
        registry.registerBeanDefinition("defaultSessionManager", sessionManagerBuilder.beanDefinition)
    }

    /**
     * 注册编解码器
     */
    private fun registerCodec(meta: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        val binaryCodecBuilder = BeanDefinitionBuilder.genericBeanDefinition(BinaryCodec::class.java)
        registry.registerBeanDefinition("binaryCodec", binaryCodecBuilder.beanDefinition)
    }

    /**
     * 注册参数处理器
     */
    private fun registerParameter(meta: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        val inSessionBuilder = BeanDefinitionBuilder.genericBeanDefinition(InSessionParameterImpl::class.java)
        registry.registerBeanDefinition("inSessionParameterImpl", inSessionBuilder.beanDefinition)

        val inBodyBuilder = BeanDefinitionBuilder.genericBeanDefinition(InBodyParameterImpl::class.java)
        registry.registerBeanDefinition("inBodyParameterImpl", inBodyBuilder.beanDefinition)

        val defaultBuilder = BeanDefinitionBuilder.genericBeanDefinition(BodyParameterImpl::class.java)
        registry.registerBeanDefinition("defaultParameterImpl", defaultBuilder.beanDefinition)

        val callbackBuilder = BeanDefinitionBuilder.genericBeanDefinition(CallbackParameterImpl::class.java)
        registry.registerBeanDefinition("callbackParameterImpl", callbackBuilder.beanDefinition)

        val sessionBuilder = BeanDefinitionBuilder.genericBeanDefinition(SessionParameterImpl::class.java)
        registry.registerBeanDefinition("sessionParameterImpl", sessionBuilder.beanDefinition)
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

}