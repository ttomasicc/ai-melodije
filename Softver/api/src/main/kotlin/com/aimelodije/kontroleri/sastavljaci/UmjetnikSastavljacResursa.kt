package com.aimelodije.kontroleri.sastavljaci

import com.aimelodije.kontroleri.api.AlbumKontroler
import com.aimelodije.kontroleri.api.UmjetnikKontroler
import com.aimelodije.modeli.pogledi.UmjetnikPogled
import com.aimelodije.modeli.resursi.UmjetnikResurs
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.stereotype.Component

@Component
class UmjetnikSastavljacResursa : RepresentationModelAssemblerSupport<UmjetnikPogled, UmjetnikResurs>(
    UmjetnikKontroler::class.java, UmjetnikResurs::class.java
) {

    override fun toModel(entitet: UmjetnikPogled): UmjetnikResurs =
        createModelWithId(entitet.id, entitet).apply {
            add(
                linkTo<AlbumKontroler> {
                    dohvatiSve(entitet.id)
                }.withRel(Linkovi.ALBUMI.toString())
            )
        }

    override fun instantiateModel(entitet: UmjetnikPogled): UmjetnikResurs =
        UmjetnikResurs(
            id = entitet.id,
            korime = entitet.korime,
            email = entitet.email,
            ime = entitet.ime,
            prezime = entitet.prezime,
            opis = entitet.opis,
            slika = entitet.slika,
            datumRegistracije = entitet.datumRegistracije,
            rola = entitet.rola
        )
}