package com.aimelodije.repozitoriji

import com.aimelodije.modeli.domena.Zanr
import com.aimelodije.modeli.generirano.tables.Melodija.MELODIJA
import com.aimelodije.modeli.generirano.tables.Zanr.ZANR
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository

@Repository
class JooqZanrRepozitorij(
    private val dsl: DSLContext
) : ZanrRepozitorij {

    override fun findAll(): List<Zanr> =
        dsl.selectFrom(ZANR)
            .orderBy(ZANR.NAZIV.asc())
            .fetchInto(Zanr::class.java)

    override fun findByIdOrNull(id: Long): Zanr? =
        dsl.selectFrom(ZANR)
            .where(ZANR.ID.eq(id))
            .fetchOneInto(Zanr::class.java)

    override fun findByNazivIgnoreCase(naziv: String): Zanr? =
        dsl.selectFrom(ZANR)
            .where(DSL.lower(ZANR.NAZIV).eq(DSL.lower(naziv)))
            .fetchOneInto(Zanr::class.java)

    override fun existsByNazivIgnoreCase(naziv: String): Boolean =
        dsl.fetchExists(
            dsl.selectFrom(ZANR)
                .where(DSL.lower(ZANR.NAZIV).eq(DSL.lower(naziv)))
        )

    override fun existsById(id: Long): Boolean =
        dsl.fetchExists(
            dsl.selectFrom(ZANR)
                .where(ZANR.ID.eq(id))
        )

    override fun update(zanr: Zanr): Zanr? =
        dsl.update(ZANR)
            .set(ZANR.NAZIV, DSL.lower(zanr.naziv))
            .where(ZANR.ID.eq(zanr.id))
            .returning()
            .fetchOneInto(Zanr::class.java)

    override fun upsert(naziv: String) {
        dsl.insertInto(ZANR, ZANR.NAZIV)
            .values(DSL.lower(naziv))
            .onDuplicateKeyIgnore()
            .execute()
    }

    override fun delete(id: Long): Boolean =
        dsl.deleteFrom(ZANR)
            .where(ZANR.ID.eq(id))
            .execute() > 0

    override fun deleteUnused() {
        dsl.deleteFrom(ZANR)
            .whereNotExists(
                dsl.selectOne().from(MELODIJA)
                    .where(MELODIJA.ZANR_ID.eq(ZANR.ID))
            )
            .execute()
    }
}