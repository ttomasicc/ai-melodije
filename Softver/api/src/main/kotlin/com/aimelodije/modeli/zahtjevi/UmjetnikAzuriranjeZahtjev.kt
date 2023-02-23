package com.aimelodije.modeli.zahtjevi

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.web.multipart.MultipartFile

data class UmjetnikAzuriranjeZahtjev(
    val id: Long = 0,

    @field:NotBlank(message = "Email ne može biti prazan")
    @field:Size(max = 70, message = "Maksimalna veličina email adrese je 70 karaktera")
    @field:Email(message = "Nevažeća email adresa")
    val email: String? = null,

    @field:NotBlank(message = "Lozinka ne može biti prazna")
    val lozinka: String? = null,

    @field:NotBlank(message = "Ime ne može biti prazno")
    @field:Size(max = 70, message = "Maksimalna veličina imena je 70 karaktera")
    val ime: String? = null,

    @field:NotBlank(message = "Prezime ne može biti prazno")
    @field:Size(max = 70, message = "Maksimalna veličina prezimena je 70 karaktera")
    val prezime: String? = null,

    @field:NotBlank(message = "Opis ne može biti prazan")
    val opis: String? = null,

    val slika: MultipartFile? = null
)