package com.aimelodije.jedinicno.repozitoriji

import com.aimelodije.modeli.domena.Album
import com.aimelodije.modeli.domena.Umjetnik
import com.aimelodije.modeli.pogledi.MelodijaPogled
import com.aimelodije.repozitoriji.datotecni.SlikaRepozitorij
import org.assertj.core.api.Assertions.assertThat
import org.hibernate.validator.internal.util.Contracts.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.mock.web.MockMultipartFile
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class SlikaRepozitorijJedinicno {

    val testDir = "file:/tmp/spring-test/"
    val slikaRepozitorij = SlikaRepozitorij(testDir)
    val UMJETNICI_PATH = Path.of(testDir.split(":")[1], "umjetnici")
    val ALBUMI_PATH = Path.of(testDir.split(":")[1], "albumi")

    val testSlikaDatoteka = MockMultipartFile(
        "98hfs-39dskl-asd81s-12fsa.jpeg",
        "luna.jpeg",
        "image/jpeg",
        FileInputStream(Paths.get("src", "test", "resources", "static", "luna.jpeg").toFile())
    )

    @Test
    fun `repozitorij slika uspjesno je instanciran`() {
        assertNotNull(slikaRepozitorij)
    }

    @Test
    fun `spremanje nepodrzanog tipa baca IllegalArgumentException`() {
        assertThrows<IllegalArgumentException> {
            slikaRepozitorij.save(
                testSlikaDatoteka,
                null,
                MelodijaPogled::class.java
            )
        }
    }

    @Test
    fun `spremanje slike umjetnika vraca generirani naziv datoteke koji se moze obrisati`() {
        val generiraniNaziv = slikaRepozitorij.save(
            testSlikaDatoteka,
            "test.jpg",
            Umjetnik::class.java
        )
        assertNotNull(generiraniNaziv)
        assertThat(
            Files.exists(UMJETNICI_PATH.resolve(generiraniNaziv))
        ).isTrue
        assertThat(
            slikaRepozitorij.delete(generiraniNaziv, Umjetnik::class.java)
        ).isTrue
    }

    @Test
    fun `spremanje slike albuma vraca generirani naziv datoteke koji se moze obrisati`() {
        val generiraniNaziv = slikaRepozitorij.save(
            testSlikaDatoteka,
            "test2.jpg",
            Album::class.java
        )
        assertNotNull(generiraniNaziv)
        assertThat(
            Files.exists(ALBUMI_PATH.resolve(generiraniNaziv))
        ).isTrue
        assertThat(
            slikaRepozitorij.delete(generiraniNaziv, Album::class.java)
        ).isTrue
    }
}