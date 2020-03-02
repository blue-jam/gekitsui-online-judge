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
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
@RabbitAvailable("gekitsui-judge")
@WithGitHubUser(username = "user", githubId = 123)
internal class SubmitControllerTest {
    companion object {
        const val PROBLEM_NAME = "aplusbmod"
        const val TESTCASE = "testcase"
        const val USER_NAME = "user"
        const val GITHUB_ID = 123
        const val USER_ID = 456L
        val USER = User(USER_NAME, GITHUB_ID, USER_ID)
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
    fun `user submit answer`() {
        val problem = mockk<Problem>()
        val submission = Submission(USER, problem, TESTCASE, JudgeStatus.WAITING_JUDGE, id = 789)

        every { userRepository.findByGithubId(GITHUB_ID) } returns USER
        every {
            submissionRepository.save(match<Submission> {
                it.problem.name == PROBLEM_NAME && it.author === USER && it.testcase == TESTCASE &&
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
                it.problem.name == PROBLEM_NAME && it.author === USER && it.testcase == TESTCASE &&
                        it.status == JudgeStatus.WAITING_JUDGE
            })
        }
        // TODO: Verify RabbitMQ message
    }

    @Test
    fun `user submit answer from contest page`() {
        val problem = mockk<Problem>()
        val submission = Submission(USER, problem, TESTCASE, JudgeStatus.WAITING_JUDGE, id = 789)

        every { userRepository.findByGithubId(GITHUB_ID) } returns USER
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

    @Test
    fun `When unregistered user submit answer, then throw UnauthorizedException`() {
        every { userRepository.findByGithubId(GITHUB_ID) } returns null

        mockMvc.post("/api/submit") {
            param("problemName", PROBLEM_NAME)
            param("testcase", TESTCASE)
            with(SecurityMockMvcRequestPostProcessors.csrf())
        }.andExpect {
            status { isUnauthorized }
        }
    }

    @Test
    fun `When wrong problem name is passed, then return BadRequest`() {
        every { userRepository.findByGithubId(GITHUB_ID) } returns USER

        mockMvc.post("/api/submit") {
            param("problemName", "wrong_problem_name")
            param("testcase", TESTCASE)
            with(SecurityMockMvcRequestPostProcessors.csrf())
        }.andExpect {
            status { isBadRequest }
        }
    }

    @Test
    fun `When submission repository failed to save submission, then throw ServiceUnavailable`() {
        val problem = mockk<Problem>()

        every { userRepository.findByGithubId(GITHUB_ID) } returns USER
        every { submissionRepository.save(any<Submission>()) } returns
                Submission(USER, problem, TESTCASE, JudgeStatus.WAITING_JUDGE, createdDate = null, id = null)

        mockMvc.post("/api/submit") {
            param("problemName", PROBLEM_NAME)
            param("testcase", TESTCASE)
            with(SecurityMockMvcRequestPostProcessors.csrf())
        }.andExpect {
            status { isServiceUnavailable }
        }
    }

    @Test
    fun `When user submit too long testcase, then return BadRequest`() {
        every { userRepository.findByGithubId(GITHUB_ID) } returns USER

        mockMvc.post("/api/submit") {
            param("problemName", PROBLEM_NAME)
            param("testcase", (1..2001).joinToString("") { "a" })
            with(SecurityMockMvcRequestPostProcessors.csrf())
        }.andExpect {
            status { isBadRequest }
        }
    }
}
