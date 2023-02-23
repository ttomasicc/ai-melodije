package com.aimelodije.modeli.zahtjevi

import com.aimelodije.modeli.domena.Umjetnik
import com.aimelodije.modeli.enumeracije.Rola
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UmjetnikRegistracijaZahtjev(
    @field:NotBlank(message = "Korisničko ime ne može biti prazno")
    @field:Size(max = 50, message = "Maksimalna veličina korisničkog imena je 50 karaktera")
    val korime: String = "",

    @field:NotBlank(message = "Email ne može biti prazan")
    @field:Size(max = 70, message = "Maksimalna veličina email adrese je 70 karaktera")
    @field:Email(message = "Nevažeća email adresa")
    val email: String = "",

    @field:NotBlank(message = "Lozinka ne može biti prazna")
    val lozinka: String = "",

    @field:NotBlank(message = "Ime ne može biti prazno")
    @field:Size(max = 70, message = "Maksimalna veličina imena je 70 karaktera")
    val ime: String? = null,

    @field:NotBlank(message = "Prezime ne može biti prazno")
    @field:Size(max = 70, message = "Maksimalna veličina prezimena je 70 karaktera")
    val prezime: String? = null,

    @field:NotBlank(message = "Opis ne može biti prazan")
    val opis: String? = null
) {

    fun toUmjetnik(
        dohvatiRolu: () -> Rola,
        dohvatiLozinku: (lozinka: String) -> String
    ) = Umjetnik(
        korime = korime.lowercase(),
        email = email.lowercase(),
        lozinka = dohvatiLozinku(lozinka),
        ime = ime,
        prezime = prezime,
        opis = opis,
        rola = dohvatiRolu()
    )
}