package com.aimelodije.kontroleri

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.validation.BindingResult
import org.springframework.web.server.ResponseStatusException

@Component
class KontrolerValidator {

    fun provjeriNeNullPogreske(rezultatPovezivanja: BindingResult, iznimke: Iterable<String> = listOf()) {
        if (rezultatPovezivanja.hasErrors()) {
            val pogreske = rezultatPovezivanja.fieldErrors.filter {
                it.rejectedValue != null || it.field in iznimke
            }
            if (pogreske.isNotEmpty())
                throw ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    pogreske.joinToString(separator = "; ") { it.defaultMessage ?: "Nepoznata pogreška" }
                )
        }
    }

    fun provjeriPogreske(rezultatPovezivanja: BindingResult) {
        if (rezultatPovezivanja.hasErrors()) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                rezultatPovezivanja.fieldErrors.joinToString(separator = "; ") {
                    it.defaultMessage ?: "Nepoznata pogreška"
                }
            )
        }
    }
}