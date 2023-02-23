package com.aimelodije.kontroleri.sastavljaci

import com.aimelodije.kontroleri.api.ZanrKontroler
import com.aimelodije.modeli.pogledi.ZanrPogled
import com.aimelodije.modeli.resursi.ZanrResurs
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.stereotype.Component

@Component
class ZanrSastavljacResursa : RepresentationModelAssemblerSupport<ZanrPogled, ZanrResurs>(
    ZanrKontroler::class.java, ZanrResurs::class.java
) {

    override fun toModel(entitet: ZanrPogled): ZanrResurs =
        createModelWithId(entitet.id, entitet)

    override fun toCollectionModel(entiteti: MutableIterable<ZanrPogled>): CollectionModel<ZanrResurs> =
        CollectionModel.of(
            entiteti.map { toModel(it) }
        ).add(
            linkTo<ZanrKontroler> {
                dohvatiSve()
            }.withSelfRel()
        )

    override fun instantiateModel(entitet: ZanrPogled): ZanrResurs =
        ZanrResurs(
            id = entitet.id,
            naziv = entitet.naziv
        )
}