package com.aimelodije.modeli.pogledi

import com.aimelodije.modeli.domena.Melodija
import java.util.*

data class MelodijaPogled(
    val id: Long,
    val audio: String,
    val naziv: String,
    val datumDodano: Date,
    val autor: UmjetnikPogled,
    val zanr: ZanrPogled,
    val albumi: List<AlbumPogled>
) {

    constructor(melodija: Melodija) : this(
        id = melodija.id,
        audio = melodija.audio,
        naziv = melodija.naziv,
        datumDodano = melodija.datumDodano,
        autor = UmjetnikPogled(melodija.autor),
        zanr = ZanrPogled((melodija.zanr)),
        albumi = melodija.albumi.map { AlbumPogled(it) }
    )
}