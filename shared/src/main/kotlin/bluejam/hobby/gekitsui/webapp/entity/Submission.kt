package bluejam.hobby.gekitsui.webapp.entity

import bluejam.hobby.gekitsui.judge.tool.JudgeStatus
import org.hibernate.annotations.CreationTimestamp
import org.springframework.data.jpa.repository.JpaRepository
import java.sql.Timestamp
import javax.persistence.*

@Entity
class Submission (
        @ManyToOne var author: User,
        @ManyToOne var problem: Problem,
        @Column(columnDefinition = "text") var testcase: String,
        @Enumerated(EnumType.STRING) var status: JudgeStatus,
        @CreationTimestamp var createdDate: Timestamp? = null,
        @Id @GeneratedValue var id: Long? = null
)

interface SubmissionRepository: JpaRepository<Submission, Long> {
    fun findByProblemName(name: String): List<Submission>
    fun findByAuthorUsernameOrderByIdDesc(username: String): List<Submission>
}
