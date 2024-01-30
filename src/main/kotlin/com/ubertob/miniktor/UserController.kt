package com.ubertob.miniktor

import io.ktor.html.*
import io.ktor.http.*
import org.jetbrains.exposed.sql.Transaction

fun Transaction.getAllUsersPage(): HtmlContent {
        val users = getAllUsers()
        return HtmlContent(HttpStatusCode.OK, usersPage(users))
    }

    fun Transaction.getUserPage(id: Int?): HtmlContent {
        if (id == null) {
            return HtmlContent(HttpStatusCode.BadRequest, errorPage("Invalid ID format"))
        }
        val user = getUserById(id)
        if (user != null) {
            return HtmlContent(HttpStatusCode.OK, userPage(user))
        } else {
            return HtmlContent(HttpStatusCode.NotFound, errorPage("User not found"))
        }
    }


