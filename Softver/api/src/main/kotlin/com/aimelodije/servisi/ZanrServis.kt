package com.aimelodije.servisi

import com.aimelodije.iznimke.zanr.PostojeciZanrIznimka
import com.aimelodije.iznimke.zanr.ZanrNijePronadenIznimka
import com.aimelodije.modeli.pogledi.ZanrPogled
import com.aimelodije.modeli.zahtjevi.ZanrZahtjev
import com.aimelodije.repozitoriji.ZanrRepozitorij
import org.springframework.dao.DataAccessException
import org.springframework.stereotype.Service

@Service
class ZanrServis(
    private val zanrRepozitorij: ZanrRepozitorij
) {

    fun dohvatiSve(): List<ZanrPogled> =
        zanrRepozitorij.findAll().map { ZanrPogled(it) }

    @Throws(PostojeciZanrIznimka::class)
    fun dodaj(zanrZahtjev: ZanrZahtjev): ZanrPogled {
        if (zanrRepozitorij.existsByNazivIgnoreCase(zanrZahtjev.naziv))
            throw PostojeciZanrIznimka()

        zanrRepozitorij.upsert(zanrZahtjev.naziv)

        return ZanrPogled(zanrRepozitorij.findByNazivIgnoreCase(zanrZahtjev.naziv)!!)
    }

    @Throws(PostojeciZanrIznimka::class, ZanrNijePronadenIznimka::class)
    fun azuriraj(zanrZahtjev: ZanrZahtjev): ZanrPogled = try {
        ZanrPogled(
            zanrRepozitorij.update(zanrZahtjev.toZanr()) ?: throw ZanrNijePronadenIznimka()
        )
    } catch (sqlIznimka: DataAccessException) {
        throw PostojeciZanrIznimka()
    }

    @Throws(ZanrNijePronadenIznimka::class)
    fun obrisi(id: Long): Boolean {
        if (zanrRepozitorij.existsById(id).not())
            throw ZanrNijePronadenIznimka()

        return try {
            zanrRepozitorij.delete(id)
        } catch (sqlIznimka: DataAccessException) {
            false
        }
    }

    fun obrisiNekoristene() = zanrRepozitorij.deleteUnused()
}