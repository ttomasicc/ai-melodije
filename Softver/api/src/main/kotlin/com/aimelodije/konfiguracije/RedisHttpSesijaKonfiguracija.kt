package com.aimelodije.konfiguracije

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration
import java.time.Duration

/**
 * Ručna Redis "auto"-konfiguracija potrebna je zbog Spring Boot 3.x verzije
 *
 * Više na: <a href="https://elvisciotti.medium.com/migrate-spring-boot-data-redis-session-to-v3-0-3be482a3cfef">linku</a>
 */
@Configuration
class RedisHttpSesijaKonfiguracija(
    @Value("\${spring.session.timeout}") val maksimalnoTrajanje: Duration,
) : RedisHttpSessionConfiguration() {

    override fun getMaxInactiveInterval(): Duration = maksimalnoTrajanje
}