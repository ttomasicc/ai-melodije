package com.aimelodije.modeli.resursi

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import java.util.Date

@Relation(collectionRelation = "albumi")
data class AlbumResurs(
    val id: Long,
    val naziv: String,
    val opis: String?,
    val slika: String?,
    @JsonFormat(pattern = "yyyy-MM-dd")
    val datumDodano: Date
) : RepresentationModel<AlbumResurs>()