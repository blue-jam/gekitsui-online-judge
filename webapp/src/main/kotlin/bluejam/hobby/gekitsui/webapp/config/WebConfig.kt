package bluejam.hobby.gekitsui.webapp.config

import bluejam.hobby.gekitsui.webapp.interceptor.SignUpInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig: WebMvcConfigurer {
    @Autowired
    lateinit var signUpInterceptor: SignUpInterceptor

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(signUpInterceptor)
    }
}
