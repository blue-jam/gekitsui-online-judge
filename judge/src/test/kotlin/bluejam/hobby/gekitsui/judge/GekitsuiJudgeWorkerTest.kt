package bluejam.hobby.gekitsui.judge

import bluejam.hobby.gekitsui.judge.tool.JudgeSuite
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class GekitsuiJudgeWorkerTest {
    @Autowired
    lateinit var judgeSuites: Array<JudgeSuite>

    @Autowired
    lateinit var judgeWorker: JudgeWorker

    @Test
    fun contextLoads() {
        assertThat(judgeSuites).isNotEmpty
        assertThat(judgeWorker).isNotNull
    }
}
