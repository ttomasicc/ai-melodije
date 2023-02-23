package com.aimelodije.modeli.zahtjevi

import com.aimelodije.modeli.zahtjevi.validatori.File
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.web.multipart.MultipartFile

data class AlbumAzuriranjeZahtjev(
    val albumId: Long = 0,

    @field:NotBlank(message = "Naziv ne može biti prazan")
    @field:Size(max = 100, message = "Maksimalna veličina naziva je 100 karaktera")
    val naziv: String? = null,

    @field:NotBlank(message = "Opis ne može biti prazan")
    val opis: String? = null,

    @field:File(
        message = "Nevažeća grafička datoteka - dozvoljene su jpg, jpeg i png datoteke do 1MB",
        allowedExtensions = ["jpg", "jpeg", "png"],
    )
    val slika: MultipartFile? = null
)