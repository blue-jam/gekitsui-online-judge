package bluejam.hobby.gekitsui.webapp.repository

import bluejam.hobby.gekitsui.webapp.entity.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Long> {
    fun findByGithubId(githubId: Int): User?
}
