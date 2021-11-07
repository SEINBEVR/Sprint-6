package ru.sber.springmvc.security.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import javax.sql.DataSource

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
class SecurityConfig(private val dataSource: DataSource): WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.jdbcAuthentication().dataSource(dataSource)
            .authoritiesByUsernameQuery("select username, password, enabled from users where username = ?")
            .authoritiesByUsernameQuery("select username, authority from authorities where username = ?")
    }

    override fun configure(http: HttpSecurity) {
        http
            .authorizeRequests().antMatchers("/api/**").hasAnyRole("ADMIN", "API")
            .and()
            .authorizeRequests().anyRequest().authenticated()
            .and()
            .formLogin()
            .and()
            .logout().logoutUrl("/logout").deleteCookies("JSESSIONID")
            .and()
            .csrf().disable()
    }

    @Bean
    fun getPasswordEncoder(): PasswordEncoder = NoOpPasswordEncoder.getInstance()
}