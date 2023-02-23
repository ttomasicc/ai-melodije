package com.aimelodije.zadaci

import com.aimelodije.konfiguracije.postavke.SpotifyPostavke
import com.aimelodije.modeli.odgovori.SpotifyAPITokenOdgovor
import com.aimelodije.modeli.odgovori.SpotifyAPIZanroviOdgovor
import com.aimelodije.repozitoriji.ZanrRepozitorij
import java.util.Base64
import mu.KLogging
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.body
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

@Service
class ZanroviZadatak(
    private val webKlijent: WebClient,
    private val spotifyPostavke: SpotifyPostavke,
    private val zanrRepozitorij: ZanrRepozitorij
) {

    companion object : KLogging()

    // Svaki dan u ponoć
    @Scheduled(cron = "0 0 0 * * *")
    // Sigurnost u slučaju da se komunikacije između više instanci aplikacije prekine na nekoliko minuta
    @SchedulerLock(name = "zanrovi-azuriranje", lockAtLeastFor = "5m")
    @Transactional
    fun azurirajZanrove() {
        logger.info { "Ažuriram žanrove u bazi" }

        dohvatiZanrove().subscribe { apiOdgovor ->
            apiOdgovor.zanrovi.forEach {
                zanrRepozitorij.upsert(it)
                logger.info { "Dodajem žanr: $it" }
            }
            logger.info { "Ažuriranje žanrova u bazi završilo" }
        }
    }

    fun dohvatiZanrove(): Mono<SpotifyAPIZanroviOdgovor> =
        dohvatiSpotifyToken().flatMap {
            logger.info { "Dobiveni token: ${it.token}" }
            webKlijent
                .get()
                .uri("${spotifyPostavke.baseUrl}/recommendations/available-genre-seeds")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer ${it.token}")
                .retrieve()
                .bodyToMono()
        }

    fun dohvatiSpotifyToken(): Mono<SpotifyAPITokenOdgovor> =
        webKlijent
            .post()
            .uri("https://accounts.spotify.com/api/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header(
                HttpHeaders.AUTHORIZATION,
                "Basic ${
                Base64.getEncoder().encodeToString(
                    "${spotifyPostavke.clientId}:${spotifyPostavke.clientSecret}".toByteArray(Charsets.UTF_8)
                )
                }"
            )
            .body(Mono.just("grant_type=client_credentials"))
            .retrieve()
            .bodyToMono()
}