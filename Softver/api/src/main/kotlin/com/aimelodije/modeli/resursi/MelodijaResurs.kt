package com.aimelodije.modeli.resursi

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import java.util.Date

@Relation(collectionRelation = "melodije")
data class MelodijaResurs(
    val id: Long,
    val audio: String,
    val naziv: String,
    @JsonFormat(pattern = "yyyy-MM-dd")
    val datumDodano: Date,
    val zanr: ZanrResurs,
) : RepresentationModel<MelodijaResurs>()