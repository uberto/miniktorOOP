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
    initDatabase()
    val userService = UserService()
    insertFakeData(userService)
    val userView = UserView()
    val controller = UserController(userService, userView)
    embeddedServer(Netty, port = 8080) {
        routing {
            staticResources("/static", "static")

            get("/") {
                call.respond(HtmlContent(HttpStatusCode.OK, userView.indexHtml()))
            }

            get("/users") {
                call.respond(controller.getAllUsers())
            }

            get("/user/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                call.respond(controller.getUserById(id))
            }
        }
    }.start(wait = true)
}

fun insertFakeData(userService: UserService) {
    userService.addUser(
        "Alice", LocalDate.of(1999, 11, 11)
    )
    userService.addUser(
        "Bob", LocalDate.of(2001, 1, 31)
    )
}
