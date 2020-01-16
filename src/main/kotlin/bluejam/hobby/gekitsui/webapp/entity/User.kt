package bluejam.hobby.gekitsui.webapp.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name="\"User\"")
class User(
        var username: String,
        var githubId: Int,
        @Id @GeneratedValue var id: Long? = null
)
