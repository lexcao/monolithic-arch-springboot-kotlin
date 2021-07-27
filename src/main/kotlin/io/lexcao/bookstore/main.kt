package io.lexcao.bookstore

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity

@SpringBootApplication
@EnableCaching
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
class BookStoreApplication

fun main() {
    runApplication<BookStoreApplication>()
}
