package com.aimelodije.modeli.zahtjevi

import com.aimelodije.modeli.domena.Album
import com.aimelodije.modeli.domena.Umjetnik
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class AlbumDodavanjeZahtjev(
    val umjetnikId: Long = 0,

    @field:NotBlank(message = "Naziv ne može biti prazan")
    @field:Size(max = 70, message = "Maksimalna veličina naziva je 100 karaktera")
    val naziv: String? = null,

    @field:NotBlank(message = "Opis ne može biti prazan")
    val opis: String? = null
) {

    fun toAlbum(
        dohvatiUmjetnika: (id: Long) -> Umjetnik
    ) = Album(
        opis = opis,
        umjetnik = dohvatiUmjetnika(umjetnikId),
    ).also { album ->
        naziv?.let {
            album.naziv = it
        }
    }
}