package com.aimelodije.konfiguracije.postavke

import jakarta.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.validation.annotation.Validated

@Validated
@ConfigurationProperties(prefix = "app.spotify")
data class SpotifyPostavke @ConstructorBinding constructor(
    @field:NotBlank(message = "Nedostaje Spotify client secret")
    val baseUrl: String,
    @field:NotBlank(message = "Nedostaje Spotify client ID")
    val clientId: String,
    @field:NotBlank(message = "Nedostaje Spotify client secret")
    val clientSecret: String
)