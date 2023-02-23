package com.aimelodije.kontroleri.filteri

import com.aimelodije.servisi.JwtServis
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val jwtServis: JwtServis,
    private val detaljiKorisnikaServis: UserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        zahtjev: HttpServletRequest,
        odgovor: HttpServletResponse,
        filterLanac: FilterChain
    ) {
        zahtjev.getHeader("Authorization")?.let { authZagljavlje ->
            if (authZagljavlje.startsWith("Bearer ").not()) {
                filterLanac.doFilter(zahtjev, odgovor)
                return
            }

            val jwt = authZagljavlje.substring(7)
            postaviSigurnosniKontekstKorisnika(zahtjev, jwt)
        }
        filterLanac.doFilter(zahtjev, odgovor)
    }

    fun postaviSigurnosniKontekstKorisnika(zahtjev: HttpServletRequest, jwt: String) {
        if (SecurityContextHolder.getContext().authentication == null && jwtServis.jeValidan(jwt)) {
            val korime = jwtServis.dohvatiKorime(jwt)
            val detaljiKorisnika = detaljiKorisnikaServis.loadUserByUsername(korime)

            SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
                detaljiKorisnika.username,
                detaljiKorisnika.password,
                detaljiKorisnika.authorities
            ).apply {
                details = WebAuthenticationDetailsSource().buildDetails(zahtjev)
            }
        }
    }
}