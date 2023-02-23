package com.aimelodije.repozitoriji

import com.aimelodije.modeli.domena.Album
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface AlbumRepozitorij : JpaRepository<Album, Long> {

    fun findAllByNazivContainsIgnoreCase(dioNaziva: String, stranicenje: Pageable): Page<Album>
}