package com.ubertob.miniktor

import io.ktor.html.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.OK
import org.jetbrains.exposed.sql.Transaction

fun Transaction.getAllUsersPage(): HtmlContent {
    val usersRes = getAllUsers()

    return when (usersRes) {
        is Success -> HtmlContent(HttpStatusCode.OK, usersPage(usersRes.value)).asSuccess()
        is Failure -> ResponseError("Error getting users", HttpStatusCode.NotFound, usersRes.error).asFailure()
    }.orThrow()
}


fun Transaction.getUserPage(id: Int?): HtmlContent =
    id.failIfNull(ResponseError("Invalid ID format", BadRequest))
        .transform { getUserById(it).orThrow() }
        .transform { HtmlContent(OK, userPage(it)) }
        .orThrow()

