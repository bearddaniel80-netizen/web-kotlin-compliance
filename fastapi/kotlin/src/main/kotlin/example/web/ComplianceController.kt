package example.web

import example.process.ReportRenderer
import example.security.InternalKeyVerifier
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

/** One controller serves /api/manifest, /api/suite and /api/tag. */
@RestController
@RequestMapping("/api/{folder:manifest|suite|tag}")
class ComplianceController(
    private val renderer: ReportRenderer,
    private val keys: InternalKeyVerifier,
) {
    private val html = MediaType.TEXT_HTML_VALUE

    private fun cmd(folder: String, filename: String, vararg extra: String): List<String> {
        // Filenames come from the URL; reject anything that could be parsed as a CLI option.
        if (filename.startsWith("-")) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid filename")
        return listOf("compliance", folder, "run", filename) + extra
    }

    @GetMapping("/list")
    fun list(
        @PathVariable folder: String,
        @RequestHeader("x-internal-key", required = false) key: String?,
    ): Map<String, List<String>> {
        keys.verify(key)
        val out = renderer.run(listOf("compliance", folder, "list"))
        return mapOf("result" to out.split(Regex("\n+")).filter { it.isNotEmpty() })
    }

    // ---- Format ----

    @GetMapping("/github/{filename}", produces = [MediaType.TEXT_HTML_VALUE])
    fun github(@PathVariable folder: String, @PathVariable filename: String,
               @RequestHeader("x-internal-key", required = false) key: String?): String {
        keys.verify(key)
        return renderer.renderGithub(cmd(folder, filename, "--format", "github"))
    }

    @GetMapping("/html/{filename}", produces = [MediaType.TEXT_HTML_VALUE])
    fun html(@PathVariable folder: String, @PathVariable filename: String,
             @RequestHeader("x-internal-key", required = false) key: String?): String {
        keys.verify(key)
        return renderer.renderHtml(cmd(folder, filename, "--format", "html"))
    }

    @GetMapping("/markdown/{filename}", produces = [MediaType.TEXT_HTML_VALUE])
    fun markdown(@PathVariable folder: String, @PathVariable filename: String,
                 @RequestHeader("x-internal-key", required = false) key: String?): String {
        keys.verify(key)
        return renderer.renderMarkdown(cmd(folder, filename, "--format", "md"))
    }

    @GetMapping("/pdf/{filename}", produces = [MediaType.TEXT_HTML_VALUE])
    fun pdf(@PathVariable folder: String, @PathVariable filename: String,
            @RequestHeader("x-internal-key", required = false) key: String?): String {
        keys.verify(key)
        return renderer.renderPdf(cmd(folder, filename, "--format", "pdf"))
    }

    // ---- Features ----

    @GetMapping("/cache/{filename}")
    fun cache(@PathVariable folder: String, @PathVariable filename: String,
              @RequestHeader("x-internal-key", required = false) key: String?): Map<String, Any?> {
        keys.verify(key)
        renderer.run(cmd(folder, filename))
        return renderer.renderJson(cmd(folder, filename, "--cache", "--format", "json"))
    }

    @GetMapping("/failfast/{filename}")
    fun failFast(@PathVariable folder: String, @PathVariable filename: String,
                 @RequestHeader("x-internal-key", required = false) key: String?): Map<String, Any?> {
        keys.verify(key)
        return renderer.renderJson(cmd(folder, filename, "--fail-fast", "--format", "json"))
    }

    @GetMapping("/failfast/query/{filename}")
    fun failFastQuery(@PathVariable folder: String, @PathVariable filename: String,
                      @RequestHeader("x-internal-key", required = false) key: String?): Map<String, Any?> {
        keys.verify(key)
        return renderer.renderJson(cmd(folder, filename, "--fail-fast-query", "--format", "json"))
    }

    @GetMapping("/profile/{filename}")
    fun profile(@PathVariable folder: String, @PathVariable filename: String,
                @RequestHeader("x-internal-key", required = false) key: String?): Map<String, Any?> {
        keys.verify(key)
        return renderer.renderJson(cmd(folder, filename, "--profile", "--format", "json"))
    }

    @GetMapping("/retry/{times}/{filename}")
    fun retry(@PathVariable folder: String, @PathVariable times: Int, @PathVariable filename: String,
              @RequestHeader("x-internal-key", required = false) key: String?): Map<String, Any?> {
        keys.verify(key)
        return renderer.renderJson(cmd(folder, filename, "--retry", times.toString(), "--format", "json"))
    }

    @GetMapping("/{filename}")
    fun run(@PathVariable folder: String, @PathVariable filename: String,
            @RequestHeader("x-internal-key", required = false) key: String?): Map<String, Any?> {
        keys.verify(key)
        return renderer.renderJson(cmd(folder, filename, "--format", "json"))
    }
}
