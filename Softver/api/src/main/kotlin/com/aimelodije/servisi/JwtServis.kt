package com.aimelodije.servisi

import com.aimelodije.konfiguracije.postavke.JwtPostavke
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.AlgorithmMismatchException
import com.auth0.jwt.exceptions.IncorrectClaimException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.MissingClaimException
import com.auth0.jwt.exceptions.TokenExpiredException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.SignatureException
import java.util.Date

@Service
class JwtServis(
    private val jwtPostavke: JwtPostavke
) {
    private val algoritam = Algorithm.HMAC256(jwtPostavke.tajniKljuc)

    fun generiraj(detaljiKorisnika: UserDetails): String =
        System.currentTimeMillis().let { trenutnoMillis ->
            JWT.create()
                .withSubject(detaljiKorisnika.username)
                .withArrayClaim(
                    "roles", arrayOf(detaljiKorisnika.authorities.joinToString(separator = ",") { it.authority })
                )
                .withIssuedAt(Date(trenutnoMillis))
                .withExpiresAt(Date(trenutnoMillis + jwtPostavke.trajanje.toMillis()))
                .withIssuer(jwtPostavke.davatelj)
                .sign(algoritam)
        }

    fun dohvatiKorime(token: String): String =
        JWT.decode(token).subject

    fun jeValidan(token: String, dozvoljeneIznimke: Array<Class<*>>? = null): Boolean = try {
        JWT
            .require(algoritam)
            .withIssuer(jwtPostavke.davatelj)
            .build()
            .verify(token)
        true
    } catch (algoritamIznimka: AlgorithmMismatchException) {
        dozvoljeneIznimke?.contains(AlgorithmMismatchException::class.java) ?: false
    } catch (potpisIznimka: SignatureException) {
        dozvoljeneIznimke?.contains(SignatureException::class.java) ?: false
    } catch (istekaoTokenIznimka: TokenExpiredException) {
        dozvoljeneIznimke?.contains(TokenExpiredException::class.java) ?: false
    } catch (nepostojeciAtributiIznimka: MissingClaimException) {
        dozvoljeneIznimke?.contains(MissingClaimException::class.java) ?: false
    } catch (netocniAtributiIznimka: IncorrectClaimException) {
        dozvoljeneIznimke?.contains(IncorrectClaimException::class.java) ?: false
    } catch (verifikacijaIznimka: JWTVerificationException) {
        dozvoljeneIznimke?.contains(JWTVerificationException::class.java) ?: false
    }
}