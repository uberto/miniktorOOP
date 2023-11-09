package com.ubertob.miniktor.com.ubertob.miniktor

import com.ubertob.miniktor.UserService
import com.ubertob.miniktor.UserView
import com.ubertob.miniktor.initDatabase
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.html.*
import io.ktor.http.*
import java.time.LocalDate

fun main() {
    val userService = UserService()
    initDatabase()
    insertFakeData(userService)
    embeddedServer(Netty, port = 8080) {
        routing {
            staticResources("/static", "static")

            get("/") {
                call.respond(HtmlContent(HttpStatusCode.OK, UserView.indexHtml()))

            }

            // Endpoint that htmx will call to load the user list
            get("/users") {
                 val users = userService.getAllUsers()
                call.respond(HtmlContent(HttpStatusCode.OK, UserView.usersPage(users)))

            }
        }
    }.start(wait = true)
}

fun insertFakeData(userService: UserService) {
    userService.addUser(
        "Alice", LocalDate.of(1999,11,11)
    )
    userService.addUser(
        "Bob", LocalDate.of(2001,1,31)
    )
}
