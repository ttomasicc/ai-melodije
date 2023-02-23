package com.aimelodije.repozitoriji.datotecni

import mu.KLogging
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path

sealed class DatotecniRepozitorij(dir: String) {

    protected val UMJETNICI_PATH: Path = Path.of(dir.split(":")[1], "umjetnici")
    protected val ALBUMI_PATH: Path = Path.of(dir.split(":")[1], "albumi")
    protected val MELODIJE_PATH: Path = Path.of(dir.split(":")[1], "melodije")

    companion object : KLogging()

    init {
        if (Files.notExists(UMJETNICI_PATH)) {
            Files.createDirectories(UMJETNICI_PATH)
            Files.createDirectories(ALBUMI_PATH)
            Files.createDirectories(MELODIJE_PATH)
            logger.info { "Uspješno kreirani podatkovni direktoriji" }
        }
    }

    abstract fun <T : Any> save(file: MultipartFile, existingFileName: String? = null, type: Class<T>): String

    @Throws(IllegalArgumentException::class)
    protected abstract fun <T : Any> getPath(type: Class<T>): Path

    fun <T : Any> delete(fileName: String, type: Class<T>): Boolean =
        Files.deleteIfExists(getPath(type).resolve(fileName))

    protected fun getExtension(file: MultipartFile): String =
        StringUtils.getFilenameExtension(file.originalFilename) ?: ""
}