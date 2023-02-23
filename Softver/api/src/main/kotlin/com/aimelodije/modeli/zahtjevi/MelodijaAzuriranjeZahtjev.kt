package com.aimelodije.modeli.zahtjevi

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class MelodijaAzuriranjeZahtjev(
    val umjetnikId: Long = 0,
    val albumId: Long = 0,
    val melodijaId: Long = 0,

    @field:NotBlank(message = "Naziv ne može biti prazan")
    @field:Size(max = 100, message = "Maksimalna veličina naziva je 100 karaktera")
    val naziv: String = ""
)