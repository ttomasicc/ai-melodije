package com.aimelodije.modeli.domena

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import java.util.Date

@Entity
@Table(name = "album")
class Album(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "slika", length = 50)
    var slika: String? = null,

    @Column(name = "naziv", length = 100)
    var naziv: String = "Moj novi album",

    @CreatedDate
    @Column(name = "datum_dodano")
    val datumDodano: Date = Date(),

    @Column(name = "opis")
    var opis: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "umjetnik_id")
    val umjetnik: Umjetnik,

    @ManyToMany
    @JoinTable(
        name = "album_melodija",
        joinColumns = [JoinColumn(name = "album_id")],
        inverseJoinColumns = [JoinColumn(name = "melodija_id")]
    )
    val melodije: MutableSet<Melodija> = mutableSetOf(),
)