package ru.sber.springmvc.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Bean

@TestConfiguration
class TestConfig {
    @Bean
    fun restTemplate(): TestRestTemplate = TestRestTemplate()
}