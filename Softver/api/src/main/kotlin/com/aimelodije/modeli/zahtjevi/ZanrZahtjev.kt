package com.aimelodije.modeli.zahtjevi

import com.aimelodije.modeli.domena.Zanr
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class ZanrZahtjev(
    val id: Long = 0,

    @field:NotBlank(message = "Naziv žanr ne može biti prazan")
    @field:Size(max = 50, message = "Maksimalna veličina naziva žanra je 50 karaktera")
    val naziv: String = ""
) {

    fun toZanr() = Zanr(
        id = id,
        naziv = naziv
    )
}