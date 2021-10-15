package ru.sber.services.processors

import org.springframework.beans.BeansException
import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.DefaultListableBeanFactory
import org.springframework.beans.factory.support.GenericBeanDefinition
import org.springframework.stereotype.Component
import ru.sber.services.BeanFactoryPostProcessorBean


@Component
class MyBeanFactoryPostProcessor : BeanFactoryPostProcessor {
    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {

        val bd = GenericBeanDefinition()
        bd.setBeanClass(BeanFactoryPostProcessorBean::class.java)

        bd.initMethodName = "postConstruct"

        (beanFactory as DefaultListableBeanFactory).registerBeanDefinition("beanFactoryPostProcessorBean", bd)
    }
}

