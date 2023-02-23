package com.aimelodije.kontroleri.sastavljaci

import com.aimelodije.kontroleri.api.AuthKontroler
import com.aimelodije.kontroleri.sastavljaci.mocks.MockBindingResult
import com.aimelodije.kontroleri.sastavljaci.mocks.MockHttpSession
import com.aimelodije.modeli.pogledi.JwtPogled
import com.aimelodije.modeli.resursi.JwtResurs
import com.aimelodije.modeli.zahtjevi.UmjetnikPrijavaZahtjev
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.stereotype.Component

@Component
class JwtSastavljacResursa : RepresentationModelAssemblerSupport<JwtPogled, JwtResurs>(
    AuthKontroler::class.java, JwtResurs::class.java
) {

    override fun toModel(entitet: JwtPogled): JwtResurs =
        instantiateModel(entitet).apply {
            add(
                linkTo<AuthKontroler> {
                    dohvatiToken(MockHttpSession)
                }.withSelfRel()
            )

            if (entitet.token != null) {
                add(
                    linkTo<AuthKontroler> {
                        odjaviUmjetnika(MockHttpSession)
                    }.withRel(Linkovi.ODJAVA.toString())
                )
            } else {
                add(
                    linkTo<AuthKontroler> {
                        prijaviUmjetnika(UmjetnikPrijavaZahtjev(), MockBindingResult, MockHttpSession)
                    }.withRel(Linkovi.PRIJAVA.toString())
                )
            }
        }

    override fun instantiateModel(entitet: JwtPogled): JwtResurs =
        JwtResurs(
            token = entitet.token
        )
}