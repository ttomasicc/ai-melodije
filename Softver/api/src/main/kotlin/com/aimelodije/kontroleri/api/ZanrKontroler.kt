package com.aimelodije.kontroleri.api

import com.aimelodije.iznimke.zanr.IlegalanZanrIznimka
import com.aimelodije.iznimke.zanr.PostojeciZanrIznimka
import com.aimelodije.iznimke.zanr.ZanrNijePronadenIznimka
import com.aimelodije.kontroleri.KontrolerValidator
import com.aimelodije.kontroleri.sastavljaci.ZanrSastavljacResursa
import com.aimelodije.modeli.resursi.ZanrResurs
import com.aimelodije.modeli.zahtjevi.ZanrZahtjev
import com.aimelodije.servisi.ZanrServis
import jakarta.validation.Valid
import org.springframework.hateoas.CollectionModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping(path = ["/api/v1/zanrovi"])
class ZanrKontroler(
    private val zanrServis: ZanrServis,
    private val zanrSastavljacResursa: ZanrSastavljacResursa,
    private val validator: KontrolerValidator
) {

    @GetMapping(path = [""])
    fun dohvatiSve(): ResponseEntity<CollectionModel<ZanrResurs>> =
        ResponseEntity.ok(
            zanrSastavljacResursa.toCollectionModel(
                zanrServis.dohvatiSve().toMutableList()
            )
        )

    @PostMapping(path = [""])
    fun dodaj(
        @Valid @RequestBody zanrZahtjev: ZanrZahtjev,
        rezultatPovezivanja: BindingResult
    ): ResponseEntity<ZanrResurs> = try {
        validator.provjeriPogreske(rezultatPovezivanja)

        ResponseEntity.ok(
            zanrSastavljacResursa.toModel(
                zanrServis.dodaj(zanrZahtjev)
            )
        )
    } catch (postojeciZanrIznimka: PostojeciZanrIznimka) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, postojeciZanrIznimka.message, postojeciZanrIznimka)
    }

    @PutMapping(path = ["/{id}"])
    fun azuriraj(
        @PathVariable id: Long,
        @Valid @RequestBody zanrZahtjev: ZanrZahtjev,
        rezultatPovezivanja: BindingResult
    ): ResponseEntity<ZanrResurs> = try {
        validator.provjeriPogreske(rezultatPovezivanja)

        ResponseEntity.ok(
            zanrSastavljacResursa.toModel(
                zanrServis.azuriraj(zanrZahtjev.copy(id = id))
            )
        )
    } catch (ilegalanZanrIznimka: IlegalanZanrIznimka) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, ilegalanZanrIznimka.message, ilegalanZanrIznimka)
    }

    @DeleteMapping(path = ["/{id}"])
    fun obrisi(
        @PathVariable id: Long
    ): ResponseEntity<Unit> = try {
        if (zanrServis.obrisi(id))
            ResponseEntity.ok().build()
        else
            throw ResponseStatusException(HttpStatus.CONFLICT, "Nemoguće obrisati žanr")
    } catch (zanrNijePronadenIznimka: ZanrNijePronadenIznimka) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, zanrNijePronadenIznimka.message, zanrNijePronadenIznimka)
    }

    @DeleteMapping(path = [""])
    fun obrisiNekoristene(): ResponseEntity<Unit> {
        zanrServis.obrisiNekoristene()
        return ResponseEntity.ok().build()
    }
}