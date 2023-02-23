package com.aimelodije.konfiguracije.postavke

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.time.DurationMin
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.validation.annotation.Validated
import java.time.Duration

@Validated
@ConfigurationProperties(prefix = "app.jwt")
data class JwtPostavke @ConstructorBinding constructor(
    @field:NotBlank(message = "Nedostaje tajni JWT ključ")
    @field:Size(min = 64, message = "Tajni JWT ključ mora imati najmanje 256 bitova (64 karaktera)")
    val tajniKljuc: String,
    @field:NotBlank(message = "Nedostaje JWT davatelj")
    val davatelj: String,
    @field:DurationMin(seconds = 3L, message = "Trajanje JWT-a mora biti najmanje 3 sekunde")
    val trajanje: Duration = Duration.ofSeconds(10),
    @field:NotBlank(message = "JWT atribut sesije ne može biti prazan")
    val sesija: String = "JWT_SESIJA"
)