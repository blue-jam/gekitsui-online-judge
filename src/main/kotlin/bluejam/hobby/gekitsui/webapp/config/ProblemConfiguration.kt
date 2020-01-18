package bluejam.hobby.gekitsui.webapp.config

import bluejam.hobby.gekitsui.webapp.entity.Problem
import bluejam.hobby.gekitsui.webapp.entity.ProblemRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader

@Configuration
class ProblemConfiguration {
    @Bean
    fun problemInitializer(
            resourceLoader: ResourceLoader,
            problemRepository: ProblemRepository
    ) = ApplicationRunner {
        val aPlusBMod = problemRepository.findByName("aplusbmod") ?: Problem("", "", "")

        aPlusBMod.name = "aplusbmod"
        aPlusBMod.title = "A + B mod 10"
        aPlusBMod.statement = resourceLoader.getResource("classpath:problem/aplusbmod.md").file.readText()

        problemRepository.save(aPlusBMod)
    }
}
