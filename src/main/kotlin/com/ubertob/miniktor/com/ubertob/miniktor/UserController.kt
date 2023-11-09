package com.ubertob.miniktor.com.ubertob.miniktor

import com.ubertob.miniktor.UserService
import com.ubertob.miniktor.UserView
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

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
