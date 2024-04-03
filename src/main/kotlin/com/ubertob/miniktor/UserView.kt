package com.ubertob.miniktor

import kotlinx.html.*


class UserView {
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
            link(rel = "stylesheet", href = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css")
        }
        body {
            h1 { +"User List" }
            div(classes = "list-group") {
                ul {
                    for (user in users) {
                        li(classes = "list-group-item") {
                            a("/user/${user.id}") {
                                +"${user.name} born on ${user.dateOfBirth}"
                            }

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
            link(rel = "stylesheet", href = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css")
        }
        body {
            div("container mt-5") {
                h1 { +"User Details" }
                div(classes = "card") {
                    div(classes = "card-body") {
                        h5(classes = "card-title") { +"Name: ${user.name}" }
                        p(classes = "card-text") { +"ID: ${user.id}" }
                        p(classes = "card-text") { +"Date of Birth: ${user.dateOfBirth}" }

                    }
                }
                a(href = "/users") {
                    +"Back to user list"
                }
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

fun userPage(user: User): HTML.() -> Unit = {
    head {
        title("User Details")
        link(rel = "stylesheet", href = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css")
    }
    body {
        div("container mt-5") {
            h1 { +"User Details" }
            div(classes = "card") {
                div(classes = "card-body") {
                    h5(classes = "card-title") { +"Name: ${user.name}" }
                    p(classes = "card-text") { +"ID: ${user.id}" }
                    p(classes = "card-text") { +"Date of Birth: ${user.dateOfBirth}" }

                }
            }
            a(href = "/users") {
                +"Back to user list"
            }
        }
    }
}



