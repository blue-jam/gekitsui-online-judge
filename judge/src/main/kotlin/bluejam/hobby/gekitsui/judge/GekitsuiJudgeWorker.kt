package bluejam.hobby.gekitsui.judge

import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories("bluejam.hobby.gekitsui.webapp")
@EntityScan("bluejam.hobby.gekitsui.webapp")
class GekitsuiJudgeWorker {
    companion object {
        const val QUEUE_NAME = "gekitsui-judge"
    }

    @Bean
    fun container(
            connectionFactory: ConnectionFactory,
            listenerAdapter: MessageListenerAdapter
    ): SimpleMessageListenerContainer {
        val container = SimpleMessageListenerContainer()
        container.connectionFactory = connectionFactory
        container.setQueueNames(QUEUE_NAME)
        container.setMessageListener(listenerAdapter)

        return container
    }

    @Bean
    fun listenerAdapter(judgeWorker: JudgeWorker): MessageListenerAdapter =
            MessageListenerAdapter(judgeWorker, "judge")
}

fun main(args: Array<String>) {
    runApplication<GekitsuiJudgeWorker>(*args)
}
