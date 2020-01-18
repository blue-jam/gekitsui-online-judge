package bluejam.hobby.gekitsui.webapp.entity

import org.springframework.data.repository.CrudRepository
import javax.persistence.*

@Entity
class Problem (
        @Column(unique = true) var name: String,
        var title: String,
        @Column(columnDefinition = "text") var statement: String,
        @Id @GeneratedValue var id: Long? = null
)

interface ProblemRepository : CrudRepository<Problem, Long> {
    fun findByName(name: String): Problem?
}
