package bluejam.hobby.gekitsui.webapp.controller.api

import bluejam.hobby.gekitsui.judge.tool.JudgeStatus
import bluejam.hobby.gekitsui.webapp.entity.Problem
import bluejam.hobby.gekitsui.webapp.entity.Submission
import bluejam.hobby.gekitsui.webapp.entity.SubmissionRepository
import bluejam.hobby.gekitsui.webapp.entity.User
import bluejam.hobby.gekitsui.webapp.repository.UserRepository
import bluejam.hobby.gekitsui.webapp.test.util.WithGitHubUser
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.amqp.rabbit.junit.RabbitAvailable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
@RabbitAvailable("gekitsui-judge")
@WithMockUser("user", roles = ["USER"])
internal class SubmitControllerTest {
    companion object {
        const val PROBLEM_NAME = "aplusbmod"
        const val TESTCASE = "testcase"
    }

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var userRepository: UserRepository
    @MockkBean
    private lateinit var submissionRepository: SubmissionRepository

    @Test
    @WithAnonymousUser
    fun `anonymous user cannot submit answer`() {
        mockMvc.post("/api/submit") {
            param("problemName", PROBLEM_NAME)
            param("testcase", TESTCASE)
            with(SecurityMockMvcRequestPostProcessors.csrf())
        }.andExpect {
            status { is3xxRedirection }
        }
    }

    @Test
    @WithGitHubUser(username = "user", githubId = 123)
    fun `user submit answer`() {
        val user = User("user", 123, 456)
        val problem = mockk<Problem>()
        val submission = Submission(user, problem, TESTCASE, JudgeStatus.WAITING_JUDGE, id = 789)

        every { userRepository.findByGithubId(123) } returns user
        every {
            submissionRepository.save(match<Submission> {
                it.problem.name == PROBLEM_NAME && it.author === user && it.testcase == TESTCASE &&
                        it.status == JudgeStatus.WAITING_JUDGE
            })
        } returns submission

        mockMvc.post("/api/submit") {
            param("problemName", PROBLEM_NAME)
            param("testcase", TESTCASE)
            with(SecurityMockMvcRequestPostProcessors.csrf())
        }.andExpect {
            status { is3xxRedirection }
            redirectedUrl("/submission/789")
        }

        verify {
            submissionRepository.save(match<Submission> {
                it.problem.name == PROBLEM_NAME && it.author === user && it.testcase == TESTCASE &&
                        it.status == JudgeStatus.WAITING_JUDGE
            })
        }
        // TODO: Verify RabbitMQ message
    }

    @Test
    @WithGitHubUser(username = "user", githubId = 123)
    fun `user submit answer from contest page`() {
        val user = User("user", 123, 456)
        val problem = mockk<Problem>()
        val submission = Submission(user, problem, TESTCASE, JudgeStatus.WAITING_JUDGE, id = 789)

        every { userRepository.findByGithubId(123) } returns user
        every { submissionRepository.save(any<Submission>()) } returns submission

        mockMvc.post("/api/submit") {
            param("problemName", PROBLEM_NAME)
            param("testcase", TESTCASE)
            param("contestName", "contestName")
            with(SecurityMockMvcRequestPostProcessors.csrf())
        }.andExpect {
            status { is3xxRedirection }
            redirectedUrl("/contest/contestName/submission/789")
        }

        verify { submissionRepository.save(any<Submission>()) }
    }
}
