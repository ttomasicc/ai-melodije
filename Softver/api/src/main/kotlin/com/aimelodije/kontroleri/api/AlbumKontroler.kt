package com.aimelodije.kontroleri.api

import com.aimelodije.iznimke.album.AlbumNijePronadenIznimka
import com.aimelodije.iznimke.umjetnik.UmjetnikNijePronadenIznimka
import com.aimelodije.kontroleri.KontrolerValidator
import com.aimelodije.kontroleri.sastavljaci.AlbumSastavljacResursa
import com.aimelodije.modeli.resursi.AlbumResurs
import com.aimelodije.modeli.zahtjevi.AlbumAzuriranjeZahtjev
import com.aimelodije.modeli.zahtjevi.AlbumDodavanjeZahtjev
import com.aimelodije.servisi.AlbumServis
import jakarta.validation.Valid
import mu.KLogging
import org.springframework.hateoas.CollectionModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping(path = ["/api/v1/umjetnici/{umjetnikId}/albumi"])
class AlbumKontroler(
    private val albumServis: AlbumServis,
    private val albumSastavljacResursa: AlbumSastavljacResursa,
    private val validator: KontrolerValidator
) {

    companion object : KLogging()

    @GetMapping(path = [""])
    fun dohvatiSve(
        @PathVariable umjetnikId: Long
    ): ResponseEntity<CollectionModel<AlbumResurs>> = try {
        ResponseEntity.ok(
            albumSastavljacResursa.toCollectionModel(
                albumServis.dohvatiSve(umjetnikId).toMutableList(),
                umjetnikId
            )
        )
    } catch (umjetnikIznimka: UmjetnikNijePronadenIznimka) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, umjetnikIznimka.message, umjetnikIznimka)
    }

    @GetMapping(path = ["/{albumId}"])
    fun dohvati(
        @PathVariable umjetnikId: Long,
        @PathVariable albumId: Long
    ): ResponseEntity<AlbumResurs> = try {
        ResponseEntity.ok(
            albumSastavljacResursa.toModel(
                albumServis.dohvati(umjetnikId, albumId)
            )
        )
    } catch (umjetnikIznimka: UmjetnikNijePronadenIznimka) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, umjetnikIznimka.message, umjetnikIznimka)
    } catch (albumIznimka: AlbumNijePronadenIznimka) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, albumIznimka.message, albumIznimka)
    }

    @PostMapping(path = [""])
    fun dodaj(
        @PathVariable umjetnikId: Long,
        @Valid @RequestBody albumZahtjev: AlbumDodavanjeZahtjev?,
        rezultatPovezivanja: BindingResult
    ): ResponseEntity<AlbumResurs> = try {
        validator.provjeriNeNullPogreske(rezultatPovezivanja)

        ResponseEntity.ok(
            albumSastavljacResursa.toModel(
                albumServis.dodaj(
                    albumZahtjev?.copy(umjetnikId = umjetnikId) ?: AlbumDodavanjeZahtjev(umjetnikId = umjetnikId)
                )
            )
        )
    } catch (umjetnikIznimka: UmjetnikNijePronadenIznimka) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, umjetnikIznimka.message, umjetnikIznimka)
    }

    @PutMapping(path = ["/{albumId}"])
    fun azuriraj(
        @PathVariable albumId: Long,
        @Valid @ModelAttribute albumZahtjev: AlbumAzuriranjeZahtjev,
        rezultatPovezivanja: BindingResult
    ): ResponseEntity<AlbumResurs> = try {
        validator.provjeriNeNullPogreske(rezultatPovezivanja)

        ResponseEntity.ok(
            albumSastavljacResursa.toModel(
                albumServis.azuriraj(
                    albumZahtjev.copy(albumId = albumId)
                )
            )
        )
    } catch (umjetnikIznimka: UmjetnikNijePronadenIznimka) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, umjetnikIznimka.message, umjetnikIznimka)
    } catch (albumIznimka: AlbumNijePronadenIznimka) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, albumIznimka.message, albumIznimka)
    }

    @DeleteMapping(path = ["/{albumId}"])
    fun obrisi(
        @PathVariable albumId: Long,
    ): ResponseEntity<Unit> = try {
        albumServis.obrisi(albumId)
        ResponseEntity.ok().build()
    } catch (umjetnikIznimka: UmjetnikNijePronadenIznimka) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, umjetnikIznimka.message, umjetnikIznimka)
    } catch (albumIznimka: AlbumNijePronadenIznimka) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, albumIznimka.message, albumIznimka)
    }
}