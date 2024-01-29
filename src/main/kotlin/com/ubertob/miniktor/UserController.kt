package com.ubertob.miniktor

import io.ktor.html.*
import io.ktor.http.*

class UserController(private val userService: UserService, private val userView: UserView) {
    fun getAllUsers(): HtmlContent {
        val users = userService.getAllUsers()
        return HtmlContent(HttpStatusCode.OK, userView.usersPage(users))
    }

    fun getUserById(id: Int?): HtmlContent {
        if (id == null) {
            return HtmlContent(HttpStatusCode.BadRequest, userView.errorPage("Invalid ID format"))
        }

        val user = userService.getUserById(id)
        if (user != null) {
            return HtmlContent(HttpStatusCode.OK, userView.userPage(user))
        } else {
            return HtmlContent(HttpStatusCode.NotFound, userView.errorPage("User not found"))
        }


    }

}
