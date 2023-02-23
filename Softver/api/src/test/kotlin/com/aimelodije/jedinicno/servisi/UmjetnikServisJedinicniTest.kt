package com.aimelodije.jedinicno.servisi

import com.aimelodije.iznimke.umjetnik.IlegalanUmjetnikIznimka
import com.aimelodije.iznimke.umjetnik.UmjetnikNijePronadenIznimka
import com.aimelodije.modeli.domena.Umjetnik
import com.aimelodije.modeli.enumeracije.Rola
import com.aimelodije.modeli.pogledi.UmjetnikPogled
import com.aimelodije.modeli.zahtjevi.UmjetnikAzuriranjeZahtjev
import com.aimelodije.repozitoriji.UmjetnikRepozitorij
import com.aimelodije.repozitoriji.datotecni.SlikaRepozitorij
import com.aimelodije.servisi.UmjetnikServis
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.repository.findByIdOrNull
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.Date

@ExtendWith(MockKExtension::class)
class UmjetnikServisJedinicniTest {

    @MockK
    lateinit var umjetnikRepozitorij: UmjetnikRepozitorij

    @MockK
    lateinit var enkoderLozinke: PasswordEncoder

    @MockK
    lateinit var slikaRepozitorij: SlikaRepozitorij

    @InjectMockKs
    lateinit var umjetnikServis: UmjetnikServis

    private val testUmjetnik = Umjetnik(
        id = 5,
        korime = "ttomasic",
        email = "ttomasic20@student.foi.hr",
        lozinka = "admin",
        datumRegistracije = Date(),
        rola = Rola.ADMINISTRATOR,
        albumi = mutableSetOf()
    )

    @BeforeEach
    fun setup() {
        SecurityContextHolder.getContext().authentication = TestingAuthenticationToken(testUmjetnik.korime, null)
        testUmjetnik.apply {
            email = "ttomasic20@student.foi.hr"
            lozinka = "admin"
            ime = "Tin"
            prezime = "Tomašić"
            opis = "Divan glazbeni život jednog bonvivana!"
            slika = "osobna.png"
        }
    }

    @Nested
    inner class DohvatJednogUmjetnika() {
        @Test
        fun `dohvacanje nepostojeceg umjetnika baca UmjetnikNijePronadenIznimka`() {
            every {
                umjetnikRepozitorij.findByIdOrNull(testUmjetnik.id + 1)
            } returns null

            assertThrows<UmjetnikNijePronadenIznimka> {
                umjetnikServis.dohvati(testUmjetnik.id + 1)
            }
        }

        @Test
        fun `dohvacanje umjetnika vraca umjetnika`() {
            every {
                umjetnikRepozitorij.findByIdOrNull(testUmjetnik.id)
            } returns testUmjetnik

            assertThat(umjetnikServis.dohvati(testUmjetnik.id))
                .isEqualTo(UmjetnikPogled(testUmjetnik))
        }
    }

    @Nested
    inner class AzuriranjeUmjetnika() {
        @Test
        fun `azuriranje nepostojeceg umjetnika baca UmjetnikNijePronadenIznimka`() {
            val umjetnikZahtjev = UmjetnikAzuriranjeZahtjev(id = testUmjetnik.id)

            every {
                umjetnikRepozitorij.findByIdOrNull(testUmjetnik.id)
            } returns null

            assertThrows<UmjetnikNijePronadenIznimka> {
                umjetnikServis.azuriraj(umjetnikZahtjev)
            }
        }

        @Test
        fun `azuriranje tudeg profila baca UmjetnikNijePronadenIznimka`() {
            SecurityContextHolder.getContext().authentication = TestingAuthenticationToken("elvis", null)
            val umjetnikZahtjev = UmjetnikAzuriranjeZahtjev(id = testUmjetnik.id)

            every {
                umjetnikRepozitorij.findByIdOrNull(testUmjetnik.id)
            } returns testUmjetnik

            assertThrows<UmjetnikNijePronadenIznimka> {
                umjetnikServis.azuriraj(umjetnikZahtjev)
            }
        }

        @Test
        fun `azuriranje email adrese na adresu koja vec postoji baca IlegalanUmjetnikIznimka`() {
            val umjetnikZahtjev = UmjetnikAzuriranjeZahtjev(
                id = testUmjetnik.id,
                email = testUmjetnik.email
            )

            every {
                umjetnikRepozitorij.findByIdOrNull(testUmjetnik.id)
            } returns testUmjetnik
            every {
                umjetnikRepozitorij.existsByEmailIgnoreCase(umjetnikZahtjev.email!!)
            } returns true

            assertThrows<IlegalanUmjetnikIznimka> {
                umjetnikServis.azuriraj(umjetnikZahtjev)
            }
            verify(exactly = 0) {
                umjetnikRepozitorij.save(any())
            }
        }

        @Test
        fun `azuriranje email adrese vraca umjetnika s azuriranom email adresom`() {
            val umjetnikZahtjev = UmjetnikAzuriranjeZahtjev(
                id = testUmjetnik.id,
                email = "ttomasic@foi.hr"
            )

            every {
                umjetnikRepozitorij.findByIdOrNull(testUmjetnik.id)
            } returns testUmjetnik
            every {
                umjetnikRepozitorij.existsByEmailIgnoreCase(umjetnikZahtjev.email!!)
            } returns false
            every {
                umjetnikRepozitorij.save(
                    testUmjetnik.apply { email = umjetnikZahtjev.email!! }
                )
            } returns testUmjetnik

            val umjetnikPogled = umjetnikServis.azuriraj(umjetnikZahtjev)

            assertThat(umjetnikPogled)
                .isEqualTo(UmjetnikPogled(testUmjetnik))
            assertThat(umjetnikPogled.email)
                .isEqualTo(umjetnikZahtjev.email)
            verify(exactly = 0) {
                slikaRepozitorij.save(any(), any(), Umjetnik::class.java)
            }
        }

        @Test
        fun `azuriranje slike umjetnika vraca umjetnika s azuriranim nazivom slike`() {
            val novaSlika = MockMultipartFile("novi-ja.jpeg", null)
            val umjetnikZahtjev = UmjetnikAzuriranjeZahtjev(
                id = testUmjetnik.id,
                slika = novaSlika
            )
            val generiraniNazivSlike = "99f9201e-b228-11ed-afa1-0242ac120002.jpeg"

            every {
                umjetnikRepozitorij.findByIdOrNull(testUmjetnik.id)
            } returns testUmjetnik
            every {
                slikaRepozitorij.save(novaSlika, testUmjetnik.slika, Umjetnik::class.java)
            } returns generiraniNazivSlike
            every {
                umjetnikRepozitorij.save(testUmjetnik)
            } returns testUmjetnik

            val umjetnikPogled = umjetnikServis.azuriraj(umjetnikZahtjev)
            assertThat(umjetnikPogled)
                .isEqualTo(UmjetnikPogled(testUmjetnik))
            assertThat(umjetnikPogled.slika)
                .isEqualTo(generiraniNazivSlike)
            verify(exactly = 0) {
                umjetnikRepozitorij.existsByEmailIgnoreCase(any())
            }
        }
    }
}