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
    val db = initDatabase()
    val userService = UserService()
    val userView = UserView()
    val controller = UserController(userService, userView)
    insertModelData(userService)

    val userFetcher = partialApply(::getUserById, db)
    val webServer = httpRoutes(userView, controller, userFetcher)
    webServer.start(wait = true)
}

private fun httpRoutes(
    userView: UserView,
    controller: UserController,
    userFetcher: (Int) -> Outcome<User>
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
            // req -> id
            // id -> user
            // user -> pageHtml
            // page -> response

            getIdFromReq(call)
                .bind { id -> userFetcher(id) }
                .transform { user -> getUserPage(user) }
                .recover { msg -> HtmlContent(HttpStatusCode.NotFound, errorPage(msg)) }
                .also { html -> call.respond(html) }

        }
    }
}

fun <T> Outcome<T>.recover(fn: (String) -> T): T =
    when (this) {
        is Failure -> fn(msg)
        is Success -> value
    }


fun getIdFromReq(applicationCall: ApplicationCall): Outcome<Int> =
    applicationCall.parameters["id"]?.toIntOrNull()?.asSuccess()
        ?: Failure("id not present!")


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


fun <A, B, C> partialApply(fn: (A, B) -> C, a: A): (B) -> C =
    { b: B -> fn(a, b) }


fun <T> T.asSuccess(): Success<T> = Success(this)

sealed interface Outcome<out T> {
    fun <U> transform(fn: (T) -> U): Outcome<U> =
        when (this) {
            is Failure -> this
            is Success -> Success(fn(this.value))
        }


    fun <U> bind(fn: (T) -> Outcome<U>): Outcome<U> =
        when (this) {
            is Failure -> this
            is Success -> fn(this.value)
        }

}

data class Success<T>(val value: T) : Outcome<T>
data class Failure(val msg: String) : Outcome<Nothing>