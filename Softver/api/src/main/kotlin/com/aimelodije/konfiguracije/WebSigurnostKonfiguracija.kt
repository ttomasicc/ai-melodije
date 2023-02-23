package com.aimelodije.konfiguracije

import com.aimelodije.kontroleri.filteri.JwtAuthFilter
import com.aimelodije.modeli.enumeracije.Rola
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class WebSigurnostKonfiguracija(
    private val jwtAuthFilter: JwtAuthFilter,
    private val authDavatelj: AuthenticationProvider
) {

    @Bean
    fun sigurnosniFilterLanac(http: HttpSecurity): SecurityFilterChain {
        http {
            csrf { disable() }

            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }

            authorizeRequests {
                // Auth
                authorize(HttpMethod.GET, "/api/v1/auth/token", permitAll)
                authorize(HttpMethod.POST, "/api/v1/auth/prijava", permitAll)
                authorize(HttpMethod.POST, "/api/v1/auth/registracija", permitAll)
                authorize(HttpMethod.DELETE, "/api/v1/auth/odjava", permitAll)

                // Umjetnici
                authorize(HttpMethod.GET, "/api/v1/umjetnici/*", hasRole(Rola.UMJETNIK.name))
                authorize(HttpMethod.PUT, "/api/v1/umjetnici/*", hasRole(Rola.UMJETNIK.name))

                // Albumi
                authorize(HttpMethod.GET, "/api/v1/umjetnici/*/albumi", hasRole(Rola.UMJETNIK.name))
                authorize(HttpMethod.GET, "/api/v1/umjetnici/*/albumi/*", hasRole(Rola.UMJETNIK.name))
                authorize(HttpMethod.POST, "/api/v1/umjetnici/*/albumi", hasRole(Rola.UMJETNIK.name))
                authorize(HttpMethod.PUT, "/api/v1/umjetnici/*/albumi/*", hasRole(Rola.UMJETNIK.name))
                authorize(HttpMethod.DELETE, "/api/v1/umjetnici/*/albumi/*", hasRole(Rola.UMJETNIK.name))

                // Melodije
                authorize(HttpMethod.GET, "/api/v1/umjetnici/*/albumi/*/melodija", hasRole(Rola.UMJETNIK.name))
                authorize(HttpMethod.GET, "/api/v1/umjetnici/*/albumi/*/melodije/*", hasRole(Rola.UMJETNIK.name))
                authorize(HttpMethod.POST, "/api/v1/umjetnici/*/albumi/*/melodije", hasRole(Rola.UMJETNIK.name))
                authorize(HttpMethod.PUT, "/api/v1/umjetnici/*/albumi/*/melodije/*", hasRole(Rola.UMJETNIK.name))
                authorize(HttpMethod.DELETE, "/api/v1/umjetnici/*/albumi/*/melodije/*", hasRole(Rola.UMJETNIK.name))

                // Žanrovi
                authorize(HttpMethod.GET, "/api/v1/zanrovi", hasRole(Rola.UMJETNIK.name))
                authorize(HttpMethod.POST, "/api/v1/zanrovi", hasRole(Rola.ADMINISTRATOR.name))
                authorize(HttpMethod.DELETE, "/api/v1/zanrovi", hasRole(Rola.ADMINISTRATOR.name))
                authorize(HttpMethod.PUT, "/api/v1/zanrovi/*", hasRole(Rola.ADMINISTRATOR.name))
                authorize(HttpMethod.DELETE, "/api/v1/zanrovi/*", hasRole(Rola.ADMINISTRATOR.name))

                // INFO
                authorize(HttpMethod.GET, "/api/v1/info/pretrazuj", hasRole(Rola.UMJETNIK.name))
                authorize(HttpMethod.GET, "/api/v1/info/novo", permitAll)

                authorize("/error", permitAll)
                authorize("/staticno/**", permitAll)

                authorize(anyRequest, authenticated)
            }

            addFilterBefore<UsernamePasswordAuthenticationFilter>(jwtAuthFilter)
        }

        http.authenticationProvider(authDavatelj)

        return http.build()
    }

    @Bean
    fun webSigurnostRukovatelj(): DefaultWebSecurityExpressionHandler =
        DefaultWebSecurityExpressionHandler().apply {
            setRoleHierarchy(hijerarhijaRola())
        }

    @Bean
    fun hijerarhijaRola(): RoleHierarchy =
        RoleHierarchyImpl().apply {
            setHierarchy("ROLE_${Rola.ADMINISTRATOR.name} > ROLE_${Rola.UMJETNIK.name}")
        }
}