package bluejam.hobby.gekitsui.webapp.controller.api

import bluejam.hobby.gekitsui.judge.tool.JudgeStatus
import bluejam.hobby.gekitsui.webapp.entity.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser("user", roles = ["USER"])
internal class SubmissionApiControllerTest(@Autowired val mockMvc: MockMvc) {
    @MockkBean
    private lateinit var submissionRepository: SubmissionRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private lateinit var submission: Submission

    @BeforeEach
    fun setup() {
        submission = Submission(
                User("user", 456),
                Problem(
                        "name",
                        "title",
                        "statement",
                        Visibility.PUBLIC,
                        mutableSetOf(User("writer", 789))
                ),
                "testcase",
                JudgeStatus.ACCEPTED
        )
    }

    @Test
    fun `fetch submission`() {
        every { submissionRepository.findById(123) } returns Optional.of(submission)

        mockMvc.perform(MockMvcRequestBuilders.get("/api/submission?id={id}", "123"))
                .andExpect {
                    MockMvcResultMatchers.status().isOk
                    MockMvcResultMatchers.content().contentType("application/json")
                    MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(submission))
                }
    }

    @Test
    fun `fetch submission with wrong ID`() {
        every { submissionRepository.findById(123) } returns Optional.empty()

        mockMvc.perform(MockMvcRequestBuilders.get("/api/submission?id={id}", "123"))
                .andExpect {
                    MockMvcResultMatchers.status().isNotFound
                }
    }

    @Test
    fun `fetch submissions`() {
        every { submissionRepository.findByAuthorUsernameOrderByIdDesc("user") } returns listOf(submission)

        mockMvc.perform(MockMvcRequestBuilders.get("/api/submissions?username={username}", "user"))
                .andExpect {
                    MockMvcResultMatchers.status().isOk
                    MockMvcResultMatchers.content().contentType("application/json")
                    MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(listOf(submission)))
                }
    }
}
