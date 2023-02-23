package com.aimelodije.kontroleri.api

import com.aimelodije.iznimke.album.AlbumNijePronadenIznimka
import com.aimelodije.iznimke.melodija.MelodijaNijePronadenaIznimka
import com.aimelodije.iznimke.umjetnik.UmjetnikNijePronadenIznimka
import com.aimelodije.iznimke.zanr.ZanrNijePronadenIznimka
import com.aimelodije.kontroleri.KontrolerValidator
import com.aimelodije.kontroleri.sastavljaci.MelodijaSastavljacResursa
import com.aimelodije.modeli.pogledi.MelodijaDodatnoPogled
import com.aimelodije.modeli.resursi.MelodijaResurs
import com.aimelodije.modeli.zahtjevi.MelodijaAzuriranjeZahtjev
import com.aimelodije.modeli.zahtjevi.MelodijaDodavanjeZahtjev
import com.aimelodije.servisi.MelodijaServis
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
@RequestMapping(path = ["/api/v1/umjetnici/{umjetnikId}/albumi/{albumId}/melodije"])
class MelodijaKontroler(
    private val melodijaServis: MelodijaServis,
    private val melodijaSastavljacResursa: MelodijaSastavljacResursa,
    private val validator: KontrolerValidator
) {

    companion object : KLogging()

    @GetMapping(path = [""])
    fun dohvatiSve(
        @PathVariable umjetnikId: Long,
        @PathVariable albumId: Long
    ): ResponseEntity<CollectionModel<MelodijaResurs>> = try {
        ResponseEntity.ok(
            melodijaSastavljacResursa.toCollectionModel(
                melodijaServis.dohvatiSve(umjetnikId, albumId).toMutableList(),
                umjetnikId,
                albumId
            )
        )
    } catch (umjetnikIznimka: UmjetnikNijePronadenIznimka) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, umjetnikIznimka.message, umjetnikIznimka)
    } catch (albumIznimka: AlbumNijePronadenIznimka) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, albumIznimka.message, albumIznimka)
    } catch (melodijaIznimka: MelodijaNijePronadenaIznimka) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, melodijaIznimka.message, melodijaIznimka)
    }

    @GetMapping(path = ["/{melodijaId}"])
    fun dohvati(
        @PathVariable umjetnikId: Long,
        @PathVariable albumId: Long,
        @PathVariable melodijaId: Long,
    ): ResponseEntity<MelodijaResurs> = try {
        ResponseEntity.ok(
            melodijaSastavljacResursa.toModel(
                MelodijaDodatnoPogled(albumId, melodijaServis.dohvati(umjetnikId, albumId, melodijaId))
            )
        )
    } catch (umjetnikIznimka: UmjetnikNijePronadenIznimka) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, umjetnikIznimka.message, umjetnikIznimka)
    } catch (albumIznimka: AlbumNijePronadenIznimka) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, albumIznimka.message, albumIznimka)
    } catch (melodijaIznimka: MelodijaNijePronadenaIznimka) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, melodijaIznimka.message, melodijaIznimka)
    }

    @PostMapping(path = [""])
    fun dodaj(
        @PathVariable umjetnikId: Long,
        @PathVariable albumId: Long,
        @Valid @ModelAttribute melodijaZahtjev: MelodijaDodavanjeZahtjev?,
        rezultatPovezivanja: BindingResult
    ): ResponseEntity<MelodijaResurs> = try {
        if (melodijaZahtjev == null)
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Nedostaje tijelo zahtjeva"
            )

        validator.provjeriNeNullPogreske(
            rezultatPovezivanja,
            iznimke = listOf(MelodijaDodavanjeZahtjev::audio.name)
        )

        val melodija = melodijaServis.dodaj(
            melodijaZahtjev.copy(
                umjetnikId = umjetnikId,
                albumId = albumId
            )
        )

        ResponseEntity(
            melodijaSastavljacResursa.toModel(
                MelodijaDodatnoPogled(albumId, melodija)
            ),
            HttpStatus.CREATED
        )
    } catch (umjetnikIznimka: UmjetnikNijePronadenIznimka) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, umjetnikIznimka.message, umjetnikIznimka)
    } catch (albumIznimka: AlbumNijePronadenIznimka) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, albumIznimka.message, albumIznimka)
    } catch (zanrIznimka: ZanrNijePronadenIznimka) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, zanrIznimka.message, zanrIznimka)
    }

    @PutMapping(path = ["/{melodijaId}"])
    fun azuriraj(
        @PathVariable umjetnikId: Long,
        @PathVariable albumId: Long,
        @PathVariable melodijaId: Long,
        @Valid @RequestBody melodijaZahtjev: MelodijaAzuriranjeZahtjev,
        rezultatPovezivanja: BindingResult
    ): ResponseEntity<MelodijaResurs> = try {
        validator.provjeriPogreske(rezultatPovezivanja)

        val melodija = melodijaServis.azuriraj(
            melodijaZahtjev.copy(
                umjetnikId = umjetnikId,
                albumId = albumId,
                melodijaId = melodijaId
            )
        )

        ResponseEntity.ok(
            melodijaSastavljacResursa.toModel(
                MelodijaDodatnoPogled(albumId, melodija)
            )
        )
    } catch (umjetnikIznimka: UmjetnikNijePronadenIznimka) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, umjetnikIznimka.message, umjetnikIznimka)
    } catch (albumIznimka: AlbumNijePronadenIznimka) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, albumIznimka.message, albumIznimka)
    } catch (melodijaIznimka: MelodijaNijePronadenaIznimka) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, melodijaIznimka.message, melodijaIznimka)
    }

    @DeleteMapping(path = ["/{melodijaId}"])
    fun obrisi(
        @PathVariable umjetnikId: Long,
        @PathVariable albumId: Long,
        @PathVariable melodijaId: Long,
    ): ResponseEntity<Unit> = try {
        melodijaServis.obrisi(umjetnikId, albumId, melodijaId)
        ResponseEntity.ok().build()
    } catch (umjetnikIznimka: UmjetnikNijePronadenIznimka) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, umjetnikIznimka.message, umjetnikIznimka)
    } catch (albumIznimka: AlbumNijePronadenIznimka) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, albumIznimka.message, albumIznimka)
    } catch (melodijaIznimka: MelodijaNijePronadenaIznimka) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, melodijaIznimka.message, melodijaIznimka)
    }
}