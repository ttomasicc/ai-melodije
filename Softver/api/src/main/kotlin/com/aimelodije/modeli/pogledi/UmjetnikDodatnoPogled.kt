package com.aimelodije.modeli.pogledi

import com.aimelodije.modeli.enumeracije.UmjetnikAkcija

data class UmjetnikDodatnoPogled(
    val akcija: UmjetnikAkcija,
    val umjetnik: UmjetnikPogled
)