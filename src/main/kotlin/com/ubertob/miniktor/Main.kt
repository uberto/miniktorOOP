package com.ubertob.miniktor

import io.ktor.html.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.LocalDate

fun main() {
    initDatabase()
    val userService = UserService()
    val userView = UserView()
    val controller = UserController(userService, userView)
    insertModelData(userService)

    val webServer = httpRoutes(userView, controller)
    webServer.start(wait = true)
}

private fun httpRoutes(
    userView: UserView,
    controller: UserController
) = embeddedServer(Netty, port = 8080) {
    routing {
        staticResources("/static", "static")

        get("/") {
            call.respond(HtmlContent(HttpStatusCode.OK, userView.indexHtml()))
        }

        get("/users") {
            controller.respondWithAllUsers(call)
        }

        get("/user/{id}") {
            controller.respondWithUserDetails(call)
        }
    }
}

fun insertModelData(userService: UserService) {
    userService.addUser(
        "Alice", LocalDate.of(2001, 1, 1)
    )
    userService.addUser(
        "Bob", LocalDate.of(2002, 2, 2)
    )
    userService.addUser(
        "Charlie", LocalDate.of(2003, 3, 3)
    )
    userService.addUser(
        "Diana", LocalDate.of(2004, 4, 4)
    )
    userService.addUser(
        "Evan", LocalDate.of(2005, 5, 5)
    )
}
