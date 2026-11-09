package example.controller

import example.service.FastapiClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriUtils
import java.nio.charset.StandardCharsets

@RestController
@RequestMapping("/api/manifest")
class ManifestController(private val fastapi: FastapiClient) {

    private fun segment(value: String): String =
        UriUtils.encodePathSegment(value, StandardCharsets.UTF_8)

    // Dynamic manifest routes
    @GetMapping("/{kind}/{filename}")
    fun manifestKind(@PathVariable kind: String, @PathVariable filename: String): ResponseEntity<Any?> =
        fastapi.get("/api/manifest/${segment(kind)}/${segment(filename)}")

    @GetMapping("/failfast/query/{filename}")
    fun manifestFailfastQuery(@PathVariable filename: String): ResponseEntity<Any?> =
        fastapi.get("/api/manifest/failfast/query/${segment(filename)}")

    @GetMapping("/retry/{times}/{filename}")
    fun manifestRetry(@PathVariable times: String, @PathVariable filename: String): ResponseEntity<Any?> =
        fastapi.get("/api/manifest/retry/${segment(times)}/${segment(filename)}")
   
    @GetMapping("/{filename}")
    fun manifestDefault(@PathVariable filename: String): ResponseEntity<Any?> =
        fastapi.get("/api/manifest/${segment(filename)}")

    @GetMapping("/list")
    fun manifestList(): ResponseEntity<Any?> =
        fastapi.get("/api/manifest/list")
}
