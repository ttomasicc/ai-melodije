package com.aimelodije.servisi

import com.aimelodije.iznimke.umjetnik.IlegalanUmjetnik
import com.aimelodije.iznimke.umjetnik.IlegalanUmjetnikIznimka
import com.aimelodije.modeli.domena.Umjetnik
import com.aimelodije.modeli.enumeracije.Rola
import com.aimelodije.modeli.pogledi.UmjetnikPogled
import com.aimelodije.modeli.zahtjevi.UmjetnikPrijavaZahtjev
import com.aimelodije.modeli.zahtjevi.UmjetnikRegistracijaZahtjev
import com.aimelodije.repozitoriji.UmjetnikRepozitorij
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthServis(
    private val umjetnikRepozitorij: UmjetnikRepozitorij,
    private val authMenadzer: AuthenticationManager,
    private val enkoderLozinke: PasswordEncoder
) {

    @Transactional
    @Throws(IlegalanUmjetnikIznimka::class)
    fun registriraj(umjetnikRegistracijaZahtjev: UmjetnikRegistracijaZahtjev): UmjetnikPogled {
        if (umjetnikRepozitorij.existsByKorimeIgnoreCase(umjetnikRegistracijaZahtjev.korime)) {
            throw IlegalanUmjetnikIznimka(IlegalanUmjetnik.POSTOJECE_KORISNICKO_IME.poruka)
        } else if (umjetnikRepozitorij.existsByEmailIgnoreCase(umjetnikRegistracijaZahtjev.email)) {
            throw IlegalanUmjetnikIznimka(IlegalanUmjetnik.POSTOJECI_EMAIL.poruka)
        }

        val umjetnik = umjetnikRepozitorij.save(
            umjetnikRegistracijaZahtjev.toUmjetnik(
                { Rola.UMJETNIK },
                { lozinka -> enkoderLozinke.encode(lozinka) }
            )
        )

        return UmjetnikPogled(umjetnik)
    }

    @Transactional(readOnly = true)
    @Throws(AuthenticationException::class)
    fun prijavi(umjetnikPrijavaZahtjev: UmjetnikPrijavaZahtjev): Umjetnik {
        authMenadzer.authenticate(
            UsernamePasswordAuthenticationToken(
                umjetnikPrijavaZahtjev.korime,
                umjetnikPrijavaZahtjev.lozinka
            )
        )
        return dohvatiUmjetnika(umjetnikPrijavaZahtjev.korime)!!
    }

    fun dohvatiUmjetnika(korime: String): Umjetnik? =
        umjetnikRepozitorij.findByKorimeIgnoreCase(korime)
}