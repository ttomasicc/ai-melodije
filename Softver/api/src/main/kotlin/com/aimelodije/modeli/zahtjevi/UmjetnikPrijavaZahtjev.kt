package com.aimelodije.modeli.zahtjevi

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UmjetnikPrijavaZahtjev(
    @field:NotBlank(message = "Korisničko ime ne može biti prazno")
    @field:Size(max = 50, message = "Maksimalna veličina korisničkog imena je 50 karaktera")
    val korime: String = "",

    @field:NotBlank(message = "Lozinka ne može biti prazna")
    val lozinka: String = ""
)