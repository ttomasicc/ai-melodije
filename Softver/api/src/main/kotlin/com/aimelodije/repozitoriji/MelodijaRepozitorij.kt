package com.aimelodije.repozitoriji

import com.aimelodije.modeli.domena.Melodija
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface MelodijaRepozitorij : JpaRepository<Melodija, Long> {

    fun findAllByNazivContainsIgnoreCase(dioNaziva: String, stranicenje: Pageable): Page<Melodija>
}