package bluejam.hobby.gekitsui.webapp.controller.api

import bluejam.hobby.gekitsui.webapp.entity.SubmissionRepository
import bluejam.hobby.gekitsui.webapp.repository.UserRepository
import com.ninjasquad.springmockk.MockkBean
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser("user", roles = ["USER"])
internal class SubmitControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var userRepository: UserRepository
    @MockkBean
    private lateinit var submissionRepository: SubmissionRepository

    @Test
    @WithAnonymousUser
    fun `anonymous user cannot submit answer`() {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/submit"))
                .andExpect(MockMvcResultMatchers.status().isForbidden)
    }
}
