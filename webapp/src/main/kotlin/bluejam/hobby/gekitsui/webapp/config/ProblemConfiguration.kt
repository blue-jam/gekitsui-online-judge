package bluejam.hobby.gekitsui.webapp.config

import bluejam.hobby.gekitsui.webapp.entity.Problem
import bluejam.hobby.gekitsui.webapp.entity.ProblemRepository
import bluejam.hobby.gekitsui.webapp.entity.Visibility
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader
import java.io.BufferedReader
import java.io.InputStreamReader

@Configuration
class ProblemConfiguration {
    fun createOrUpdateProblem(
            problemName: String,
            title: String,
            resourceLoader: ResourceLoader,
            problemRepository: ProblemRepository
    ) {
        val problem = problemRepository.findByName(problemName)
                ?: Problem(problemName, "", "", Visibility.PUBLIC, mutableSetOf())

        problem.title = title
        val fileUrl = resourceLoader.getResource("classpath:problem/${problemName}.md").url
        problem.statement = BufferedReader(InputStreamReader(fileUrl.openStream())).use {
            it.readText()
        }

        problemRepository.save(problem)
    }

    @Bean
    fun problemInitializer(
            resourceLoader: ResourceLoader,
            problemRepository: ProblemRepository
    ) = ApplicationRunner {
        createOrUpdateProblem("aplusbmod", "A + B mod 10", resourceLoader, problemRepository)
        createOrUpdateProblem("floyd_algorithm_warshall", "Floyd-Algorithm's warshall", resourceLoader, problemRepository)
    }
}
