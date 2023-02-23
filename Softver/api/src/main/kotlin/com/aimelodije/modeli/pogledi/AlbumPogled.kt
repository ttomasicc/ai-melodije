package com.aimelodije.modeli.pogledi

import com.aimelodije.modeli.domena.Album
import java.util.Date

data class AlbumPogled(
    val id: Long,
    val naziv: String,
    val opis: String?,
    val slika: String?,
    val datumDodano: Date,
    val umjetnik: UmjetnikPogled
) {

    constructor(album: Album) : this(
        id = album.id,
        naziv = album.naziv,
        opis = album.opis,
        slika = album.slika,
        datumDodano = album.datumDodano,
        umjetnik = UmjetnikPogled(album.umjetnik)
    )
}