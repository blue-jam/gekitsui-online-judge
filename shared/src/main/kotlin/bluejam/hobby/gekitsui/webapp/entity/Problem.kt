package bluejam.hobby.gekitsui.webapp.entity

import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.*

@Entity
@Table(
        uniqueConstraints = [UniqueConstraint(columnNames = ["name"])]
)
class Problem (
        @Column(unique = true) var name: String,
        var title: String,
        @Column(columnDefinition = "text") var statement: String,
        @Id @GeneratedValue var id: Long? = null
)

interface ProblemRepository : JpaRepository<Problem, Long> {
    fun findByName(name: String): Problem?
    fun removeByName(name: String)
}
