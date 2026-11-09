package example.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/feature") // Base URL path for all endpoints in this controller
class FeatureController {

    private companion object {
        const val FOLDER = "feature"
    }

    @GetMapping("/cache")
    fun cache(): String = "forward:/$FOLDER/cache.html"

    @GetMapping("/failfast")
    fun failFast(): String = "forward:/$FOLDER/failfast.html"

    @GetMapping("/failfast-query")
    fun failFastQuery(): String = "forward:/$FOLDER/failfast-query.html"

    @GetMapping("/profile")
    fun profile(): String = "forward:/$FOLDER/profile.html"

    @GetMapping("/retry")
    fun retry(): String = "forward:/$FOLDER/retry.html"
}
