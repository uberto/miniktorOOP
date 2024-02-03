package com.ubertob.miniktor

import io.ktor.html.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

fun main() {
    val db = initDatabase()
    insertSomeData()

    val userPageFromDb = inTransaction(db, Transaction::getUserPage)
    val allUsersPageFromDb = inTransaction(db, Transaction::getAllUsersPage)

    embeddedServer(Netty, port = 8080) {
        routing {
            staticResources("/static", "static")

            get("/") {
                call.respond(HtmlContent(HttpStatusCode.OK, indexHtml()))
            }

            get("/users") {
                call.respond(allUsersPageFromDb)
            }

            get("/user/{id}") {
                val id = call.parameters["id"]
                    ?.toIntOrNull()

                val tx = TransactionRunner {userPageFromDb(id)}
                call.respond(tx.runOnDb(db))
            }
        }
    }.start(wait = true)
}

fun insertSomeData() =
    transaction {
        addUser(
            "Alice", LocalDate.of(1999, 11, 11)
        )

        addUser(
            "Bob", LocalDate.of(2001, 1, 31)
        )
    }


fun <T, R> inTransaction(db: Database, f: (Transaction).(T) -> R): (T) -> R =
    { x: T ->
        transaction(db) {
            f(x)
        }
    }

fun <R> inTransaction(db: Database, f: (Transaction).() -> R): () -> R =
    {
        transaction(db) {
            f()
        }
    }


data class TransactionRunner<T>(val f: Transaction.() -> T) {

    fun runOnDb(db: Database) = transaction(db) { f }
}