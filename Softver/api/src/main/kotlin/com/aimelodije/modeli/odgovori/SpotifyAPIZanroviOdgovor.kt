package com.aimelodije.modeli.odgovori

import com.fasterxml.jackson.annotation.JsonProperty

data class SpotifyAPIZanroviOdgovor(
    @JsonProperty("genres")
    val zanrovi: List<String>
)