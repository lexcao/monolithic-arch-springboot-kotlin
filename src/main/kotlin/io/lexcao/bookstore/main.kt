package io.lexcao.bookstore

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["io.lexcao.bookstore"])
class BookstoreApplication

fun main() {
    runApplication<BookstoreApplication>()
}
