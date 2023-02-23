package com.aimelodije.servisi

import com.aimelodije.iznimke.umjetnik.IlegalanUmjetnik
import com.aimelodije.iznimke.umjetnik.IlegalanUmjetnikIznimka
import com.aimelodije.iznimke.umjetnik.UmjetnikNijePronadenIznimka
import com.aimelodije.modeli.domena.Umjetnik
import com.aimelodije.modeli.pogledi.UmjetnikPogled
import com.aimelodije.modeli.zahtjevi.UmjetnikAzuriranjeZahtjev
import com.aimelodije.repozitoriji.UmjetnikRepozitorij
import com.aimelodije.repozitoriji.datotecni.SlikaRepozitorij
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UmjetnikServis(
    private val umjetnikRepozitorij: UmjetnikRepozitorij,
    private val enkoderLozinke: PasswordEncoder,
    private val slikaRepozitorij: SlikaRepozitorij
) {

    @Throws(UmjetnikNijePronadenIznimka::class)
    fun dohvati(id: Long): UmjetnikPogled =
        UmjetnikPogled(
            umjetnikRepozitorij.findByIdOrNull(id) ?: throw UmjetnikNijePronadenIznimka()
        )

    @Transactional
    @Throws(UmjetnikNijePronadenIznimka::class, IlegalanUmjetnikIznimka::class)
    fun azuriraj(umjetnikZahtjev: UmjetnikAzuriranjeZahtjev): UmjetnikPogled {
        val dbUmjetnik = umjetnikRepozitorij.findByIdOrNull(umjetnikZahtjev.id) ?: throw UmjetnikNijePronadenIznimka()
        if (dbUmjetnik.korime != SecurityContextHolder.getContext().authentication.name)
            throw UmjetnikNijePronadenIznimka()

        umjetnikZahtjev.email?.let {
            if (umjetnikRepozitorij.existsByEmailIgnoreCase(it))
                throw IlegalanUmjetnikIznimka(IlegalanUmjetnik.POSTOJECI_EMAIL.poruka)
        }

        var putanjaSlike: String? = null
        umjetnikZahtjev.slika?.let {
            putanjaSlike = slikaRepozitorij.save(it, dbUmjetnik.slika, Umjetnik::class.java)
        }

        return UmjetnikPogled(
            umjetnikRepozitorij.save(azuriraj(dbUmjetnik, umjetnikZahtjev, putanjaSlike))
        )
    }

    private fun azuriraj(
        umjetnik: Umjetnik,
        umjetnikZahtjev: UmjetnikAzuriranjeZahtjev,
        putanjaSlike: String? = null
    ): Umjetnik = umjetnik.apply {
        email = umjetnikZahtjev.email ?: umjetnik.email
        lozinka = umjetnikZahtjev.lozinka?.let { enkoderLozinke.encode(it) } ?: umjetnik.lozinka
        ime = umjetnikZahtjev.ime ?: umjetnik.ime
        prezime = umjetnikZahtjev.prezime ?: umjetnik.prezime
        opis = umjetnikZahtjev.opis ?: umjetnik.opis
        slika = putanjaSlike ?: umjetnik.slika
    }
}