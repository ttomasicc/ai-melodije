package com.aimelodije.modeli.odgovori

import com.fasterxml.jackson.annotation.JsonProperty

data class SpotifyAPITokenOdgovor(
    @JsonProperty("access_token")
    val token: String
)