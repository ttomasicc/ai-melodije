package com.aimelodije.jedinicno.servisi

import com.aimelodije.iznimke.album.AlbumNijePronadenIznimka
import com.aimelodije.iznimke.umjetnik.UmjetnikNijePronadenIznimka
import com.aimelodije.modeli.domena.Album
import com.aimelodije.modeli.domena.Melodija
import com.aimelodije.modeli.domena.Umjetnik
import com.aimelodije.modeli.domena.Zanr
import com.aimelodije.modeli.enumeracije.Rola
import com.aimelodije.modeli.pogledi.AlbumPogled
import com.aimelodije.modeli.zahtjevi.AlbumAzuriranjeZahtjev
import com.aimelodije.modeli.zahtjevi.AlbumDodavanjeZahtjev
import com.aimelodije.repozitoriji.AlbumRepozitorij
import com.aimelodije.repozitoriji.MelodijaRepozitorij
import com.aimelodije.repozitoriji.UmjetnikRepozitorij
import com.aimelodije.repozitoriji.datotecni.AudioRepozitorij
import com.aimelodije.repozitoriji.datotecni.SlikaRepozitorij
import com.aimelodije.servisi.AlbumServis
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

@ExtendWith(MockKExtension::class)
class AlbumServisJedinicniTest {

    @MockK
    lateinit var albumRepozitorij: AlbumRepozitorij

    @MockK
    lateinit var umjetnikRepozitorij: UmjetnikRepozitorij

    @MockK
    lateinit var melodijaRepozitorij: MelodijaRepozitorij

    @MockK(relaxed = true)
    lateinit var slikaRepozitorij: SlikaRepozitorij

    @MockK(relaxed = true)
    lateinit var audioRepozitorij: AudioRepozitorij

    @InjectMockKs
    lateinit var albumServis: AlbumServis

    private val testUmjetnik = Umjetnik(
        id = 5,
        korime = "ttomasic",
        email = "ttomasic20@student.foi.hr",
        lozinka = "admin",
        rola = Rola.ADMINISTRATOR,
        albumi = mutableSetOf()
    )

    private val testAlbumi = listOf(
        Album(id = 1, naziv = "Algoritamske ode", umjetnik = testUmjetnik),
        Album(id = 2, naziv = "Kvantna glazba", umjetnik = testUmjetnik),
        Album(id = 3, naziv = "Digitalne fantazije", umjetnik = testUmjetnik),
        Album(id = 4, naziv = "Kvarkovi", umjetnik = testUmjetnik)
    )

    @BeforeEach
    fun setup() {
        SecurityContextHolder.getContext().authentication = TestingAuthenticationToken(testUmjetnik.korime, null)
        testUmjetnik.albumi.clear()
    }

    @Nested
    inner class DohvatSvihAlbuma {
        @Test
        fun `dohvacanje svih albuma nepostojeceg umjetnika baca UmjetnikNijePronadenIznimka`() {
            every {
                umjetnikRepozitorij.findByIdOrNull(testUmjetnik.id)
            } returns null

            assertThrows<UmjetnikNijePronadenIznimka> {
                albumServis.dohvatiSve(testUmjetnik.id)
            }
        }

        @Test
        fun `dohvacanje svih albuma umjetnika bez albuma vraca praznu listu`() {
            every {
                umjetnikRepozitorij.findByIdOrNull(testUmjetnik.id)
            } returns testUmjetnik

            assertThat(albumServis.dohvatiSve(testUmjetnik.id))
                .isEmpty()
        }

        @Test
        fun `dohvacanje svih albuma umjetnika vraca listu svih albuma`() {
            testUmjetnik.albumi.addAll(testAlbumi)

            every {
                umjetnikRepozitorij.findByIdOrNull(testUmjetnik.id)
            } returns testUmjetnik

            assertThat(albumServis.dohvatiSve(testUmjetnik.id))
                .isEqualTo(testUmjetnik.albumi.map { AlbumPogled(it) })
        }

        @Test
        fun `dohvacanje svih stranicenih albuma vraca stranicu s popisom albuma`() {
            testUmjetnik.albumi.addAll(testAlbumi)
            val stranica = PageImpl(testAlbumi)

            every {
                albumRepozitorij.findAll(Pageable.unpaged())
            } returns stranica

            assertThat(albumServis.dohvatiSve(Pageable.unpaged()))
                .isEqualTo(stranica.map { AlbumPogled(it) })
        }
    }

    @Nested
    inner class PretrazivanjeAlbuma {
        @Test
        fun `pretrazivanje albuma vraca stranicu albuma ciji nazivi sadrze kljucnu rijec`() {
            testUmjetnik.albumi.addAll(testAlbumi)
            val kljucnaRijec = "Kva"
            val stranica = PageImpl(testAlbumi.filter { it.naziv.contains(kljucnaRijec) })

            every {
                albumRepozitorij.findAllByNazivContainsIgnoreCase(kljucnaRijec, Pageable.unpaged())
            } returns stranica

            assertThat(albumServis.pretrazuj(kljucnaRijec, Pageable.unpaged()))
                .isEqualTo(stranica.map { AlbumPogled(it) })
        }
    }

    @Nested
    inner class DohvatJednogAlbuma {
        @Test
        fun `dohvacanje albuma nepostojeceg umjetnika baca UmjetnikNijePronadenIznimka`() {
            every {
                umjetnikRepozitorij.findByIdOrNull(testUmjetnik.id)
            } returns null

            assertThrows<UmjetnikNijePronadenIznimka> {
                albumServis.dohvati(testUmjetnik.id, 5L)
            }
        }

        @Test
        fun `dohvacanje nepostojeceg albuma danog umjetnika baca AlbumNijePronadenIznimka`() {
            testUmjetnik.albumi.addAll(testAlbumi)
            val trazeniAlbumId = 10L

            every {
                umjetnikRepozitorij.findByIdOrNull(testUmjetnik.id)
            } returns testUmjetnik

            assertThrows<AlbumNijePronadenIznimka> {
                albumServis.dohvati(testUmjetnik.id, trazeniAlbumId)
            }
        }

        @Test
        fun `dohvacanje albuma danog umjetnika vraca album`() {
            testUmjetnik.albumi.addAll(testAlbumi)
            val trazeniAlbumId = 3L

            every {
                umjetnikRepozitorij.findByIdOrNull(testUmjetnik.id)
            } returns testUmjetnik

            assertThat(albumServis.dohvati(testUmjetnik.id, trazeniAlbumId))
                .isEqualTo(AlbumPogled(testAlbumi.first { it.id == trazeniAlbumId }))
        }
    }

    @Nested
    inner class DodavanjeAlbuma {
        @Test
        fun `dodavanje albuma nepostojecem umjetniku baca UmjetnikNijePronadenIznimka`() {
            val albumZahtjev = AlbumDodavanjeZahtjev(
                umjetnikId = 5,
                naziv = "AI Simfonije"
            )

            every {
                umjetnikRepozitorij.findByIdOrNull(albumZahtjev.umjetnikId)
            } returns null

            assertThrows<UmjetnikNijePronadenIznimka> {
                albumServis.dodaj(albumZahtjev)
            }
        }

        @Test
        fun `dodavanje albuma tudem umjetniku baca UmjetnikNijePronadenIznimka`() {
            SecurityContextHolder.getContext().authentication = TestingAuthenticationToken("elvis", null)
            val albumZahtjev = AlbumDodavanjeZahtjev(
                umjetnikId = testUmjetnik.id,
                naziv = "AI Simfonije"
            )

            every {
                umjetnikRepozitorij.findByIdOrNull(albumZahtjev.umjetnikId)
            } returns testUmjetnik

            assertThrows<UmjetnikNijePronadenIznimka> {
                albumServis.dodaj(albumZahtjev)
            }
        }

        @Test
        fun `dodavanje albuma vraca kreirani album`() {
            val albumZahtjev = AlbumDodavanjeZahtjev(
                umjetnikId = testUmjetnik.id,
                naziv = "AI Simfonije"
            )
            val albumDomena = Album(
                id = 5,
                naziv = albumZahtjev.naziv!!,
                umjetnik = testUmjetnik
            )

            every {
                umjetnikRepozitorij.findByIdOrNull(albumZahtjev.umjetnikId)
            } returns testUmjetnik
            every {
                albumRepozitorij.save(any())
            } returns albumDomena

            val albumPogled = albumServis.dodaj(albumZahtjev)

            assertAll(
                "albumPogled",
                { assertEquals(albumDomena.id, albumPogled.id) },
                { assertEquals(albumDomena.naziv, albumPogled.naziv) }
            )
        }
    }

    @Nested
    inner class AzuriranjeAlbuma {
        @Test
        fun `azuriranje nepostojeceg albuma baca AlbumNijePronadenIznimka`() {
            val albumZahtjev = AlbumAzuriranjeZahtjev(
                albumId = 3,
                naziv = "AI Simfonije"
            )

            every {
                albumRepozitorij.findByIdOrNull(albumZahtjev.albumId)
            } returns null

            assertThrows<AlbumNijePronadenIznimka> {
                albumServis.azuriraj(albumZahtjev)
            }
            verify(exactly = 0) {
                albumRepozitorij.save(any())
            }
        }

        @Test
        fun `azuriranje tudeg albuma baca UmjetnikNijePronadenIznimka`() {
            SecurityContextHolder.getContext().authentication = TestingAuthenticationToken("elvis", null)
            val albumZahtjev = AlbumAzuriranjeZahtjev(
                albumId = 3,
                naziv = "AI Simfonije"
            )

            every {
                albumRepozitorij.findByIdOrNull(albumZahtjev.albumId)
            } returns testAlbumi.first()

            assertThrows<UmjetnikNijePronadenIznimka> {
                albumServis.azuriraj(albumZahtjev)
            }
            verify(exactly = 0) {
                albumRepozitorij.save(any())
            }
        }

        @Test
        fun `azuriranje slike albuma vraca album s azuriranim nazivom slike`() {
            val testAlbum = testAlbumi.first()
            val staraSlikaAlbuma = testAlbum.slika
            val stariNazivAlbuma = testAlbum.naziv

            val slika = MockMultipartFile("jabuka.png", null)
            val albumZahtjev = AlbumAzuriranjeZahtjev(
                albumId = 3,
                slika = slika
            )
            val noviNazivSlikaAlbuma = "nova-jabuka.png"

            every {
                albumRepozitorij.findByIdOrNull(albumZahtjev.albumId)
            } returns testAlbum
            every {
                slikaRepozitorij.save(slika, testAlbum.slika, Album::class.java)
            } returns noviNazivSlikaAlbuma
            every {
                albumRepozitorij.save(testAlbum)
            } returns testAlbum

            assertThat(albumServis.azuriraj(albumZahtjev))
                .isEqualTo(AlbumPogled(testAlbum))
            assertThat(testAlbum.slika)
                .isEqualTo(noviNazivSlikaAlbuma)
            assertThat(testAlbum.naziv)
                .isEqualTo(stariNazivAlbuma)
            verify(exactly = 1) {
                slikaRepozitorij.save(slika, null, Album::class.java)
            }

            testAlbum.slika = staraSlikaAlbuma
        }

        @Test
        fun `azuriranje naziva albuma vraca album s azuriranim nazivom albuma`() {
            val testAlbum = testAlbumi.first()
            val stariNazivAlbuma = testAlbum.naziv
            val stariOpisAlbuma = testAlbum.opis

            val noviNazivAlbuma = "Cyber sonate"
            val albumZahtjev = AlbumAzuriranjeZahtjev(
                albumId = 3,
                naziv = noviNazivAlbuma
            )

            every {
                albumRepozitorij.findByIdOrNull(albumZahtjev.albumId)
            } returns testAlbum
            every {
                albumRepozitorij.save(testAlbum)
            } returns testAlbum

            assertThat(albumServis.azuriraj(albumZahtjev))
                .isEqualTo(AlbumPogled(testAlbum))
            assertThat(testAlbum.naziv)
                .isEqualTo(noviNazivAlbuma)
            assertThat(testAlbum.opis)
                .isEqualTo(stariOpisAlbuma)
            verify(exactly = 0) {
                slikaRepozitorij.save(any(), any(), Album::class.java)
            }

            testAlbum.naziv = stariNazivAlbuma
        }
    }

    @Nested
    inner class BrisanjeAlbuma {
        @Test
        fun `brisanje nepostojeceg albuma baca AlbumNijePronadenIznimka`() {
            val testAlbum = testAlbumi.first()

            every {
                albumRepozitorij.findByIdOrNull(testAlbum.id)
            } returns null

            assertThrows<AlbumNijePronadenIznimka> {
                albumServis.obrisi(testAlbum.id)
            }
        }

        @Test
        fun `brisanje tudeg albuma baca UmjetnikNijePronadenIznimka`() {
            SecurityContextHolder.getContext().authentication = TestingAuthenticationToken("elvis", null)
            val testAlbum = testAlbumi.first()

            every {
                albumRepozitorij.findByIdOrNull(testAlbum.id)
            } returns testAlbum

            assertThrows<UmjetnikNijePronadenIznimka> {
                albumServis.obrisi(testAlbum.id)
            }
        }

        @Test
        fun `brisanje albuma brise sve melodije koje se pojavljuju samo na tom albumu`() {
            val testAlbum = testAlbumi.first()
            val melodija1 = Melodija(
                id = 1,
                audio = "",
                autor = testUmjetnik,
                zanr = Zanr(naziv = "blues"),
                albumi = mutableSetOf(testAlbum, testAlbumi[1])
            )
            val melodija2 = Melodija(
                id = 2,
                audio = "",
                autor = testUmjetnik,
                zanr = Zanr(naziv = "blues"),
                albumi = mutableSetOf(testAlbum)
            )
            testAlbum.melodije.addAll(listOf(melodija1, melodija2))

            every {
                albumRepozitorij.findByIdOrNull(testAlbum.id)
            } returns testAlbum
            every {
                melodijaRepozitorij.deleteById(melodija2.id)
            } returns Unit
            every {
                albumRepozitorij.deleteById(testAlbum.id)
            } returns Unit

            albumServis.obrisi(testAlbum.id)
            verify(exactly = 1) {
                melodijaRepozitorij.deleteById(any())
                audioRepozitorij.delete(any(), Melodija::class.java)
                albumRepozitorij.deleteById(testAlbum.id)
            }

            testAlbum.melodije.clear()
        }

        @Test
        fun `brisanje albuma brise njegovu sliku`() {
            val testAlbum = testAlbumi.first()
            val staraSlikaAlbuma = testAlbum.slika

            val slikaAlbuma = "test.png"
            testAlbum.slika = slikaAlbuma

            every {
                albumRepozitorij.findByIdOrNull(testAlbum.id)
            } returns testAlbum
            every {
                albumRepozitorij.deleteById(testAlbum.id)
            } returns Unit

            albumServis.obrisi(testAlbum.id)
            verify(exactly = 1) {
                slikaRepozitorij.delete(slikaAlbuma, Album::class.java)
            }

            testAlbum.slika = staraSlikaAlbuma
        }
    }
}