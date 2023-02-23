package com.aimelodije.modeli.resursi

import org.springframework.hateoas.RepresentationModel

data class JwtResurs(
    val token: String?
) : RepresentationModel<JwtResurs>()