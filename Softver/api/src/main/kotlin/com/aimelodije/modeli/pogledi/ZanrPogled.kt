package com.aimelodije.modeli.pogledi

import com.aimelodije.modeli.domena.Zanr

data class ZanrPogled(
    val id: Long,
    val naziv: String
) {
    constructor(zanr: Zanr) : this(
        id = zanr.id,
        naziv = zanr.naziv
    )
}