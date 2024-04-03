package com.ubertob.miniktor

import io.ktor.html.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.HTML
import org.jetbrains.exposed.sql.Database
import java.time.LocalDate

fun main() {
    val db = initDatabase()
    insertModelData(db)

    val webServer = httpRoutes(DbRunner(db, ::getUserById), DbRunner1(db, ::getAllUsers))
    webServer.start(wait = true)
}

private fun httpRoutes(
    userFetcher: (Int) -> Outcome<User>,
    allUsersFetcher: () -> List<User>
) = embeddedServer(Netty, port = 8080) {
    routing {
        staticResources("/static", "static")

        get("/") {
            call.respond(okHttpContent(indexHtml()))
        }

        get("/users") {
            val response = allUsersPage(allUsersFetcher())
            call.respond(response)

        }

        get("/user/{id}") {
//            req -> id
//            id -> User
//            User -> Html
//            Html -> HttpResp

            call.respond(
                getUserId(call.parameters)
                    .bind(userFetcher)
                    .transform(::userPage)
                    .transform(::okHttpContent)
                    .recover{ msg -> HtmlContent(HttpStatusCode.NotFound, errorPage(msg) )}
            )
        }
    }
}



private fun allUsersPage(users: List<User>) =
    HtmlContent(HttpStatusCode.OK, usersPage(users))

private fun okHttpContent(htmlFn: HTML.() -> Unit) = HtmlContent(HttpStatusCode.OK, htmlFn)

private fun getUserId(parameters: Parameters): Outcome<Int> =
    parameters["id"]?.toIntOrNull()?.let { Success(it) } ?: Failure("No id in the parameters!")


fun insertModelData(db: Database) {
    addUser(db, "Alice", LocalDate.of(2001, 1, 1))
    addUser(db, "Bob", LocalDate.of(2002, 2, 2))
    addUser(db, "Charlie", LocalDate.of(2003, 3, 3))
    addUser(db, "Diana", LocalDate.of(2004, 4, 4))
    addUser(db, "Evan", LocalDate.of(2005, 5, 5))
}



