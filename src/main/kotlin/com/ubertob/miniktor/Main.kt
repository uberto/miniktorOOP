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
import io.ktor.html.HtmlContent
import io.ktor.http.*
import io.ktor.server.request.*
import kotlinx.html.*
import java.time.LocalDate

fun main() {
    initDatabase()

    embeddedServer(Netty, port = 8080) {
        routing {
            staticResources("/static", "static")

            bindGET("/") { indexPage()}

            bindGET("/users") {getAllUsers()}

            bindGET("/user/{id}") {getUserById(queryParameters["id"]?.toIntOrNull())}

        }
    }.start(wait = true)
}

private fun Routing.bindGET(path: String, htmlContent: ApplicationRequest.() -> HtmlContent) {
    get(path) {
        call.respond(htmlContent(call.request))
    }
}

private fun indexPage() = HtmlContent(HttpStatusCode.OK, {
    head {
        title("Welcome to MiniKtor")
    }
    body {
        h1 {
            +"Pages"
        }
        div {
            a("users") { +"User List" }
        }
    }
})

fun insertFakeData(userService: UserService) {
    userService.addUser(
        "Alice", LocalDate.of(1999, 11, 11)
    )
    userService.addUser(
        "Bob", LocalDate.of(2001, 1, 31)
    )
}
