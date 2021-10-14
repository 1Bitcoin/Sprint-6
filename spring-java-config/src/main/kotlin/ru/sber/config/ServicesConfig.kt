package ru.sber.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan("ru.sber.services")
class ServicesConfig

@Configuration
@ComponentScan("ru.sber.services")
class AnotherServicesConfig
