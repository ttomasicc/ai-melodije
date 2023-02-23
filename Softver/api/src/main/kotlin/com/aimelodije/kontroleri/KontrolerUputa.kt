package com.aimelodije.kontroleri.upute

import mu.KLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.sql.SQLException
import java.time.Instant
import java.time.ZoneOffset

/**
 * Uputa kontroleru - hvata sve nepredviđene SQL iznimke
 */
@RestControllerAdvice
class KontrolerUputa {

    companion object : KLogging()

    /**
     * Hvata sve nepredviđene SQL iznimke
     *
     * @param sqlIznimka generalna nepredviđena SQL iznimka koja će se ovdje uhvatiti
     * @return [ResponseEntity] obavještava krajnjeg korisnika da je nešto pošlo po zlu na našoj strani (server)
     */
    @ExceptionHandler
    fun handleDatabaseExceptions(sqlIznimka: SQLException): ResponseEntity<String> {
        logger.warn { "Kontroler Uputa je uhvatila nepredviđenu SQL iznimku: $sqlIznimka" }

        return ResponseEntity(
            """{
                "timestamp": "${Instant.now().atOffset(ZoneOffset.UTC)}",
                "status": 500,
                "error": "Internal Server Error",
                "message": "Nepredviđena greška: molimo pokušajte ponovno kasnije ili kontaktirajte podršku."
                }
            """.trimIndent(),
            HttpHeaders().apply {
                contentType = MediaType.APPLICATION_JSON
            },
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }
}