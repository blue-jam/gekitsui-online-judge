package bluejam.hobby.gekitsui.webapp.util

import org.springframework.security.oauth2.core.user.OAuth2User

class GitHubOAuthFields {
    companion object {
        fun getUserName(oAuth2User: OAuth2User): String? = oAuth2User.attributes["login"] as String?

        fun getUserId(oAuth2User: OAuth2User): Int? = oAuth2User.attributes["id"] as Int?
    }
}
