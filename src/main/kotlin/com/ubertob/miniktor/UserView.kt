package com.ubertob.miniktor

import kotlinx.html.*


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

fun userPage(user: User): HTML.() -> Unit = {
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
        // You might have a link to go back to the user list
        a(href = "/users") {
            +"Back to user list"
        }
    }
}

fun errorPage(errorMessage: String): HTML.() -> Unit = {
    head {
        title("Error")
    }
    body {
        h1 { +"Error" }
        p {
            +"An error occurred: $errorMessage"
        }
        // Link to go back to the user list or home
        a(href = "/users") {
            +"Back to user list"
        }
    }
}


