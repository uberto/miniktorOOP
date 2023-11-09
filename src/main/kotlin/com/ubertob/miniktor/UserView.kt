package com.ubertob.miniktor

import kotlinx.html.*

object UserView {
    fun indexHtml(): HTML.() -> Unit = {
        head {
            title("Welcome to MiniKtor")
        }
        body {
            h1 {
                +"Pages"
            }
            div {
                a("users") { +"User List" }
            }
        }
    }


    fun usersPage(users: List<User>): HTML.() -> Unit = {
        head {
            title("Users")
            link(rel = "stylesheet", href = "/static/styles.css", type = "text/css") // Assuming you have some CSS
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
