package bluejam.hobby.gekitsui.webapp

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class GekitsuiWebappApplication {
    companion object {
		const val TOPIC_EXCHANGE_NAME = "gekitsui-judge-exchange"
		const val QUEUE_NAME = "gekitsui-judge"
	}

	@Bean
	fun queue(): Queue = Queue(QUEUE_NAME, true)

	@Bean
	fun exchange(): TopicExchange = TopicExchange(TOPIC_EXCHANGE_NAME)

	@Bean
	fun binding(
			queue: Queue,
			exchange: TopicExchange
	): Binding {
		return BindingBuilder
				.bind(queue)
				.to(exchange)
				.with("")
	}
}

fun main(args: Array<String>) {
	runApplication<GekitsuiWebappApplication>(*args)
}
