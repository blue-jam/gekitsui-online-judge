package bluejam.hobby.gekitsui.webapp.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority
import org.springframework.security.web.csrf.CookieCsrfTokenRepository

@EnableWebSecurity
class ApplicationConfigurerAdapter : WebSecurityConfigurerAdapter() {
    @Value("\${gekitsui.admins}")
    val admins: List<String> = emptyList()

    override fun configure(http: HttpSecurity?) {
        if (http == null) {
            return
        }

        http.oauth2Login()
                .userInfoEndpoint()
                    .userAuthoritiesMapper(userAuthoritiesMapper())

        http.csrf { it.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) }

        http.authorizeRequests {
            it.antMatchers(
                    "/", "/error", "/static/**",
                    "/problems", "/problem/**", "/submissions", "/submission/**"
            ).permitAll()
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
        }
                .oauth2Login(Customizer.withDefaults())
    }

    private fun userAuthoritiesMapper(): GrantedAuthoritiesMapper {
        return GrantedAuthoritiesMapper{ authorities ->
            val isAdminUser = authorities.filterIsInstance<OAuth2UserAuthority>()
                    .any { admins.contains(it.attributes["login"]) }

            if (isAdminUser) {
                setOf(*authorities.toTypedArray(), SimpleGrantedAuthority("ROLE_ADMIN"))
            } else {
                authorities
            }
        }
    }
}
