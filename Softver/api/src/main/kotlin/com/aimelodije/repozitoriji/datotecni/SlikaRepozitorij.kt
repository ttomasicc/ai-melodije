package com.aimelodije.repozitoriji.datotecni

import com.aimelodije.modeli.domena.Album
import com.aimelodije.modeli.domena.Umjetnik
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import java.nio.file.Path
import java.util.*
import javax.imageio.ImageIO
import kotlin.io.path.name
import kotlin.math.min

@Repository
class SlikaRepozitorij(
    @Value("\${spring.web.resources.static-locations}")
    dir: String
) : DatotecniRepozitorij(dir) {

    override fun <T : Any> save(file: MultipartFile, existingFileName: String?, type: Class<T>): String {
        existingFileName?.run { delete(this, type) }
        return saveImage(file, getPath(type))
    }

    override fun <T : Any> getPath(type: Class<T>): Path =
        when (type) {
            Umjetnik::class.java -> UMJETNICI_PATH
            Album::class.java -> ALBUMI_PATH
            else -> throw IllegalArgumentException("Nepodržani tip")
        }

    private fun saveImage(image: MultipartFile, path: Path): String {
        val extension = getExtension(image)
        val squareImage = cropImage(ImageIO.read(image.inputStream))
        val outDir = path.resolve("${UUID.randomUUID()}.$extension")

        ImageIO.write(squareImage, extension, outDir.toFile())

        return outDir.fileName.name
    }

    private fun cropImage(image: BufferedImage): BufferedImage {
        val height = image.height
        val width = image.width

        return if (height == width)
            image // Slika je već kvadratna
        else {
            // Maksimalna duljina slike - ograničeno manjom "stranicom" slike
            val maxLength = min(height, width)

            // Pomiče platno u središte slike - dulja se stranica slike smanjuje na maksimalnu duljinu
            var (xOffset, yOffset) = 0 to 0
            if (height == maxLength)
                xOffset = (width - maxLength) / 2
            else
                yOffset = (height - maxLength) / 2

            image.getSubimage(xOffset, yOffset, maxLength, maxLength)
        }
    }
}