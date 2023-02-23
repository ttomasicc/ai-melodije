package com.aimelodije.kontroleri.sastavljaci

import com.aimelodije.kontroleri.api.AlbumKontroler
import com.aimelodije.kontroleri.api.MelodijaKontroler
import com.aimelodije.modeli.pogledi.AlbumPogled
import com.aimelodije.modeli.resursi.AlbumResurs
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.stereotype.Component

@Component
class AlbumSastavljacResursa : RepresentationModelAssemblerSupport<AlbumPogled, AlbumResurs>(
    AlbumKontroler::class.java, AlbumResurs::class.java
) {

    override fun toModel(entitet: AlbumPogled): AlbumResurs =
        instantiateModel(entitet).apply {
            add(
                linkTo<AlbumKontroler> {
                    dohvati(entitet.umjetnik.id, entitet.id)
                }.withSelfRel(),
                linkTo<MelodijaKontroler> {
                    dohvatiSve(entitet.umjetnik.id, entitet.id)
                }.withRel(Linkovi.MELODIJE.toString())
            )
        }

    fun toCollectionModel(entiteti: MutableIterable<AlbumPogled>, umjetnikId: Long): CollectionModel<AlbumResurs> =
        CollectionModel.of(
            entiteti.map { toModel(it) }
        ).add(
            linkTo<AlbumKontroler> {
                dohvatiSve(umjetnikId)
            }.withSelfRel()
        )

    override fun instantiateModel(entitet: AlbumPogled): AlbumResurs =
        AlbumResurs(
            id = entitet.id,
            naziv = entitet.naziv,
            opis = entitet.opis,
            slika = entitet.slika,
            datumDodano = entitet.datumDodano
        )
}