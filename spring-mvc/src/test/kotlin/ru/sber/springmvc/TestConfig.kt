package ru.sber.springmvc

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableAutoConfiguration
class TestConfig {

    @Bean
    fun testRestTemplate(): TestRestTemplate = TestRestTemplate()
}