package bluejam.hobby.gekitsui.webapp.util

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken

fun checkIsAdmin(oAuth2AuthenticationToken: OAuth2AuthenticationToken?): Boolean {
    oAuth2AuthenticationToken?.let { authenticationToken ->
        return authenticationToken.authorities.any { it.authority == "ROLE_ADMIN" }
    }

    return false
}
