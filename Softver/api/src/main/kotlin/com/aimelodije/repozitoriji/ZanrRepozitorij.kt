package com.aimelodije.repozitoriji

import com.aimelodije.modeli.domena.Zanr
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
interface ZanrRepozitorij {

    @Cacheable(value = ["zanrovi"], unless = "#result.isEmpty()")
    fun findAll(): List<Zanr>

    @Cacheable(value = ["zanr"], key = "#id", unless = "#result == null")
    fun findByIdOrNull(id: Long): Zanr?

    fun findByNazivIgnoreCase(naziv: String): Zanr?

    fun existsByNazivIgnoreCase(naziv: String): Boolean

    fun existsById(id: Long): Boolean

    @Transactional
    @Caching(
        put = [CachePut(value = ["zanr"], key = "#zanr.id", unless = "#result == null")],
        evict = [CacheEvict(value = ["zanrovi"], allEntries = true, condition = "#result != null")]
    )
    fun update(zanr: Zanr): Zanr?

    @Transactional
    @CacheEvict(value = ["zanrovi"], allEntries = true)
    fun upsert(naziv: String)

    @Transactional
    @Caching(
        evict = [
            CacheEvict(value = ["zanrovi"], allEntries = true),
            CacheEvict(value = ["zanr"], key = "#id")
        ]
    )
    fun delete(id: Long): Boolean

    @Transactional
    @CacheEvict(value = ["zanrovi", "zanr"], allEntries = true)
    fun deleteUnused()
}