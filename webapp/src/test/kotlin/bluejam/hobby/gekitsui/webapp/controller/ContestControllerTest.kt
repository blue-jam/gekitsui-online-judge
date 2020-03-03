package bluejam.hobby.gekitsui.webapp.controller

import bluejam.hobby.gekitsui.webapp.entity.Contest
import bluejam.hobby.gekitsui.webapp.entity.ContestRepository
import bluejam.hobby.gekitsui.webapp.entity.Problem
import bluejam.hobby.gekitsui.webapp.entity.User
import bluejam.hobby.gekitsui.webapp.test.util.WithGitHubUser
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Sort
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.sql.Timestamp

@SpringBootTest
@AutoConfigureMockMvc
@WithGitHubUser(username = "user", githubId = 123)
internal class ContestControllerTest(@Autowired val mockMvc: MockMvc) {
    @MockkBean
    private lateinit var contestRepository: ContestRepository

    @Test
    fun `show contest list page`() {
        val contest = Contest(
                "name",
                "title",
                Timestamp.valueOf("2020-02-26 23:00:00"),
                Timestamp.valueOf("2020-02-26 23:30:00"),
                mutableListOf(),
                mutableSetOf()
        )

        every { contestRepository.findAll(any<Sort>()) } returns listOf(contest)

        mockMvc.perform(MockMvcRequestBuilders.get("/contests"))
                .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `Non-participant can see problem set of finished contest`() {
        val contest = spyk(Contest(
                "name",
                "title",
                Timestamp.valueOf("2020-02-26 23:00:00"),
                Timestamp.valueOf("2020-02-26 23:30:00"),
                problemSet = mutableListOf(),
                contestants = mutableSetOf()
        ))

        every { contestRepository.findByName("name") } returns contest

        mockMvc.get("/contest/name").andExpect {
            status { isOk }
            model {
                attribute("contest", contest)
            }
        }

        verify(exactly = 0) { contest setProperty "problemSet" value any<MutableList<Problem>>() }
    }

    @Test
    fun `Non-participant cannot see problem set during contest`() {
        val contest = spyk(Contest(
                "name",
                "title",
                Timestamp.valueOf("2020-02-26 23:00:00"),
                Timestamp.valueOf("3020-02-26 23:30:00"),
                problemSet = mutableListOf(),
                contestants = mutableSetOf()
        ))

        every { contestRepository.findByName("name") } returns contest

        mockMvc.get("/contest/name").andExpect {
            status { isOk }
            model {
                attribute("contest", contest)
            }
        }

        verify { contest setProperty "problemSet" value mutableListOf<Problem>() }
    }

    @Test
    fun `Participant can see problem set during contest`() {
        val contest = spyk(Contest(
                "name",
                "title",
                Timestamp.valueOf("2020-02-26 23:00:00"),
                Timestamp.valueOf("3020-02-26 23:30:00"),
                problemSet = mutableListOf(),
                contestants = mutableSetOf(User("username", githubId = 123))
        ))

        every { contestRepository.findByName("name") } returns contest

        mockMvc.get("/contest/name").andExpect {
            status { isOk }
            model {
                attribute("contest", contest)
            }
        }

        verify(exactly = 0) { contest setProperty "problemSet" value any<MutableList<Problem>>() }
    }

    @Test
    fun `show contest page with wrong name`() {
        every { contestRepository.findByName("name") } returns null

        mockMvc.perform(MockMvcRequestBuilders.get("/contest/name"))
                .andExpect(MockMvcResultMatchers.status().isNotFound)
    }
}
