package com.ubertob.miniktor

import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

fun Transaction.getAllUsers(): Result<List<User>> =
    try {
        Users.selectAll().map {
            User(
                id = it[Users.id].value,
                name = it[Users.name],
                dateOfBirth = it[Users.dateOfBirth]
            )
        }.asSuccess()
    } catch (e: Exception) {
        DbError("Error loading all users", e).asFailure()
    }

fun Transaction.getUserById(id: Int): Result<User> =
    try {
        Users.select { Users.id eq id }
            .map {
                User(
                    id = it[Users.id].value,
                    name = it[Users.name],
                    dateOfBirth = it[Users.dateOfBirth]
                )
            }
            .single().asSuccess()
    } catch (e: Exception) {
        DbError("Error loading user $id", e).asFailure()
    }

fun Transaction.addUser(name: String, dateOfBirth: LocalDate): User {
    val userId = Users.insert {
        it[Users.name] = name
        it[Users.dateOfBirth] = dateOfBirth
    } get Users.id

    return User(userId.value, name, dateOfBirth)
}

