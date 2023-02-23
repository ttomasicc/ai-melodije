package com.aimelodije.konfiguracije

import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory

@Configuration
@EnableCaching
class RedisPredmemoriranjeKonfiguracija(
    @Value("\${spring.data.redis.host}")
    private val host: String,
    @Value("\${spring.data.redis.port}")
    private val port: Int
) {

    @Bean
    fun redisTvornicaKonekcija(): LettuceConnectionFactory =
        LettuceConnectionFactory(
            RedisStandaloneConfiguration(host, port)
        )

    @Bean
    fun menadzerPredmemoriranja(): CacheManager =
        RedisCacheManagerBuilder
            .fromConnectionFactory(redisTvornicaKonekcija())
            .build()
}