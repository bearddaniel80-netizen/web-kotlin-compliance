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
@RequestMapping("/api/tag")
class TagController(private val fastapi: FastapiClient) {

    private fun segment(value: String): String =
        UriUtils.encodePathSegment(value, StandardCharsets.UTF_8)

    // Dynamic tag routes
    @GetMapping("/{kind}/{filename}")
    fun tagKind(@PathVariable kind: String, @PathVariable filename: String): ResponseEntity<Any?> =
        fastapi.get("/api/tag/${segment(kind)}/${segment(filename)}")

    @GetMapping("/failfast/query/{filename}")
    fun tagFailfastQuery(@PathVariable filename: String): ResponseEntity<Any?> =
        fastapi.get("/api/tag/failfast/query/${segment(filename)}")

    @GetMapping("/{filename}")
    fun tagDefault(@PathVariable filename: String): ResponseEntity<Any?> =
        fastapi.get("/api/tag/${segment(filename)}")

    @GetMapping("/list")
    fun tagList(): ResponseEntity<Any?> =
        fastapi.get("/api/tag/list")
}
