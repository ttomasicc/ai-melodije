package com.aimelodije.iznimke.umjetnik

enum class IlegalanUmjetnik(val poruka: String) {
    POSTOJECI_EMAIL("Umjetnik s danom email adresom već postoji"),
    POSTOJECE_KORISNICKO_IME("Umjetnik s danim korisničkim imenom već postoji")
}