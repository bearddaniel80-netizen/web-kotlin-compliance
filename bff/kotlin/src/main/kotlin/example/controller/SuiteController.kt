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
@RequestMapping("/api/suite")
class SuiteController(private val fastapi: FastapiClient) {

    private fun segment(value: String): String =
        UriUtils.encodePathSegment(value, StandardCharsets.UTF_8)

    // Dynamic suite routes
    @GetMapping("/{kind}/{filename}")
    fun suiteKind(@PathVariable kind: String, @PathVariable filename: String): ResponseEntity<Any?> =
        fastapi.get("/api/suite/${segment(kind)}/${segment(filename)}")

    @GetMapping("/failfast/query/{filename}")
    fun suiteFailfastQuery(@PathVariable filename: String): ResponseEntity<Any?> =
        fastapi.get("/api/suite/failfast/query/${segment(filename)}")

    @GetMapping("/retry/{times}/{filename}")
    fun suiteRetry(@PathVariable times: String, @PathVariable filename: String): ResponseEntity<Any?> =
        fastapi.get("/api/suite/retry/${segment(times)}/${segment(filename)}")
    @GetMapping("/{filename}")
    fun suiteDefault(@PathVariable filename: String): ResponseEntity<Any?> =
        fastapi.get("/api/suite/${segment(filename)}")

    @GetMapping("/list")
    fun suiteList(): ResponseEntity<Any?> =
        fastapi.get("/api/suite/list")
}
