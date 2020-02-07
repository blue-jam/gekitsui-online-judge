package bluejam.hobby.gekitsui.webapp.config

import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@EnableWebSecurity
class ApplicationConfigurerAdapter : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity?) {
        if (http == null) {
            return
        }

        http.authorizeRequests {
            it.antMatchers(
                    "/", "/error", "/static/**",
                    "/problems", "/problem/**", "/submissions", "/submission/**"
            ).permitAll()
                    .anyRequest().authenticated()
        }
                .oauth2Login(Customizer.withDefaults())
    }
}
