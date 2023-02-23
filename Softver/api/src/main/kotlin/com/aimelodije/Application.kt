package com.aimelodije

import com.aimelodije.zadaci.ZanroviZadatak
import org.springframework.beans.factory.getBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.TimeZone

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    val app = runApplication<Application>(*args)

    // Ažurira žanrove kod pokretanja aplikacije
    val zanroviZadatak = app.getBean<ZanroviZadatak>()
    zanroviZadatak.azurirajZanrove()

    TimeZone.setDefault(TimeZone.getTimeZone("Europe/Zagreb"))
    println("Aplikacija uspješno pokrenuta!")
}