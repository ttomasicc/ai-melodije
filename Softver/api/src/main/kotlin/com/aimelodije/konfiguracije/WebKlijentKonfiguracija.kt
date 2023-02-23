package com.aimelodije.konfiguracije

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebKlijentKonfiguracija {

    @Bean
    fun webKlijent(webKlijentGraditelj: WebClient.Builder): WebClient =
        webKlijentGraditelj
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .build()
}