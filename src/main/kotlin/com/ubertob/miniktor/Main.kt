package com.ubertob.miniktor

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.html.*
import io.ktor.http.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

fun main() {
    initDatabase()
    insertSomeData()

    embeddedServer(Netty, port = 8080) {
        routing {
            staticResources("/static", "static")

            get("/") {
                call.respond(HtmlContent(HttpStatusCode.OK, indexHtml()))
            }

            get("/users") {
                call.respond(
                    transaction {getAllUsersPage() }
                )
            }

            get("/user/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                call.respond(
                    transaction {getUserPage(id) }
                )
            }
        }
    }.start(wait = true)
}

fun insertSomeData() =
    transaction {
        addUser(
            "Alice", LocalDate.of(1999, 11, 11)
        )
        addUser(
            "Bob", LocalDate.of(2001, 1, 31)
        )
    }
