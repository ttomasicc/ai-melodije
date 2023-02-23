package com.aimelodije.jedinicno.repozitoriji

import com.aimelodije.modeli.domena.Melodija
import com.aimelodije.modeli.pogledi.MelodijaPogled
import com.aimelodije.repozitoriji.datotecni.AudioRepozitorij
import org.assertj.core.api.Assertions.assertThat
import org.hibernate.validator.internal.util.Contracts.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.mock.web.MockMultipartFile
import java.nio.file.Files
import java.nio.file.Path

class AudioRepozitorijJedinicniTest {

    val testDir = "file:/tmp/spring-test/"
    val audioRepozitorij = AudioRepozitorij(testDir)
    val MELODIJE_PATH = Path.of(testDir.split(":")[1], "melodije")

    val testAudioDatoteka = MockMultipartFile(
        "87213-1289uasd-9182qaslkm-12asd.mp3",
        "umjetna inteligencija u e-molu.mp3",
        "audio/mpeg",
        byteArrayOf(73, 68, 51, 4, 0, 32, 4, 64)
    )

    @Test
    fun `audio repozitorij uspjesno je instanciran`() {
        assertNotNull(audioRepozitorij)
    }

    @Test
    fun `spremanje nepodržanog tipa baca IllegalArgumentException`() {
        assertThrows<IllegalArgumentException> {
            audioRepozitorij.save(
                testAudioDatoteka,
                null,
                MelodijaPogled::class.java
            )
        }
    }

    @Test
    fun `spremanje audio datoteke vraca generirani naziv datoteke koji se moze obrisati`() {
        val generiraniNaziv = audioRepozitorij.save(
            testAudioDatoteka,
            "test.wav",
            Melodija::class.java
        )
        assertNotNull(generiraniNaziv)
        assertThat(
            Files.exists(MELODIJE_PATH.resolve(generiraniNaziv))
        ).isTrue
        assertThat(
            audioRepozitorij.delete(generiraniNaziv, Melodija::class.java)
        ).isTrue
    }
}