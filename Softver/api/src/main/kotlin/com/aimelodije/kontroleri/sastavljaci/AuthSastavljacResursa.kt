package com.aimelodije.kontroleri.sastavljaci

import com.aimelodije.kontroleri.api.AlbumKontroler
import com.aimelodije.kontroleri.api.AuthKontroler
import com.aimelodije.kontroleri.api.UmjetnikKontroler
import com.aimelodije.kontroleri.sastavljaci.mocks.MockBindingResult
import com.aimelodije.kontroleri.sastavljaci.mocks.MockHttpSession
import com.aimelodije.modeli.enumeracije.UmjetnikAkcija
import com.aimelodije.modeli.pogledi.UmjetnikDodatnoPogled
import com.aimelodije.modeli.resursi.UmjetnikResurs
import com.aimelodije.modeli.zahtjevi.UmjetnikPrijavaZahtjev
import com.aimelodije.modeli.zahtjevi.UmjetnikRegistracijaZahtjev
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.stereotype.Component

@Component
class AuthSastavljacResursa : RepresentationModelAssemblerSupport<UmjetnikDodatnoPogled, UmjetnikResurs>(
    AuthKontroler::class.java, UmjetnikResurs::class.java
) {

    override fun toModel(entitet: UmjetnikDodatnoPogled): UmjetnikResurs =
        instantiateModel(entitet).apply {
            when (entitet.akcija) {
                UmjetnikAkcija.REGISTRACIJA -> {
                    add(
                        linkTo<AuthKontroler> {
                            registrirajUmjetnika(UmjetnikRegistracijaZahtjev(), MockBindingResult, MockHttpSession)
                        }.withSelfRel(),
                        linkTo<AuthKontroler> {
                            prijaviUmjetnika(UmjetnikPrijavaZahtjev(), MockBindingResult, MockHttpSession)
                        }.withRel(Linkovi.PRIJAVA.toString())
                    )
                }

                UmjetnikAkcija.PRIJAVA -> {
                    add(
                        linkTo<AuthKontroler> {
                            prijaviUmjetnika(UmjetnikPrijavaZahtjev(), MockBindingResult, MockHttpSession)
                        }.withSelfRel(),
                        linkTo<AuthKontroler> {
                            dohvatiToken(MockHttpSession)
                        }.withRel(Linkovi.TOKEN.toString()),
                        linkTo<UmjetnikKontroler> {
                            dohvati(entitet.umjetnik.id)
                        }.withRel(Linkovi.PROFIL.toString()),
                        linkTo<AlbumKontroler> {
                            dohvatiSve(entitet.umjetnik.id)
                        }.withRel(Linkovi.ALBUMI.toString()),
                        linkTo<AuthKontroler> {
                            odjaviUmjetnika(MockHttpSession)
                        }.withRel(Linkovi.ODJAVA.toString()),
                    )
                }
            }
        }

    override fun instantiateModel(entitet: UmjetnikDodatnoPogled): UmjetnikResurs =
        UmjetnikResurs(
            id = entitet.umjetnik.id,
            korime = entitet.umjetnik.korime,
            email = entitet.umjetnik.email,
            ime = entitet.umjetnik.ime,
            prezime = entitet.umjetnik.prezime,
            opis = entitet.umjetnik.opis,
            slika = entitet.umjetnik.slika,
            datumRegistracije = entitet.umjetnik.datumRegistracije,
            rola = entitet.umjetnik.rola
        )
}