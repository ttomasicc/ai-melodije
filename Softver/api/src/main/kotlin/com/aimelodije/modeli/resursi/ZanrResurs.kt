package com.aimelodije.modeli.resursi

import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation

@Relation(collectionRelation = "zanrovi")
data class ZanrResurs(
    val id: Long,
    val naziv: String
) : RepresentationModel<ZanrResurs>()