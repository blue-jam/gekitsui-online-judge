package bluejam.hobby.gekitsui.webapp.controller

import bluejam.hobby.gekitsui.webapp.entity.Contest
import bluejam.hobby.gekitsui.webapp.entity.ContestRepository
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Sort
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.sql.Timestamp

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser("user", roles = ["USER"])
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
    fun `show contest page`() {
        val contest = Contest(
                "name",
                "title",
                Timestamp.valueOf("2020-02-26 23:00:00"),
                Timestamp.valueOf("2020-02-26 23:30:00"),
                mutableListOf(),
                mutableSetOf()
        )

        every { contestRepository.findByName("name") } returns contest

        mockMvc.perform(MockMvcRequestBuilders.get("/contest/name"))
                .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `show contest page with wrong name`() {
        every { contestRepository.findByName("name") } returns null

        mockMvc.perform(MockMvcRequestBuilders.get("/contest/name"))
                .andExpect(MockMvcResultMatchers.status().isNotFound)
    }
}
