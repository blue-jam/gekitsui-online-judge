package bluejam.hobby.gekitsui.webapp.controller

import bluejam.hobby.gekitsui.webapp.entity.ContestRepository
import bluejam.hobby.gekitsui.webapp.util.GitHubOAuthFields
import bluejam.hobby.gekitsui.webapp.util.checkIsAdmin
import org.springframework.data.domain.Sort
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import java.sql.Timestamp
import java.time.Instant

@Controller
class ContestController(
        val contestRepository: ContestRepository
) {
    @GetMapping("/contests")
    fun contestList(
            model: Model
    ): String {
        model["contests"] = contestRepository.findAll(Sort.by(Sort.Direction.DESC, "startTime"))

        return "contest/contest_list"
    }

    @GetMapping(value = ["/contest/{name}", "/contest/{name}/**"])
    fun index(
            @PathVariable name: String,
            model: Model,
            @AuthenticationPrincipal authenticationToken: OAuth2AuthenticationToken
    ): String {
        val contest = contestRepository.findByName(name) ?: throw NotFoundException("There is no contest with name = $name")
        val isParticipated = contest.contestants.any { it.githubId == GitHubOAuthFields.getUserId(authenticationToken.principal) }
        val isAdmin = checkIsAdmin(authenticationToken)
        val currentTimeStamp = Timestamp(Instant.now().toEpochMilli())
        val hasStarted = currentTimeStamp.after(contest.startTime)
        val hasEnded = currentTimeStamp.after(contest.endTime)

        if (!hasEnded && !(isParticipated && hasStarted) && !isAdmin) {
            contest.problemSet = mutableListOf()
        }

        model["contest"] = contest
        model["isParticipated"] = isParticipated || isAdmin

        return "contest/contest_index"
    }
}
