package bluejam.hobby.gekitsui.webapp.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/admin")
class AdminController {
    @RequestMapping("/")
    fun doGetIndex(model: Model, @AuthenticationPrincipal principal: OAuth2User): String = "admin/admin_index"
}
