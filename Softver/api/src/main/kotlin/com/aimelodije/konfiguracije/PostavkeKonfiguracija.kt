package com.aimelodije.konfiguracije

import com.aimelodije.konfiguracije.postavke.JwtPostavke
import com.aimelodije.konfiguracije.postavke.SpotifyPostavke
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(value = [JwtPostavke::class, SpotifyPostavke::class])
class PostavkeKonfiguracija {

    @Bean
    fun jwtPostavke(jwtPostavke: JwtPostavke) = jwtPostavke

    @Bean
    fun spotifyPostavke(spotifyPostavke: SpotifyPostavke) = spotifyPostavke
}