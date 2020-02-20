package bluejam.hobby.gekitsui.webapp

import bluejam.hobby.gekitsui.webapp.entity.Problem
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
class GekitsuiWebappApplicationTests {
	@Autowired
	lateinit var mockMvc: MockMvc

	@Test
	fun contextLoads() {
	}

	@Test
	fun `access index page without login`() {
		mockMvc.perform(MockMvcRequestBuilders.get("/"))
				.andExpect(MockMvcResultMatchers.status().isOk)
				.andExpect(MockMvcResultMatchers.view().name("index"))
				.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("username"))
	}

	@Test
	fun `access problems page`() {
		mockMvc.perform(MockMvcRequestBuilders.get("/problems"))
				.andExpect(MockMvcResultMatchers.status().isOk)
				.andExpect(MockMvcResultMatchers.view().name("problems"))
				.andExpect(MockMvcResultMatchers.model().attribute("problems", Matchers.hasSize<List<Problem>>(2)))
	}

	@Test
	fun `access problem page`() {
		mockMvc.perform(MockMvcRequestBuilders.get("/problem/aplusbmod"))
				.andExpect(MockMvcResultMatchers.status().isOk)
				.andExpect(MockMvcResultMatchers.view().name("problem"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("problem"))
	}

	@Test
	fun `access problem page with wrong problem name`() {
		mockMvc.perform(MockMvcRequestBuilders.get("/problem/wrong_problem_name"))
				.andExpect(MockMvcResultMatchers.status().isNotFound)
	}
}
