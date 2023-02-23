package com.aimelodije.modeli.enumeracije

enum class TipResursa {
    ALBUM,
    MELODIJA;

    object Nazivi {
        const val ALBUM = "ALBUM"
        const val MELODIJA = "MELODIJA"
    }
}