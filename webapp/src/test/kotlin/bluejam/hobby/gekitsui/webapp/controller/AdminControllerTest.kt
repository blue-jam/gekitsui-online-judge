package bluejam.hobby.gekitsui.webapp.controller

import bluejam.hobby.gekitsui.webapp.entity.Problem
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser("admin", roles = ["USER", "ADMIN"])
internal class AdminControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    @WithMockUser
    fun `non-admin user cannot access admin page`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/"))
                .andExpect(MockMvcResultMatchers.status().isForbidden)
    }

    @Test
    fun `admin user can access admin page`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/"))
                .andExpect(MockMvcResultMatchers.view().name("admin/admin_index"))
                .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `admin can access admin problems page`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/problems"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.view().name("admin/admin_problems"))
                .andExpect(MockMvcResultMatchers.model().attribute("problems", Matchers.hasSize<List<Problem>>(2)))
    }

    @Test
    fun `admin can access problem page`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/problem/aplusbmod"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.view().name("admin/problem_form"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("problem"))
    }

    @Test
    fun `admin can access problem page with wrong problem name`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/problem/wrong_name"))
                .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `admin can access create problem page`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/create_problem"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.view().name("admin/problem_form"))
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("problem"))
    }

    @Test
    fun `admin can access rejudge page`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/rejudge"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.view().name("admin/rejudge"))
    }

    @Test
    fun `bad request for rejudge API endpoint`() {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/admin/api/rejudge").with(SecurityMockMvcRequestPostProcessors.csrf())
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }
}
