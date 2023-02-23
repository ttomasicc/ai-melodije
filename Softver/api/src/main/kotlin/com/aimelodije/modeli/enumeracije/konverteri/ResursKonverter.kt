package com.aimelodije.modeli.enumeracije.konverteri

import com.aimelodije.modeli.enumeracije.TipResursa
import org.springframework.core.convert.converter.Converter

class ResursKonverter : Converter<String, TipResursa> {

    override fun convert(source: String): TipResursa? =
        try {
            TipResursa.valueOf(source.uppercase())
        } catch (iznimka: IllegalArgumentException) {
            null
        }
}