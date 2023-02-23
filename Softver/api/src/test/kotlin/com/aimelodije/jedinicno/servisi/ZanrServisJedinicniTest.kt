package com.aimelodije.jedinicno.servisi

import com.aimelodije.iznimke.zanr.PostojeciZanrIznimka
import com.aimelodije.iznimke.zanr.ZanrNijePronadenIznimka
import com.aimelodije.modeli.domena.Zanr
import com.aimelodije.modeli.pogledi.ZanrPogled
import com.aimelodije.modeli.zahtjevi.ZanrZahtjev
import com.aimelodije.repozitoriji.ZanrRepozitorij
import com.aimelodije.servisi.ZanrServis
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.DuplicateKeyException

@ExtendWith(MockKExtension::class)
class ZanrServisJedinicniTest {

    @MockK
    lateinit var zanrRepozitorij: ZanrRepozitorij

    @InjectMockKs
    lateinit var zanrServis: ZanrServis

    @Nested
    inner class DohvatSvihZanrova() {
        @Test
        fun `dohvacanje svih zanrova uspjesno vraca sve zanrove`() {
            val zanrovi = listOf(
                Zanr(id = 1, "rockabilly"),
                Zanr(id = 2, "blues"),
                Zanr(id = 3, "jazz")
            )

            every {
                zanrRepozitorij.findAll()
            } returns zanrovi

            assertThat(zanrServis.dohvatiSve())
                .isEqualTo(zanrovi.map { ZanrPogled(it) })
        }
    }

    @Nested
    inner class DodavanjeZanra() {
        @Test
        fun `dodavanje postojeceg zanra baca PostojeciZanrIznimka`() {
            val zanrZahtjev = ZanrZahtjev(naziv = "blues")

            every {
                zanrRepozitorij.existsByNazivIgnoreCase(zanrZahtjev.naziv)
            } returns true

            assertThrows<PostojeciZanrIznimka> {
                zanrServis.dodaj(zanrZahtjev)
            }
            verify(exactly = 0) {
                zanrRepozitorij.upsert(zanrZahtjev.naziv)
            }
        }

        @Test
        fun `dodavanje zanra uspjesno vraca kreirani zanr`() {
            val zanrZahtjev = ZanrZahtjev(naziv = "Blues")
            val zanrDomena = Zanr(id = 3, naziv = zanrZahtjev.naziv.lowercase())
            val zanrPogled = ZanrPogled(zanrDomena)

            every {
                zanrRepozitorij.existsByNazivIgnoreCase(zanrZahtjev.naziv)
            } returns false
            every {
                zanrRepozitorij.upsert(zanrZahtjev.naziv)
            } returns Unit
            every {
                zanrRepozitorij.findByNazivIgnoreCase(zanrZahtjev.naziv)
            } returns zanrDomena

            assertThat(zanrServis.dodaj(zanrZahtjev))
                .isEqualTo(zanrPogled)
        }
    }

    @Nested
    inner class AzuriranjeZanra() {
        @Test
        fun `azuriranje nepostojeceg zanra baca ZanrNijePronadenIznimka`() {
            val zanrZahtjev = ZanrZahtjev(naziv = "Blues")

            every {
                zanrRepozitorij.update(any())
            } returns null

            assertThrows<ZanrNijePronadenIznimka> {
                zanrServis.azuriraj(zanrZahtjev)
            }
        }

        @Test
        fun `azuriranje naziva zanra na naziv vec postojeceg zanra baca PostojeciZanrIznimka`() {
            val zanrZahtjev = ZanrZahtjev(naziv = "Blues")

            every {
                zanrRepozitorij.update(any())
            } throws DuplicateKeyException("")

            assertThrows<PostojeciZanrIznimka> {
                zanrServis.azuriraj(zanrZahtjev)
            }
        }

        @Test
        fun `azuriranje zanra uspjesno vraca azurirani zanr`() {
            val zanrZahtjev = ZanrZahtjev(naziv = "Blues")
            val zanrDomena = Zanr(id = 3, naziv = zanrZahtjev.naziv.lowercase())
            val zanrPogled = ZanrPogled(zanrDomena)

            every {
                zanrRepozitorij.update(any())
            } returns zanrDomena

            assertThat(zanrServis.azuriraj(zanrZahtjev))
                .isEqualTo(zanrPogled)
        }
    }

    @Nested
    inner class BrisanjeZanra() {
        @Test
        fun `brisanje nepostojeceg zanra baca ZanrNijePronadenIznimka`() {
            val zanrId = 5L

            every {
                zanrRepozitorij.existsById(zanrId)
            } returns false

            assertThrows<ZanrNijePronadenIznimka> {
                zanrServis.obrisi(zanrId)
            }
            verify(exactly = 0) {
                zanrRepozitorij.delete(zanrId)
            }
        }

        @Test
        fun `brisanje referenciranog zanra vraca false`() {
            val zanrId = 5L

            every {
                zanrRepozitorij.existsById(zanrId)
            } returns true
            every {
                zanrRepozitorij.delete(zanrId)
            } throws DataIntegrityViolationException("")

            assertFalse(zanrServis.obrisi(zanrId))
        }
    }
}