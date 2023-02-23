package com.aimelodije.modeli.zahtjevi

import com.aimelodije.modeli.zahtjevi.validatori.File
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.springframework.web.multipart.MultipartFile

data class MelodijaDodavanjeZahtjev(
    val umjetnikId: Long = 0,
    val albumId: Long = 0,

    @field:Min(value = 1, message = "Žanr mora biti prirodan broj")
    val zanrId: Long = 0,

    @field:NotNull(message = "Nedostaje audio datoteka")
    @field:File(
        message = "Nevažeća audio datoteka - dozvoljene su mp3, ogg i wav datoteke do 3MB",
        allowedExtensions = ["mp3", "ogg", "wav"],
        maximumBytes = 3 * 1024 * 1024
    )
    val audio: MultipartFile? = null,

    @field:NotBlank(message = "Naziv ne može biti prazan")
    @field:Size(max = 100, message = "Maksimalna veličina naziva je 100 karaktera")
    val naziv: String? = null,
)