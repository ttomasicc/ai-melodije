package com.aimelodije.kontroleri.api

import com.aimelodije.kontroleri.sastavljaci.AlbumSastavljacResursa
import com.aimelodije.kontroleri.sastavljaci.MelodijaSastavljacResursa
import com.aimelodije.modeli.enumeracije.TipResursa
import com.aimelodije.modeli.pogledi.AlbumPogled
import com.aimelodije.modeli.pogledi.MelodijaDodatnoPogled
import com.aimelodije.servisi.AlbumServis
import com.aimelodije.servisi.MelodijaServis
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.RepresentationModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import kotlin.math.min

@RestController
@RequestMapping(path = ["/api/v1/info"])
class InfoKontroler(
    private val albumServis: AlbumServis,
    private val melodijaServis: MelodijaServis,

    private val albumSastavljacResursa: AlbumSastavljacResursa,
    private val melodijaSastavljacResursa: MelodijaSastavljacResursa,
) {

    @GetMapping(path = ["/novo"])
    fun novo(
        @RequestParam(name = "tipResursa", defaultValue = TipResursa.Nazivi.ALBUM) tipResursa: TipResursa?,
        @RequestParam(name = "page", defaultValue = "0") stranica: Int,
        @RequestParam(name = "size", defaultValue = "10") velicina: Int
    ): ResponseEntity<PagedModel<out RepresentationModel<*>>> {
        if (tipResursa == null) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Nevažeći resurs. Dostupni su resursi ${enumValues<TipResursa>().joinToString(separator = ", ")}"
            )
        }

        val stranicenje = PageRequest.of(
            stranica,
            min(velicina, 20),
            Sort.by("datumDodano").descending()
        )
        val bazicniURI = ServletUriComponentsBuilder.fromCurrentRequest()
            .replaceQueryParam("resource", tipResursa.name)
            .build()

        return ResponseEntity.ok(
            when (tipResursa) {
                TipResursa.ALBUM -> PagedResourcesAssembler<AlbumPogled>(null, bazicniURI)
                    .toModel(albumServis.dohvatiSve(stranicenje), albumSastavljacResursa)

                TipResursa.MELODIJA -> PagedResourcesAssembler<MelodijaDodatnoPogled>(null, bazicniURI)
                    .toModel(
                        melodijaServis.dohvatiSve(stranicenje).map {
                            MelodijaDodatnoPogled(it.albumi.first().id, it)
                        },
                        melodijaSastavljacResursa
                    )
            }
        )
    }

    @GetMapping(path = ["/pretrazivanje"])
    fun pretrazuj(
        @RequestParam(name = "tipResursa", defaultValue = TipResursa.Nazivi.ALBUM) tipResursa: TipResursa?,
        @RequestParam(name = "title", defaultValue = "") dioNaziva: String,
        @RequestParam(name = "page", defaultValue = "0") stranica: Int,
        @RequestParam(name = "size", defaultValue = "10") velicina: Int
    ): ResponseEntity<PagedModel<out RepresentationModel<*>>> {
        if (tipResursa == null) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Nevažeći resurs. Dostupni su resursi ${enumValues<TipResursa>().joinToString(separator = ", ")}"
            )
        }

        val stranicenje = PageRequest.of(
            stranica,
            min(velicina, 20),
            Sort.by("datumDodano").descending()
        )
        val bazicniURI = ServletUriComponentsBuilder.fromCurrentRequest()
            .replaceQueryParam("resource", tipResursa.name)
            .replaceQueryParam("title", dioNaziva)
            .build()

        return ResponseEntity.ok(
            when (tipResursa) {
                TipResursa.ALBUM -> PagedResourcesAssembler<AlbumPogled>(null, bazicniURI)
                    .toModel(albumServis.pretrazuj(dioNaziva, stranicenje), albumSastavljacResursa)

                TipResursa.MELODIJA -> PagedResourcesAssembler<MelodijaDodatnoPogled>(null, bazicniURI)
                    .toModel(
                        melodijaServis.pretrazuj(dioNaziva, stranicenje).map {
                            MelodijaDodatnoPogled(it.albumi.first().id, it)
                        },
                        melodijaSastavljacResursa
                    )
            }
        )
    }
}