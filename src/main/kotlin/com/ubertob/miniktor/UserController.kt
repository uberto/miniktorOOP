package com.ubertob.miniktor

import io.ktor.html.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.InternalServerError
import io.ktor.http.HttpStatusCode.Companion.OK
import org.jetbrains.exposed.sql.Transaction

fun Transaction.getAllUsersPage(): HtmlContent =
    when (val usersRes = getAllUsers()) {
        is Success -> HtmlContent(OK, usersPage(usersRes.value)).asSuccess()
        is Failure -> ResponseError("Error getting users", HttpStatusCode.NotFound, usersRes.error).asFailure()
    }.orThrow()


fun Transaction.getUserPage(id: Int?): HtmlContent =
    id.failIfNull(ResponseError("Invalid ID format", BadRequest))
        .bind(::getUserById)
        .transform(::htmlUserPage)
        .recover(::htmlForError)

private fun htmlUserPage(it: User) = HtmlContent(OK, userPage(it))

fun htmlForError(error: Error): HtmlContent =
    when (error) {
        is ResponseError -> HtmlContent(error.statusCode, errorPage(error.msg))
        else -> HtmlContent(InternalServerError, errorPage(error.msg))
    }


