package com.ubertob.miniktor

import io.ktor.html.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class UserController(private val userService: UserService, private val userView: UserView) {
    suspend fun returnAllUsers(call: ApplicationCall) {
        val users = userService.getAllUsers()
        val response = HtmlContent(HttpStatusCode.OK, userView.usersPage(users))
        call.respond(response)
    }

    suspend fun returnUserDetails(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull()

        if (id == null) {
            call.respond(
                HtmlContent(
                    HttpStatusCode.BadRequest, userView.errorPage("Invalid ID format")
                )
            )
            return
        }

        val user = userService.getUserById(id)
        val response = if (user != null) {
            HtmlContent(HttpStatusCode.OK, userView.userPage(user))
        } else {
            HtmlContent(HttpStatusCode.NotFound, userView.errorPage("User not found"))
        }

        call.respond(response)

    }

}
