package bluejam.hobby.gekitsui.webapp.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
internal class AdminControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    @WithMockUser
    fun `non-admin user cannot access to admin page`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/"))
                .andExpect(MockMvcResultMatchers.status().isForbidden)
    }
}
