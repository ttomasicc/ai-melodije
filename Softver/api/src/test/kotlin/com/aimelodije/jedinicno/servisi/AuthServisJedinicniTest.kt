package com.aimelodije.jedinicno.servisi

import com.aimelodije.iznimke.umjetnik.IlegalanUmjetnik
import com.aimelodije.iznimke.umjetnik.IlegalanUmjetnikIznimka
import com.aimelodije.modeli.domena.Umjetnik
import com.aimelodije.modeli.enumeracije.Rola
import com.aimelodije.modeli.pogledi.UmjetnikPogled
import com.aimelodije.modeli.zahtjevi.UmjetnikPrijavaZahtjev
import com.aimelodije.modeli.zahtjevi.UmjetnikRegistracijaZahtjev
import com.aimelodije.repozitoriji.UmjetnikRepozitorij
import com.aimelodije.servisi.AuthServis
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
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder

@ExtendWith(MockKExtension::class)
class AuthServisJedinicniTest {

    @MockK
    lateinit var umjetnikRepozitorij: UmjetnikRepozitorij

    @MockK
    lateinit var authMenadzer: AuthenticationManager

    @MockK(relaxed = true)
    lateinit var enkoderLozinke: PasswordEncoder

    @InjectMockKs
    lateinit var authServis: AuthServis

    @BeforeEach
    fun setup() {
        SecurityContextHolder.getContext().authentication = TestingAuthenticationToken("ttomasic", "admin")
    }

    @Nested
    inner class Registracija {
        @Test
        fun `registracija pod vec postojecim imenom baca IlegalanUmjetnikIznimka`() {
            val registracijaZahtjev = UmjetnikRegistracijaZahtjev(
                korime = "ttomasic"
            )

            every {
                umjetnikRepozitorij.existsByKorimeIgnoreCase(registracijaZahtjev.korime)
            } returns true

            val iznimka = assertThrows<IlegalanUmjetnikIznimka> {
                authServis.registriraj(registracijaZahtjev)
            }
            assertThat(iznimka.message)
                .isEqualTo(IlegalanUmjetnik.POSTOJECE_KORISNICKO_IME.poruka)
            verify(exactly = 0) {
                umjetnikRepozitorij.save(any())
            }
        }

        @Test
        fun `registracija s vec postojecom email adresom baca IlegalanUmjetnikIznimka`() {
            val registracijaZahtjev = UmjetnikRegistracijaZahtjev(
                korime = "ttomasic",
                email = "ttomasic20@student.foi.hr"
            )

            every {
                umjetnikRepozitorij.existsByKorimeIgnoreCase(registracijaZahtjev.korime)
            } returns false
            every {
                umjetnikRepozitorij.existsByEmailIgnoreCase(registracijaZahtjev.email)
            } returns true

            val iznimka = assertThrows<IlegalanUmjetnikIznimka> {
                authServis.registriraj(registracijaZahtjev)
            }
            assertThat(iznimka.message)
                .isEqualTo(IlegalanUmjetnik.POSTOJECI_EMAIL.poruka)
            verify(exactly = 0) {
                umjetnikRepozitorij.save(any())
            }
        }

        @Test
        fun `uspjesna registracija vraca kreiranog umjetnika s rolom umjetnika`() {
            val registracijaZahtjev = UmjetnikRegistracijaZahtjev(
                korime = "ttomasic",
                email = "ttomasic20@student.foi.hr",
                lozinka = "admin"
            )
            val umjetnik = Umjetnik(
                korime = registracijaZahtjev.korime,
                email = registracijaZahtjev.email,
                lozinka = enkoderLozinke.encode(registracijaZahtjev.lozinka),
                rola = Rola.UMJETNIK
            )

            every {
                umjetnikRepozitorij.existsByKorimeIgnoreCase(registracijaZahtjev.korime)
            } returns false
            every {
                umjetnikRepozitorij.existsByEmailIgnoreCase(registracijaZahtjev.email)
            } returns false
            every {
                umjetnikRepozitorij.save(any())
            } returns umjetnik

            val umjetnikPogled = authServis.registriraj(registracijaZahtjev)
            assertThat(umjetnikPogled)
                .isEqualTo(UmjetnikPogled(umjetnik))
            assertThat(umjetnikPogled.rola)
                .isEqualTo(Rola.UMJETNIK.toString())
            verify(exactly = 1) {
                umjetnikRepozitorij.save(any())
            }
        }
    }

    @Nested
    inner class Prijava {
        @Test
        fun `prijava s pogresnim korisnickim imenom baca AuthenticationException`() {
            val prijavaZahtjev = UmjetnikPrijavaZahtjev(
                korime = "elvis",
                lozinka = "admin"
            )
            val authToken = TestingAuthenticationToken(
                prijavaZahtjev.korime,
                prijavaZahtjev.lozinka
            )

            every {
                authMenadzer.authenticate(authToken)
            } throws UsernameNotFoundException("")

            assertThrows<AuthenticationException> {
                authServis.prijavi(prijavaZahtjev)
            }
        }

        @Test
        fun `uspjesna prijava vraca autentificiranog umjetnika`() {
            val prijavaZahtjev = UmjetnikPrijavaZahtjev(
                korime = "ttomasic",
                lozinka = "admin"
            )
            val authToken = TestingAuthenticationToken(
                prijavaZahtjev.korime,
                prijavaZahtjev.lozinka
            )
            val umjetnik = Umjetnik(
                korime = prijavaZahtjev.korime,
                email = prijavaZahtjev.korime,
                lozinka = enkoderLozinke.encode(prijavaZahtjev.lozinka),
                rola = Rola.UMJETNIK
            )

            every {
                authMenadzer.authenticate(authToken)
            } returns authToken
            every {
                umjetnikRepozitorij.findByKorimeIgnoreCase(prijavaZahtjev.korime)
            } returns umjetnik

            assertThat(authServis.prijavi(prijavaZahtjev))
                .isEqualTo(umjetnik)
        }
    }
}