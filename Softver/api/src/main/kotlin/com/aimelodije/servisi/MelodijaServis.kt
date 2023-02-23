package com.aimelodije.servisi

import com.aimelodije.iznimke.album.AlbumNijePronadenIznimka
import com.aimelodije.iznimke.melodija.MelodijaNijePronadenaIznimka
import com.aimelodije.iznimke.umjetnik.UmjetnikNijePronadenIznimka
import com.aimelodije.iznimke.zanr.ZanrNijePronadenIznimka
import com.aimelodije.modeli.domena.Melodija
import com.aimelodije.modeli.pogledi.MelodijaPogled
import com.aimelodije.modeli.zahtjevi.MelodijaAzuriranjeZahtjev
import com.aimelodije.modeli.zahtjevi.MelodijaDodavanjeZahtjev
import com.aimelodije.repozitoriji.MelodijaRepozitorij
import com.aimelodije.repozitoriji.UmjetnikRepozitorij
import com.aimelodije.repozitoriji.ZanrRepozitorij
import com.aimelodije.repozitoriji.datotecni.AudioRepozitorij
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MelodijaServis(
    private val umjetnikRepozitorij: UmjetnikRepozitorij,
    private val melodijaRepozitorij: MelodijaRepozitorij,
    private val zanrRepozitorij: ZanrRepozitorij,
    private val audioRepozitorij: AudioRepozitorij
) {

    @Transactional(readOnly = true)
    @Throws(UmjetnikNijePronadenIznimka::class, AlbumNijePronadenIznimka::class)
    fun dohvatiSve(umjetnikId: Long, albumId: Long): List<MelodijaPogled> {
        val umjetnik = umjetnikRepozitorij.findByIdOrNull(umjetnikId) ?: throw UmjetnikNijePronadenIznimka()
        val album = umjetnik.albumi.firstOrNull { it.id == albumId } ?: throw AlbumNijePronadenIznimka()
        return album.melodije.map { MelodijaPogled(it) }
    }

    @Transactional(readOnly = true)
    fun dohvatiSve(stranicenje: Pageable): Page<MelodijaPogled> =
        melodijaRepozitorij.findAll(stranicenje).map { MelodijaPogled(it) }

    @Transactional(readOnly = true)
    fun pretrazuj(dioNaziva: String, stranicenje: Pageable): Page<MelodijaPogled> =
        melodijaRepozitorij.findAllByNazivContainsIgnoreCase(dioNaziva, stranicenje).map { MelodijaPogled(it) }

    @Transactional(readOnly = true)
    @Throws(UmjetnikNijePronadenIznimka::class, AlbumNijePronadenIznimka::class, MelodijaNijePronadenaIznimka::class)
    fun dohvati(umjetnikId: Long, albumId: Long, melodijaId: Long): MelodijaPogled {
        val umjetnik = umjetnikRepozitorij.findByIdOrNull(umjetnikId) ?: throw UmjetnikNijePronadenIznimka()
        val album = umjetnik.albumi.firstOrNull { it.id == albumId } ?: throw AlbumNijePronadenIznimka()
        return MelodijaPogled(
            album.melodije.firstOrNull { it.id == melodijaId } ?: throw MelodijaNijePronadenaIznimka()
        )
    }

    @Transactional
    @Throws(UmjetnikNijePronadenIznimka::class, AlbumNijePronadenIznimka::class, ZanrNijePronadenIznimka::class)
    fun dodaj(melodijaZahtjev: MelodijaDodavanjeZahtjev): MelodijaPogled {
        val umjetnik = umjetnikRepozitorij.findByIdOrNull(
            melodijaZahtjev.umjetnikId
        ) ?: throw UmjetnikNijePronadenIznimka()
        autentificiraj(umjetnik.korime)

        val album = umjetnik.albumi.firstOrNull { it.id == melodijaZahtjev.albumId } ?: throw AlbumNijePronadenIznimka()

        val melodija = Melodija(
            zanr = zanrRepozitorij.findByIdOrNull(melodijaZahtjev.zanrId) ?: throw ZanrNijePronadenIznimka(),
            audio = audioRepozitorij.save(melodijaZahtjev.audio!!, type = Melodija::class.java),
            autor = umjetnik,
            albumi = mutableSetOf(album)
        ).also { melodija ->
            melodijaZahtjev.naziv?.let {
                melodija.naziv = it
            }
        }

        album.melodije.add(melodija)

        return MelodijaPogled(
            melodijaRepozitorij.save(melodija)
        )
    }

    @Transactional
    @Throws(UmjetnikNijePronadenIznimka::class, AlbumNijePronadenIznimka::class, MelodijaNijePronadenaIznimka::class)
    fun azuriraj(melodijaZahtjev: MelodijaAzuriranjeZahtjev): MelodijaPogled {
        val umjetnik = umjetnikRepozitorij.findByIdOrNull(
            melodijaZahtjev.umjetnikId
        ) ?: throw UmjetnikNijePronadenIznimka()
        autentificiraj(umjetnik.korime)

        val album = umjetnik.albumi.firstOrNull {
            it.id == melodijaZahtjev.albumId
        } ?: throw AlbumNijePronadenIznimka()
        val melodija = album.melodije.firstOrNull {
            it.id == melodijaZahtjev.melodijaId
        } ?: throw MelodijaNijePronadenaIznimka()

        melodija.naziv = melodijaZahtjev.naziv

        return MelodijaPogled(
            melodijaRepozitorij.save(melodija)
        )
    }

    @Transactional
    @Throws(UmjetnikNijePronadenIznimka::class, AlbumNijePronadenIznimka::class, MelodijaNijePronadenaIznimka::class)
    fun obrisi(umjetnikId: Long, albumId: Long, melodijaId: Long) {
        val umjetnik = umjetnikRepozitorij.findByIdOrNull(umjetnikId) ?: throw UmjetnikNijePronadenIznimka()
        autentificiraj(umjetnik.korime)

        val album = umjetnik.albumi.firstOrNull { it.id == albumId } ?: throw AlbumNijePronadenIznimka()
        val melodija = album.melodije.firstOrNull { it.id == melodijaId } ?: throw MelodijaNijePronadenaIznimka()

        audioRepozitorij.delete(melodija.audio, Melodija::class.java)
        melodijaRepozitorij.delete(melodija)
    }

    @Throws(UmjetnikNijePronadenIznimka::class)
    private fun autentificiraj(korime: String) {
        if (korime != SecurityContextHolder.getContext().authentication.principal)
            throw UmjetnikNijePronadenIznimka()
    }
}