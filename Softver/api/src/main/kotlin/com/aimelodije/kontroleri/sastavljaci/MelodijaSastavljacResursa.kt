package com.aimelodije.kontroleri.sastavljaci

import com.aimelodije.kontroleri.api.MelodijaKontroler
import com.aimelodije.modeli.pogledi.MelodijaDodatnoPogled
import com.aimelodije.modeli.pogledi.MelodijaPogled
import com.aimelodije.modeli.resursi.MelodijaResurs
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.stereotype.Component

@Component
class MelodijaSastavljacResursa(
    private val zanrSastavljacResursa: ZanrSastavljacResursa
) : RepresentationModelAssemblerSupport<MelodijaDodatnoPogled, MelodijaResurs>(
    MelodijaKontroler::class.java, MelodijaResurs::class.java
) {

    override fun toModel(entitet: MelodijaDodatnoPogled): MelodijaResurs =
        instantiateModel(entitet).apply {
            add(
                linkTo<MelodijaKontroler> {
                    dohvati(entitet.melodija.autor.id, entitet.albumId, entitet.melodija.id)
                }.withSelfRel()
            )
        }

    fun toCollectionModel(
        entiteti: MutableIterable<MelodijaPogled>,
        umjetnikId: Long,
        albumId: Long
    ): CollectionModel<MelodijaResurs> =
        CollectionModel.of(
            entiteti.map { toModel(MelodijaDodatnoPogled(albumId, it)) }
        ).add(
            linkTo<MelodijaKontroler> {
                dohvatiSve(umjetnikId, albumId)
            }.withSelfRel()
        )

    override fun instantiateModel(entitet: MelodijaDodatnoPogled): MelodijaResurs =
        MelodijaResurs(
            id = entitet.melodija.id,
            audio = entitet.melodija.audio,
            naziv = entitet.melodija.naziv,
            datumDodano = entitet.melodija.datumDodano,
            zanr = zanrSastavljacResursa.toModel(entitet.melodija.zanr)
        )
}