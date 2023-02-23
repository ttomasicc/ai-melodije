package com.aimelodije.modeli.pogledi

import com.aimelodije.modeli.domena.Umjetnik
import java.util.Date

data class UmjetnikPogled(
    val id: Long,
    val korime: String,
    val email: String,
    val ime: String?,
    val prezime: String?,
    val opis: String?,
    val slika: String?,
    val datumRegistracije: Date,
    val rola: String
) {
    constructor(umjetnik: Umjetnik) : this(
        id = umjetnik.id,
        korime = umjetnik.korime,
        email = umjetnik.email,
        ime = umjetnik.ime,
        prezime = umjetnik.prezime,
        opis = umjetnik.opis,
        slika = umjetnik.slika,
        datumRegistracije = umjetnik.datumRegistracije,
        rola = umjetnik.rola.name
    )
}