package com.ubertob.miniktor

import io.ktor.html.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class UserController(private val userService: UserService, private val userView: UserView) {
    suspend fun respondWithAllUsers(call: ApplicationCall) {
        val users = userService.getAllUsers()
        val response = HtmlContent(HttpStatusCode.OK, userView.usersPage(users))
        call.respond(response)
    }

}
