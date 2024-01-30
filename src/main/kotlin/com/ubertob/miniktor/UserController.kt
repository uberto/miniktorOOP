package com.ubertob.miniktor

import io.ktor.html.*
import io.ktor.http.*
import org.jetbrains.exposed.sql.Transaction

fun Transaction.getAllUsersPage(): HtmlContent {
    val usersRes = getAllUsers()

    return when (usersRes) {
        is Success -> HtmlContent(HttpStatusCode.OK, usersPage(usersRes.value)).asSuccess()
        is Failure -> ResponseError("Error getting users", HttpStatusCode.NotFound, usersRes.error).asFailure()
    }.orThrow()
}

fun Transaction.getUserPage(id: Int?): HtmlContent =
    if (id == null) {
        ResponseError("Invalid ID format", HttpStatusCode.BadRequest)
            .asFailure()
    } else {
        val userRes = getUserById(id)
        when (userRes) {
            is Success -> HtmlContent(HttpStatusCode.OK, userPage(userRes.value)).asSuccess()
            is Failure -> ResponseError("User not found", HttpStatusCode.NotFound, userRes.error).asFailure()
        }
    }.orThrow()


