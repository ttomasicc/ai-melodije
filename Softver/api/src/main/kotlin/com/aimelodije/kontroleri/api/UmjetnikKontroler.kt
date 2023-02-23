package com.aimelodije.kontroleri.api

import com.aimelodije.iznimke.umjetnik.IlegalanUmjetnikIznimka
import com.aimelodije.iznimke.umjetnik.UmjetnikNijePronadenIznimka
import com.aimelodije.kontroleri.KontrolerValidator
import com.aimelodije.kontroleri.sastavljaci.UmjetnikSastavljacResursa
import com.aimelodije.modeli.resursi.UmjetnikResurs
import com.aimelodije.modeli.zahtjevi.UmjetnikAzuriranjeZahtjev
import com.aimelodije.servisi.UmjetnikServis
import jakarta.validation.Valid
import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping(path = ["/api/v1/umjetnici"])
class UmjetnikKontroler(
    private val umjetnikServis: UmjetnikServis,
    private val umjetnikSastavljacResursa: UmjetnikSastavljacResursa,
    private val validator: KontrolerValidator
) {

    companion object : KLogging()

    @GetMapping(path = ["/{id}"])
    fun dohvati(
        @PathVariable id: Long
    ): ResponseEntity<UmjetnikResurs> = try {
        ResponseEntity.ok(
            umjetnikSastavljacResursa.toModel(
                umjetnikServis.dohvati(id)
            )
        )
    } catch (iznimka: UmjetnikNijePronadenIznimka) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, iznimka.message, iznimka)
    }

    @PutMapping(path = ["/{id}"])
    fun azuriraj(
        @PathVariable id: Long,
        @Valid @ModelAttribute azuriranjeZahtjev: UmjetnikAzuriranjeZahtjev,
        rezultatPovezivanja: BindingResult
    ): ResponseEntity<UmjetnikResurs> = try {
        validator.provjeriNeNullPogreske(rezultatPovezivanja)

        ResponseEntity.ok(
            umjetnikSastavljacResursa.toModel(
                umjetnikServis.azuriraj(azuriranjeZahtjev.copy(id = id))
            )
        )
    } catch (ilegalanUmjetnikIznimka: IlegalanUmjetnikIznimka) {
        throw ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            ilegalanUmjetnikIznimka.message,
            ilegalanUmjetnikIznimka
        )
    }
}