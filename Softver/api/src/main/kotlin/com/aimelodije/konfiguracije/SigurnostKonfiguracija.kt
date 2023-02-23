package com.aimelodije.konfiguracije

import com.aimelodije.repozitoriji.UmjetnikRepozitorij
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class SigurnostKonfiguracija(
    private val umjetnikRepozitorij: UmjetnikRepozitorij
) {

    @Bean
    fun authDavatelj(): AuthenticationProvider = DaoAuthenticationProvider().apply {
        setUserDetailsService(detaljiKorisnikaServis())
        setPasswordEncoder(enkoderLozinke())
    }

    @Bean
    fun detaljiKorisnikaServis(): UserDetailsService = UserDetailsService { korime ->
        umjetnikRepozitorij.findByKorimeIgnoreCase(korime) ?: throw UsernameNotFoundException("Korisnik nije pronađen")
    }

    @Bean
    fun enkoderLozinke(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    @Throws(Exception::class)
    fun authMenadzer(config: AuthenticationConfiguration): AuthenticationManager = config.authenticationManager
}