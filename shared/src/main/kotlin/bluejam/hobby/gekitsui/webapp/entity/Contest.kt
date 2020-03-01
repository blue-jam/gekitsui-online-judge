package bluejam.hobby.gekitsui.webapp.entity

import org.springframework.data.jpa.repository.JpaRepository
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(
        uniqueConstraints = [UniqueConstraint(columnNames = ["name"])]
)
data class Contest (
        @Column(unique = true) var name: String,
        var title: String,
        var startTime: Timestamp,
        var endTime: Timestamp,
        @ManyToMany @OrderColumn(name = "index") var problemSet: MutableList<Problem>,
        @ManyToMany var contestants: MutableSet<User> = mutableSetOf(),
        @Id @GeneratedValue var id: Long? = null
)

interface ContestRepository : JpaRepository<Contest, Long> {
    fun findByName(name: String): Contest?
    fun removeByName(name: String)
}
