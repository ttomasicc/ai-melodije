package com.aimelodije.repozitoriji

import com.aimelodije.modeli.domena.Umjetnik
import org.springframework.data.jpa.repository.JpaRepository

interface UmjetnikRepozitorij : JpaRepository<Umjetnik, Long> {

    fun findByKorimeIgnoreCase(korime: String): Umjetnik?

    fun existsByKorimeIgnoreCase(korime: String): Boolean

    fun existsByEmailIgnoreCase(email: String): Boolean
}