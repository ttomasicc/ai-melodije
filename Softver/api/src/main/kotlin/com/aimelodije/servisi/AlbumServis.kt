package com.aimelodije.servisi

import com.aimelodije.iznimke.album.AlbumNijePronadenIznimka
import com.aimelodije.iznimke.umjetnik.UmjetnikNijePronadenIznimka
import com.aimelodije.modeli.domena.Album
import com.aimelodije.modeli.domena.Melodija
import com.aimelodije.modeli.pogledi.AlbumPogled
import com.aimelodije.modeli.zahtjevi.AlbumAzuriranjeZahtjev
import com.aimelodije.modeli.zahtjevi.AlbumDodavanjeZahtjev
import com.aimelodije.repozitoriji.AlbumRepozitorij
import com.aimelodije.repozitoriji.MelodijaRepozitorij
import com.aimelodije.repozitoriji.UmjetnikRepozitorij
import com.aimelodije.repozitoriji.datotecni.AudioRepozitorij
import com.aimelodije.repozitoriji.datotecni.SlikaRepozitorij
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AlbumServis(
    private val albumRepozitorij: AlbumRepozitorij,
    private val umjetnikRepozitorij: UmjetnikRepozitorij,
    private val melodijaRepozitorij: MelodijaRepozitorij,
    private val slikaRepozitorij: SlikaRepozitorij,
    private val audioRepozitorij: AudioRepozitorij
) {

    @Transactional(readOnly = true)
    @Throws(UmjetnikNijePronadenIznimka::class)
    fun dohvatiSve(umjetnikId: Long): List<AlbumPogled> {
        val umjetnik = umjetnikRepozitorij.findByIdOrNull(umjetnikId) ?: throw UmjetnikNijePronadenIznimka()
        return umjetnik.albumi.map { AlbumPogled(it) }
    }

    @Transactional(readOnly = true)
    fun dohvatiSve(stranicenje: Pageable): Page<AlbumPogled> =
        albumRepozitorij.findAll(stranicenje).map { AlbumPogled(it) }

    @Transactional(readOnly = true)
    fun pretrazuj(dioNaziva: String, stranicenje: Pageable): Page<AlbumPogled> =
        albumRepozitorij.findAllByNazivContainsIgnoreCase(dioNaziva, stranicenje).map { AlbumPogled(it) }

    @Transactional(readOnly = true)
    @Throws(UmjetnikNijePronadenIznimka::class, AlbumNijePronadenIznimka::class)
    fun dohvati(umjetnikId: Long, albumId: Long): AlbumPogled {
        val umjetnik = umjetnikRepozitorij.findByIdOrNull(umjetnikId) ?: throw UmjetnikNijePronadenIznimka()
        return AlbumPogled(
            umjetnik.albumi.firstOrNull { it.id == albumId } ?: throw AlbumNijePronadenIznimka()
        )
    }

    @Throws(UmjetnikNijePronadenIznimka::class)
    fun dodaj(albumZahtjev: AlbumDodavanjeZahtjev): AlbumPogled =
        AlbumPogled(
            albumRepozitorij.save(
                albumZahtjev.toAlbum { umjetnikId ->
                    (umjetnikRepozitorij.findByIdOrNull(umjetnikId) ?: throw UmjetnikNijePronadenIznimka()).also {
                        autentificiraj(it.korime)
                    }
                }
            )
        )

    @Transactional
    @Throws(AlbumNijePronadenIznimka::class)
    fun azuriraj(albumZahtjev: AlbumAzuriranjeZahtjev): AlbumPogled {
        val dbAlbum = albumRepozitorij.findByIdOrNull(albumZahtjev.albumId) ?: throw AlbumNijePronadenIznimka()
        autentificiraj(dbAlbum.umjetnik.korime)

        var putanjaSlike: String? = null
        albumZahtjev.slika?.let {
            putanjaSlike = slikaRepozitorij.save(it, dbAlbum.slika, Album::class.java)
        }

        return AlbumPogled(
            albumRepozitorij.save(azuriraj(dbAlbum, albumZahtjev, putanjaSlike))
        )
    }

    @Transactional
    @Throws(AlbumNijePronadenIznimka::class)
    fun obrisi(albumId: Long) {
        val album = albumRepozitorij.findByIdOrNull(albumId) ?: throw AlbumNijePronadenIznimka()
        autentificiraj(album.umjetnik.korime)

        album.melodije.forEach { melodija ->
            if (melodija.albumi.size == 1) {
                melodijaRepozitorij.deleteById(melodija.id)
                audioRepozitorij.delete(melodija.audio, Melodija::class.java)
            }
        }

        album.slika?.let {
            slikaRepozitorij.delete(it, Album::class.java)
        }

        albumRepozitorij.deleteById(album.id)
    }

    private fun azuriraj(
        album: Album,
        albumZahtjev: AlbumAzuriranjeZahtjev,
        putanjaSlike: String? = null
    ): Album = album.apply {
        naziv = albumZahtjev.naziv ?: album.naziv
        opis = albumZahtjev.opis ?: album.opis
        slika = putanjaSlike ?: album.slika
    }

    @Throws(UmjetnikNijePronadenIznimka::class)
    private fun autentificiraj(korime: String) {
        if (korime != SecurityContextHolder.getContext().authentication.principal)
            throw UmjetnikNijePronadenIznimka()
    }
}