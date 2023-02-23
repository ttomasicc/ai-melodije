package com.aimelodije.jedinicno.servisi

import com.aimelodije.iznimke.album.AlbumNijePronadenIznimka
import com.aimelodije.iznimke.melodija.MelodijaNijePronadenaIznimka
import com.aimelodije.iznimke.umjetnik.UmjetnikNijePronadenIznimka
import com.aimelodije.iznimke.zanr.ZanrNijePronadenIznimka
import com.aimelodije.modeli.domena.Album
import com.aimelodije.modeli.domena.Melodija
import com.aimelodije.modeli.domena.Umjetnik
import com.aimelodije.modeli.domena.Zanr
import com.aimelodije.modeli.enumeracije.Rola
import com.aimelodije.modeli.pogledi.MelodijaPogled
import com.aimelodije.modeli.zahtjevi.MelodijaAzuriranjeZahtjev
import com.aimelodije.modeli.zahtjevi.MelodijaDodavanjeZahtjev
import com.aimelodije.repozitoriji.MelodijaRepozitorij
import com.aimelodije.repozitoriji.UmjetnikRepozitorij
import com.aimelodije.repozitoriji.ZanrRepozitorij
import com.aimelodije.repozitoriji.datotecni.AudioRepozitorij
import com.aimelodije.servisi.MelodijaServis
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

@ExtendWith(MockKExtension::class)
class MelodijaServisJedinicniTest {

    @MockK
    lateinit var umjetnikRepozitorij: UmjetnikRepozitorij

    @MockK
    lateinit var melodijaRepozitorij: MelodijaRepozitorij

    @MockK
    lateinit var zanrRepozitorij: ZanrRepozitorij

    @MockK(relaxed = true)
    lateinit var audioRepozitorij: AudioRepozitorij

    @InjectMockKs
    lateinit var melodijaServis: MelodijaServis

    private val testUmjetnik = Umjetnik(
        id = 5,
        korime = "ttomasic",
        email = "ttomasic20@student.foi.hr",
        lozinka = "admin",
        rola = Rola.ADMINISTRATOR,
    )

    private val testZanrovi = listOf(
        Zanr(id = 1, naziv = "blues"),
        Zanr(id = 2, naziv = "jazz")
    )

    private val testMelodije = listOf(
        Melodija(
            id = 1,
            audio = "sanjalica.wav",
            naziv = "AI sanjar",
            autor = testUmjetnik,
            zanr = testZanrovi[0],
        ),
        Melodija(
            id = 2,
            audio = "kvant.ogg",
            naziv = "Mrežni valovi",
            autor = testUmjetnik,
            zanr = testZanrovi[1],
        ),
        Melodija(
            id = 3,
            audio = "Algoritamska sonata.mp3",
            naziv = "Algoritamska sonata",
            autor = testUmjetnik,
            zanr = testZanrovi[0],
        ),
        Melodija(
            id = 4,
            audio = "mv.mp3",
            naziv = "Mehanički valcer",
            autor = testUmjetnik,
            zanr = testZanrovi[1],
        )
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
        testAlbumi.forEach { album ->
            album.melodije.add(testMelodije[album.id.toInt() - 1])
        }
        testUmjetnik.albumi.addAll(testAlbumi)
    }

    @AfterEach
    fun teardown() {
        testAlbumi.forEach { it.melodije.clear() }
        testUmjetnik.albumi.clear()
    }

    @Nested
    inner class DohvaSvihMelodija() {
        @Test
        fun `dohvacanje svih melodija albuma nepostojeceg umjetnika baca UmjetnikNijePronadenIznimka`() {
            every {
                umjetnikRepozitorij.findByIdOrNull(testUmjetnik.id)
            } returns null

            assertThrows<UmjetnikNijePronadenIznimka> {
                melodijaServis.dohvatiSve(testUmjetnik.id, testAlbumi.last().id)
            }
        }

        @Test
        fun `dohvacanje svih melodija nepostojeceg albuma danog umjetnika baca AlbumNijePronadenIznimka`() {
            every {
                umjetnikRepozitorij.findByIdOrNull(testUmjetnik.id)
            } returns testUmjetnik

            assertThrows<AlbumNijePronadenIznimka> {
                melodijaServis.dohvatiSve(testUmjetnik.id, testUmjetnik.albumi.last().id + 1)
            }
        }

        @Test
        fun `dohvacanje svih melodija albuma danog umjetnika vraca listu svih melodija`() {
            every {
                umjetnikRepozitorij.findByIdOrNull(testUmjetnik.id)
            } returns testUmjetnik

            assertThat(melodijaServis.dohvatiSve(testUmjetnik.id, testAlbumi.first().id))
                .isEqualTo(testAlbumi.first().melodije.map { MelodijaPogled(it) })
        }

        @Test
        fun `dohvacanje svih stranicenih melodija vraca stranicu s popisom melodija`() {
            val stranica = PageImpl(testMelodije)

            every {
                melodijaRepozitorij.findAll(Pageable.unpaged())
            } returns stranica

            assertThat(melodijaServis.dohvatiSve(Pageable.unpaged()))
                .isEqualTo(stranica.map { MelodijaPogled(it) })
        }
    }

    @Nested
    inner class PretrazivanjeMelodija() {
        @Test
        fun `pretrazivanje melodija vraca stranicu melodija ciji nazivi sadrze kljucnu rijec`() {
            val kljucnaRijec = "val"
            val stranica = PageImpl(testMelodije.filter { it.naziv.contains(kljucnaRijec) })

            every {
                melodijaRepozitorij.findAllByNazivContainsIgnoreCase(kljucnaRijec, Pageable.unpaged())
            } returns stranica

            assertThat(melodijaServis.pretrazuj(kljucnaRijec, Pageable.unpaged()))
                .isEqualTo(stranica.map { MelodijaPogled(it) })
        }
    }

    @Nested
    inner class DohvatJedneMelodije {
        @Test
        fun `dohvacanje melodije nepostojeceg umjetnika baca UmjetnikNijePronadenIznimka`() {
            every {
                umjetnikRepozitorij.findByIdOrNull(testUmjetnik.id)
            } returns null

            assertThrows<UmjetnikNijePronadenIznimka> {
                melodijaServis.dohvati(testUmjetnik.id, testAlbumi.last().id, testMelodije.first().id)
            }
        }

        @Test
        fun `dohvacanje melodije nepostojeceg albuma danog umjetnika baca AlbumNijePronadenIznimka`() {
            every {
                umjetnikRepozitorij.findByIdOrNull(testUmjetnik.id)
            } returns testUmjetnik

            assertThrows<AlbumNijePronadenIznimka> {
                melodijaServis.dohvati(testUmjetnik.id, testUmjetnik.albumi.last().id + 1, testMelodije.first().id)
            }
        }

        @Test
        fun `dohvacanje nepostojece melodije danog albuma danog umjetnika baca MelodijaNijePronadenIznimka`() {
            every {
                umjetnikRepozitorij.findByIdOrNull(testUmjetnik.id)
            } returns testUmjetnik

            assertThrows<MelodijaNijePronadenaIznimka> {
                melodijaServis.dohvati(
                    testUmjetnik.id,
                    testUmjetnik.albumi.last().id,
                    testUmjetnik.albumi.last().melodije.last().id + 1
                )
            }
        }

        @Test
        fun `dohvacanje melodije danog albuma danog umjetnika vraca melodiju`() {
            every {
                umjetnikRepozitorij.findByIdOrNull(testUmjetnik.id)
            } returns testUmjetnik

            val melodija = melodijaServis.dohvati(
                testUmjetnik.id,
                testUmjetnik.albumi.last().id,
                testUmjetnik.albumi.last().melodije.first().id
            )
            assertThat(melodija)
                .isEqualTo(MelodijaPogled(testUmjetnik.albumi.last().melodije.first()))
        }
    }

    @Nested
    inner class DodavanjeMelodije() {
        @Test
        fun `dodavanje melodije nepostojecem umjetniku baca UmjetnikNijePronadenIznimka`() {
            val melodijaZahtjev = MelodijaDodavanjeZahtjev(
                umjetnikId = testUmjetnik.id
            )

            every {
                umjetnikRepozitorij.findByIdOrNull(melodijaZahtjev.umjetnikId)
            } returns null

            assertThrows<UmjetnikNijePronadenIznimka> {
                melodijaServis.dodaj(melodijaZahtjev)
            }
            verify(exactly = 0) {
                melodijaRepozitorij.save(any())
            }
        }

        @Test
        fun `dodavanje melodije tudem umjetniku baca UmjetnikNijePronadenIznimka`() {
            SecurityContextHolder.getContext().authentication = TestingAuthenticationToken("elvis", null)
            val melodijaZahtjev = MelodijaDodavanjeZahtjev(
                umjetnikId = testUmjetnik.id
            )

            every {
                umjetnikRepozitorij.findByIdOrNull(melodijaZahtjev.umjetnikId)
            } returns testUmjetnik

            assertThrows<UmjetnikNijePronadenIznimka> {
                melodijaServis.dodaj(melodijaZahtjev)
            }
            verify(exactly = 0) {
                melodijaRepozitorij.save(any())
            }
        }

        @Test
        fun `dodavanje melodije u nepostojeci album baca AlbumNijePronadenIznimka`() {
            val melodijaZahtjev = MelodijaDodavanjeZahtjev(
                umjetnikId = testUmjetnik.id,
                albumId = testUmjetnik.albumi.last().id + 1
            )

            every {
                umjetnikRepozitorij.findByIdOrNull(melodijaZahtjev.umjetnikId)
            } returns testUmjetnik

            assertThrows<AlbumNijePronadenIznimka> {
                melodijaServis.dodaj(melodijaZahtjev)
            }
            verify(exactly = 0) {
                melodijaRepozitorij.save(any())
            }
        }

        @Test
        fun `dodavanje melodije za nepostojeci zanr baca ZanrNijePronadenIznimka`() {
            val melodijaZahtjev = MelodijaDodavanjeZahtjev(
                umjetnikId = testUmjetnik.id,
                albumId = testUmjetnik.albumi.last().id,
                zanrId = testZanrovi.last().id + 1
            )

            every {
                umjetnikRepozitorij.findByIdOrNull(melodijaZahtjev.umjetnikId)
            } returns testUmjetnik
            every {
                zanrRepozitorij.findByIdOrNull(melodijaZahtjev.zanrId)
            } returns null

            assertThrows<ZanrNijePronadenIznimka> {
                melodijaServis.dodaj(melodijaZahtjev)
            }
            verify(exactly = 0) {
                melodijaRepozitorij.save(any())
            }
        }

        @Test
        fun `dodavanje melodije u dani album danog umjetnika vraca melodiju koja pripada danom albumu`() {
            val melodijaZahtjev = MelodijaDodavanjeZahtjev(
                umjetnikId = testUmjetnik.id,
                albumId = testUmjetnik.albumi.last().id,
                audio = MockMultipartFile("syntth.wav", null),
                zanrId = testZanrovi.last().id,
                naziv = "Sinetički šapat"
            )
            val melodijaDomena = Melodija(
                zanr = testZanrovi.last(),
                audio = "syntth.wav",
                autor = testUmjetnik,
                albumi = mutableSetOf(testUmjetnik.albumi.last())
            )

            every {
                umjetnikRepozitorij.findByIdOrNull(melodijaZahtjev.umjetnikId)
            } returns testUmjetnik
            every {
                zanrRepozitorij.findByIdOrNull(melodijaZahtjev.zanrId)
            } returns testZanrovi.last()
            every {
                melodijaRepozitorij.save(any())
            } returns melodijaDomena

            val melodijaPogled = melodijaServis.dodaj(melodijaZahtjev)
            assertThat(melodijaPogled)
                .isEqualTo(MelodijaPogled(melodijaDomena))
            assertThat(melodijaPogled.albumi.size)
                .isEqualTo(1L)
            assertThat(testUmjetnik.albumi.last().melodije.size)
                .isEqualTo(2L)
            verify(exactly = 1) {
                melodijaRepozitorij.save(any())
            }
        }
    }

    @Nested
    inner class AzuriranjeMelodije() {
        @Test
        fun `azuriranje melodije nepostojeceg umjetnika baca UmjetnikNijePronadenIznimka`() {
            val melodijaZahtjev = MelodijaAzuriranjeZahtjev(
                umjetnikId = testUmjetnik.id
            )

            every {
                umjetnikRepozitorij.findByIdOrNull(melodijaZahtjev.umjetnikId)
            } returns null

            assertThrows<UmjetnikNijePronadenIznimka> {
                melodijaServis.azuriraj(melodijaZahtjev)
            }
            verify(exactly = 0) {
                melodijaRepozitorij.save(any())
            }
        }

        @Test
        fun `azuriranje melodije tudeg albuma baca UmjetnikNijePronadenIznimka`() {
            SecurityContextHolder.getContext().authentication = TestingAuthenticationToken("elvis", null)
            val melodijaZahtjev = MelodijaAzuriranjeZahtjev(
                umjetnikId = testUmjetnik.id
            )

            every {
                umjetnikRepozitorij.findByIdOrNull(melodijaZahtjev.umjetnikId)
            } returns testUmjetnik

            assertThrows<UmjetnikNijePronadenIznimka> {
                melodijaServis.azuriraj(melodijaZahtjev)
            }
            verify(exactly = 0) {
                melodijaRepozitorij.save(any())
            }
        }

        @Test
        fun `azuriranje melodije nepostojeceg albuma baca AlbumNijePronadenIznimka`() {
            val melodijaZahtjev = MelodijaAzuriranjeZahtjev(
                umjetnikId = testUmjetnik.id,
                albumId = testUmjetnik.albumi.last().id + 1
            )

            every {
                umjetnikRepozitorij.findByIdOrNull(melodijaZahtjev.umjetnikId)
            } returns testUmjetnik

            assertThrows<AlbumNijePronadenIznimka> {
                melodijaServis.azuriraj(melodijaZahtjev)
            }
            verify(exactly = 0) {
                melodijaRepozitorij.save(any())
            }
        }

        @Test
        fun `azuriranje nepostojece melodije baca AlbumNijePronadenIznimka`() {
            val melodijaZahtjev = MelodijaAzuriranjeZahtjev(
                umjetnikId = testUmjetnik.id,
                albumId = testUmjetnik.albumi.last().id,
                melodijaId = testUmjetnik.albumi.last().melodije.last().id + 1
            )

            every {
                umjetnikRepozitorij.findByIdOrNull(melodijaZahtjev.umjetnikId)
            } returns testUmjetnik

            assertThrows<MelodijaNijePronadenaIznimka> {
                melodijaServis.azuriraj(melodijaZahtjev)
            }
            verify(exactly = 0) {
                melodijaRepozitorij.save(any())
            }
        }

        @Test
        fun `azuriranje melodije vraca melodiju s azuriranim nazivom`() {
            val testMelodija = testUmjetnik.albumi.last().melodije.last()
            val stariNaziv = testMelodija.naziv
            val noviNaziv = "Retro bitovi"
            val melodijaZahtjev = MelodijaAzuriranjeZahtjev(
                umjetnikId = testUmjetnik.id,
                albumId = testUmjetnik.albumi.last().id,
                melodijaId = testMelodija.id,
                naziv = noviNaziv
            )

            every {
                umjetnikRepozitorij.findByIdOrNull(melodijaZahtjev.umjetnikId)
            } returns testUmjetnik
            every {
                melodijaRepozitorij.save(testMelodija)
            } returns testMelodija

            val melodijaPogled = melodijaServis.azuriraj(melodijaZahtjev)
            assertThat(melodijaPogled)
                .isEqualTo(MelodijaPogled(testMelodija))
            assertThat(melodijaPogled.naziv)
                .isEqualTo(noviNaziv)
            verify(exactly = 1) {
                melodijaRepozitorij.save(any())
            }

            testMelodija.naziv = stariNaziv
        }
    }

    @Nested
    inner class BrisanjeMelodije() {
        @Test
        fun `brisanje melodije nepostojeceg umjetnika baca UmjetnikNijePronadenIznimka`() {
            every {
                umjetnikRepozitorij.findByIdOrNull(testUmjetnik.id)
            } returns null

            assertThrows<UmjetnikNijePronadenIznimka> {
                melodijaServis.obrisi(
                    testUmjetnik.id,
                    testUmjetnik.albumi.last().id,
                    testUmjetnik.albumi.last().melodije.last().id
                )
            }
            verify(exactly = 0) {
                audioRepozitorij.delete(any(), Melodija::class.java)
                melodijaRepozitorij.delete(any())
            }
        }

        @Test
        fun `brisanje melodije tudeg albuma baca UmjetnikNijePronadenIznimka`() {
            SecurityContextHolder.getContext().authentication = TestingAuthenticationToken("elvis", null)

            every {
                umjetnikRepozitorij.findByIdOrNull(testUmjetnik.id)
            } returns testUmjetnik

            assertThrows<UmjetnikNijePronadenIznimka> {
                melodijaServis.obrisi(
                    testUmjetnik.id,
                    testUmjetnik.albumi.last().id,
                    testUmjetnik.albumi.last().melodije.last().id
                )
            }
            verify(exactly = 0) {
                audioRepozitorij.delete(any(), Melodija::class.java)
                melodijaRepozitorij.delete(any())
            }
        }

        @Test
        fun `brisanje melodije nepostojeceg albuma baca AlbumNijePronadenIznimka`() {
            every {
                umjetnikRepozitorij.findByIdOrNull(testUmjetnik.id)
            } returns testUmjetnik

            assertThrows<AlbumNijePronadenIznimka> {
                melodijaServis.obrisi(
                    testUmjetnik.id,
                    testUmjetnik.albumi.last().id + 1,
                    testUmjetnik.albumi.last().melodije.last().id
                )
            }
            verify(exactly = 0) {
                audioRepozitorij.delete(any(), Melodija::class.java)
                melodijaRepozitorij.delete(any())
            }
        }

        @Test
        fun `brisanje nepostojece melodije baca AlbumNijePronadenIznimka`() {
            every {
                umjetnikRepozitorij.findByIdOrNull(testUmjetnik.id)
            } returns testUmjetnik

            assertThrows<MelodijaNijePronadenaIznimka> {
                melodijaServis.obrisi(
                    testUmjetnik.id,
                    testUmjetnik.albumi.last().id,
                    testUmjetnik.albumi.last().melodije.last().id + 1
                )
            }
            verify(exactly = 0) {
                audioRepozitorij.delete(any(), Melodija::class.java)
                melodijaRepozitorij.delete(any())
            }
        }

        @Test
        fun `brisanje melodije uspjesno brise melodiju sa svih albuma, zajedno sa audio datotekom`() {
            every {
                umjetnikRepozitorij.findByIdOrNull(testUmjetnik.id)
            } returns testUmjetnik
            every {
                melodijaRepozitorij.delete(testUmjetnik.albumi.last().melodije.last())
            } returns Unit

            melodijaServis.obrisi(
                testUmjetnik.id,
                testUmjetnik.albumi.last().id,
                testUmjetnik.albumi.last().melodije.last().id
            )

            verify(exactly = 1) {
                audioRepozitorij.delete(any(), Melodija::class.java)
                melodijaRepozitorij.delete(any())
            }
        }
    }
}