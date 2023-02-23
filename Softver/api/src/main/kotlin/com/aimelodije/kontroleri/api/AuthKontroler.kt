package com.aimelodije.kontroleri.api

import com.aimelodije.iznimke.umjetnik.IlegalanUmjetnikIznimka
import com.aimelodije.konfiguracije.postavke.JwtPostavke
import com.aimelodije.kontroleri.KontrolerValidator
import com.aimelodije.kontroleri.sastavljaci.AuthSastavljacResursa
import com.aimelodije.kontroleri.sastavljaci.JwtSastavljacResursa
import com.aimelodije.modeli.enumeracije.UmjetnikAkcija
import com.aimelodije.modeli.pogledi.JwtPogled
import com.aimelodije.modeli.pogledi.UmjetnikDodatnoPogled
import com.aimelodije.modeli.pogledi.UmjetnikPogled
import com.aimelodije.modeli.resursi.JwtResurs
import com.aimelodije.modeli.resursi.UmjetnikResurs
import com.aimelodije.modeli.zahtjevi.UmjetnikPrijavaZahtjev
import com.aimelodije.modeli.zahtjevi.UmjetnikRegistracijaZahtjev
import com.aimelodije.servisi.AuthServis
import com.aimelodije.servisi.JwtServis
import com.auth0.jwt.exceptions.TokenExpiredException
import jakarta.servlet.http.HttpSession
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping(path = ["/api/v1/auth"])
class AuthKontroler(
    private val authServis: AuthServis,
    private val jwtServis: JwtServis,
    private val jwtPostavke: JwtPostavke,
    private val authSastavljacResursa: AuthSastavljacResursa,
    private val jwtSastavljacResursa: JwtSastavljacResursa,
    private val validator: KontrolerValidator
) {

    @GetMapping(path = ["/token"])
    fun dohvatiToken(
        sesija: HttpSession
    ): ResponseEntity<JwtResurs> {
        val sesijaJwt = sesija.getAttribute(jwtPostavke.sesija) as String?

        sesijaJwt?.let {
            if (jwtServis.jeValidan(it, dozvoljeneIznimke = arrayOf(TokenExpiredException::class.java))) {
                val umjetnik = authServis.dohvatiUmjetnika(jwtServis.dohvatiKorime(it))!!

                val jwt = jwtServis.generiraj(umjetnik)
                sesija.setAttribute(jwtPostavke.sesija, jwt)

                return ResponseEntity.ok(
                    jwtSastavljacResursa.toModel(JwtPogled(jwt))
                )
            }
        }

        return ResponseEntity.ok(
            jwtSastavljacResursa.toModel(JwtPogled(null))
        )
    }

    @PostMapping(path = ["/prijava"])
    fun prijaviUmjetnika(
        @Valid @RequestBody prijavaZahtjev: UmjetnikPrijavaZahtjev,
        rezultatPovezivanja: BindingResult,
        sesija: HttpSession
    ): ResponseEntity<UmjetnikResurs> = try {
        validator.provjeriPogreske(rezultatPovezivanja)

        val umjetnik = authServis.prijavi(prijavaZahtjev)

        val jwt = jwtServis.generiraj(umjetnik)
        sesija.setAttribute(jwtPostavke.sesija, jwt)

        ResponseEntity.ok(
            authSastavljacResursa.toModel(
                UmjetnikDodatnoPogled(
                    akcija = UmjetnikAkcija.PRIJAVA,
                    umjetnik = UmjetnikPogled(umjetnik)
                )
            )
        )
    } catch (iznimka: IlegalanUmjetnikIznimka) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, iznimka.message, iznimka)
    }

    @PostMapping(path = ["/registracija"])
    fun registrirajUmjetnika(
        @Valid @RequestBody registracijaZahtjev: UmjetnikRegistracijaZahtjev,
        rezultatPovezivanja: BindingResult,
        sesija: HttpSession
    ): ResponseEntity<UmjetnikResurs> = try {
        validator.provjeriNeNullPogreske(rezultatPovezivanja)

        val umjetnik = authSastavljacResursa.toModel(
            UmjetnikDodatnoPogled(
                akcija = UmjetnikAkcija.REGISTRACIJA,
                umjetnik = authServis.registriraj(registracijaZahtjev)
            )
        )

        sesija.invalidate()

        ResponseEntity(umjetnik, HttpStatus.CREATED)
    } catch (iznimka: IlegalanUmjetnikIznimka) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, iznimka.message, iznimka)
    }

    @DeleteMapping(path = ["/odjava"])
    fun odjaviUmjetnika(
        sesija: HttpSession
    ): ResponseEntity<Unit> = try {
        sesija.invalidate()
        ResponseEntity.noContent().build()
    } catch (iznimka: IllegalStateException) {
        ResponseEntity.noContent().build()
    }
}