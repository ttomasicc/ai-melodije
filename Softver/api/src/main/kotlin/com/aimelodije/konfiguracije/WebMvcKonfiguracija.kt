package com.aimelodije.konfiguracije

import com.aimelodije.modeli.enumeracije.konverteri.ResursKonverter
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcKonfiguracija : WebMvcConfigurer {

    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(ResursKonverter())
    }
}