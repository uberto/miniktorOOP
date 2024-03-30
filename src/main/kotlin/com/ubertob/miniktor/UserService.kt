package com.ubertob.miniktor

import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

fun Transaction.getAllUsers(): List<User> =
    Users.selectAll().map {
        User(
            id = it[Users.id].value,
            name = it[Users.name],
            dateOfBirth = it[Users.dateOfBirth]
        )
    }

fun Transaction.getUserById(id: Int): User? =
    Users.select { Users.id eq id }
        .map { User(it[Users.id].value, it[Users.name], it[Users.dateOfBirth]) }
        .singleOrNull()

fun Transaction.addUser(name: String, dateOfBirth: LocalDate): User {
    val userId = Users.insert {
        it[Users.name] = name
        it[Users.dateOfBirth] = dateOfBirth
    } get Users.id

    return User(userId.value, name, dateOfBirth)
}

