package bluejam.hobby.gekitsui.webapp.test.util

import org.springframework.security.test.context.support.WithSecurityContext

@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = WithGitHubUserSecurityContextFactory::class)
annotation class WithGitHubUser(
        val username: String = "user",
        val githubId: Int = 123
)
