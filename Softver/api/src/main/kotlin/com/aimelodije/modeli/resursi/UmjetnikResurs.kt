package com.aimelodije.modeli.resursi

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.hateoas.RepresentationModel
import java.util.*

data class UmjetnikResurs(
    val id: Long,
    val korime: String,
    val email: String,
    val ime: String?,
    val prezime: String?,
    val opis: String?,
    val slika: String?,
    @JsonFormat(pattern = "yyyy-MM-dd")
    val datumRegistracije: Date,
    val rola: String
) : RepresentationModel<UmjetnikResurs>()