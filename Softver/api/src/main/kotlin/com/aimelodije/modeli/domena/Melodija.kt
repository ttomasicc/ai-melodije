package com.aimelodije.modeli.domena

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import java.util.Date

@Entity
@Table(name = "melodija")
class Melodija(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "audio", length = 50)
    val audio: String,

    @Column(name = "naziv", length = 100)
    var naziv: String = "Moja nova melodija",

    @CreatedDate
    @Column(name = "datum_dodano")
    val datumDodano: Date = Date(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id")
    val autor: Umjetnik,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zanr_id")
    val zanr: Zanr,

    @ManyToMany(mappedBy = "melodije")
    val albumi: MutableSet<Album> = mutableSetOf()
)