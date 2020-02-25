package me.jinuo.imf.websocket.client

import me.jinuo.imf.websocket.configure.RegisterConfiguration
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.type.AnnotationMetadata

/**
 * @author frank
 * @date 2019-11-14 17:21
 * @desc
 **/
@Import(RegisterConfiguration::class)
@Configuration
class ClientConfigure : ImportBeanDefinitionRegistrar {

    override fun registerBeanDefinitions(annoMeta: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        val clientFactoryBuilder = BeanDefinitionBuilder.genericBeanDefinition(ClientFactory::class.java)
        registry.registerBeanDefinition("clientFactory", clientFactoryBuilder.beanDefinition)

        val websocketClientHandlerBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(WebSocketClientHandler::class.java)
        registry.registerBeanDefinition("websocketClientHandler", websocketClientHandlerBuilder.beanDefinition)
    }

}