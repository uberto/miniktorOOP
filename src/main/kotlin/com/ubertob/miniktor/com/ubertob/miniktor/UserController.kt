package com.ubertob.miniktor.com.ubertob.miniktor

import com.ubertob.miniktor.User
import com.ubertob.miniktor.Users
import io.ktor.html.HtmlContent
import io.ktor.http.*
import kotlinx.html.*
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun getUserById(id: Int?): HtmlContent =
    id.failIfNull("Invalid ID format")
        .bind(::fetchUserFromDb)
        .map(::userResponse)
        .recoverWith { errorResponse("User not found! Reason: ${it.message}") }



fun getAllUsers(): HtmlContent {
    val users = transaction {
        Users.selectAll().map {
            User(
                id = it[Users.id].value,
                name = it[Users.name],
                dateOfBirth = it[Users.dateOfBirth]
            )
        }
    }
    return HtmlContent(HttpStatusCode.OK) {
        head {
            title("Users")
            link(
                rel = "stylesheet",
                href = "/static/styles.css",
                type = "text/css"
            ) // Assuming you have some CSS
        }
        body {
            h1 { +"User List" }
            div(classes = "user-list") {
                ul {
                    for (user in users) {
                        li {
                            +"${user.name} born on ${user.dateOfBirth}"
                        }
                    }
                }
            }
            // Other HTML content and scripts can be added here
        }
    }
}





fun fetchUserFromDb(id: Int?): Result<User> = Result.runCatching {
    transaction {
        Users.select { Users.id eq id }
            .map { User(it[Users.id].value, it[Users.name], it[Users.dateOfBirth]) }
            .single()
    }
}

fun userResponse(user: User): HtmlContent =
    HtmlContent(HttpStatusCode.OK) {
        head {
            title("User Details")
            link(rel = "stylesheet", href = "/static/styles.css", type = "text/css")
        }
        body {
            h1 { +"User Details" }
            div(classes = "user-details") {
                h2 { +"ID: ${user.id}" }
                p { +"Name: ${user.name}" }
                p { +"Date of Birth: ${user.dateOfBirth}" }
            }
            a(href = "/users") {
                +"Back to user list"
            }
        }
    }

fun errorResponse(message: String) = HtmlContent(HttpStatusCode.BadRequest) {
    head {
        title("Error")
    }
    body {
        h1 { +"Error" }
        p {
            +"An error occurred: $message"
        }
        // Link to go back to the user list or home
        a(href = "/users") {
            +"Back to user list"
        }
    }
}

fun <T : Any> T?.failIfNull(errorMessage: String = "Value is null"): Result<T> {
    return if (this != null) {
        Result.success(this)
    } else {
        Result.failure(NullPointerException(errorMessage))
    }
}

private fun <T, U> Result<T>.bind(function: (T) -> Result<U>): Result<U> =
    if (isSuccess) function(getOrThrow()) else function(getOrThrow())

private fun <T> Result<T>.recoverWith(fn: (Throwable) -> T): T =
    when (val exception = exceptionOrNull()) {
        null -> getOrNull()!!
        else -> fn(exception)
    }


