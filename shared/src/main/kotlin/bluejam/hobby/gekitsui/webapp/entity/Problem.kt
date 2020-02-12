package bluejam.hobby.gekitsui.webapp.entity

import org.hibernate.annotations.ColumnDefault
import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.*

enum class Visibility {
    /**
     * Writers of the problem and admins can see the problem.
     */
    PRIVATE,
    /**
     * All participants who joined a contest can see the problem.
     * But a contestants cannot see submissions of the others.
     */
    CONTESTANTS_ONLY,
    /**
     *  Everyone, includes anonymous users, can see the problem and submissions
     */
    PUBLIC,
}

@Entity
@Table(
        uniqueConstraints = [UniqueConstraint(columnNames = ["name"])]
)
class Problem (
        @Column(unique = true) var name: String,
        var title: String,
        @Column(columnDefinition = "text") var statement: String,
        @Enumerated(EnumType.STRING) @Column(nullable = false)
        @ColumnDefault("'PUBLIC'")  // TODO: Remove this annotation. It's added to set default visibility to existing problems.
        var visibility: Visibility,
        @ManyToMany var writers: MutableSet<User> = mutableSetOf(),
        @Id @GeneratedValue var id: Long? = null
)

interface ProblemRepository : JpaRepository<Problem, Long> {
    fun findByName(name: String): Problem?
    fun removeByName(name: String)
}
