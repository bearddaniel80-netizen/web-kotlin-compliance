package example.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/format") // Base URL path for all endpoints in this controller
class FormatController {

    private companion object {
        const val FOLDER = "format"
    }

    @GetMapping("/github")
    fun github(): String = "forward:/$FOLDER/github.html"

    @GetMapping("/html")
    fun html(): String = "forward:/$FOLDER/html.html"

    @GetMapping("/json")
    fun json(): String = "forward:/$FOLDER/json.html"

    @GetMapping("/markdown")
    fun markdown(): String = "forward:/$FOLDER/markdown.html"

    @GetMapping("/pdf")
    fun pdf(): String = "forward:/$FOLDER/pdf.html"
}
