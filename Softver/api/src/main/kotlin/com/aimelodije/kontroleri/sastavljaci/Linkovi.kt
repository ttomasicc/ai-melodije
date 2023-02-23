package com.aimelodije.kontroleri.sastavljaci

enum class Linkovi {
    PRIJAVA,
    ODJAVA,
    TOKEN,
    PROFIL,
    ALBUMI,
    MELODIJE;

    override fun toString() = name.lowercase()
}