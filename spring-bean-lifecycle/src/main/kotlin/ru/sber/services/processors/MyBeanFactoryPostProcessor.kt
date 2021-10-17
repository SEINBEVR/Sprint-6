package ru.sber.services.processors

import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.stereotype.Component
import ru.sber.services.BeanFactoryPostProcessorBean
import javax.annotation.PostConstruct

@Component
class MyBeanFactoryPostProcessor : BeanFactoryPostProcessor {
    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {
        val beanDefinition = beanFactory.getBeanDefinition("beanFactoryPostProcessorBean")
        val methods = BeanFactoryPostProcessorBean::class.java.methods

        for(method in methods) {
            if(AnnotationUtils.findAnnotation(method, PostConstruct::class.java) != null) {
                beanDefinition.initMethodName = method.name
                break
            }
        }
    }
}