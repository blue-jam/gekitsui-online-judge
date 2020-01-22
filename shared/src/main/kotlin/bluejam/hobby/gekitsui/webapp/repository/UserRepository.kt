package bluejam.hobby.gekitsui.webapp.repository

import bluejam.hobby.gekitsui.webapp.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByGithubId(githubId: Int): User?
}
