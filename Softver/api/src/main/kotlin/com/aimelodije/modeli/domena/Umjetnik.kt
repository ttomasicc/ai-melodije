package com.aimelodije.modeli.domena

import com.aimelodije.modeli.enumeracije.Rola
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.Date

@Entity
@Table(name = "umjetnik")
class Umjetnik(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,

    @Column(name = "korime", length = 30, unique = true)
    val korime: String,

    @Column(name = "email", length = 70, unique = true)
    var email: String,

    @Column(name = "lozinka", length = 60)
    var lozinka: String,

    @Column(name = "ime", length = 70)
    var ime: String? = null,

    @Column(name = "prezime", length = 70)
    var prezime: String? = null,

    @Column(name = "opis")
    var opis: String? = null,

    @Column(name = "slika", length = 50)
    var slika: String? = null,

    @CreatedDate
    @Column(name = "datum_registracije",)
    val datumRegistracije: Date = Date(),

    @Enumerated(EnumType.STRING)
    @Column(name = "rola", length = 30)
    val rola: Rola,

    @OneToMany(mappedBy = "umjetnik")
    val albumi: MutableSet<Album> = mutableSetOf()
) : UserDetails {
    override fun getAuthorities() = listOf(SimpleGrantedAuthority("ROLE_${rola.name}"))

    override fun getPassword() = lozinka

    override fun getUsername() = korime

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}