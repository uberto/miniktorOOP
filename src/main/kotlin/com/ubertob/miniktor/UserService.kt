package com.ubertob.miniktor

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

fun addUser(db: Database, name: String, dateOfBirth: LocalDate): User {
    var newUser: User? = null

    transaction(db) {
        // Assuming that you've called Database.connect somewhere before
        val userId = Users.insert {
            it[Users.name] = name
            it[Users.dateOfBirth] = dateOfBirth
        } get Users.id

        newUser = User(userId.value, name, dateOfBirth)
    }

    return newUser ?: throw IllegalStateException("User could not be created")
}

fun getUserById(db: Database, id: Int): Outcome<User> =
    try {
        transaction(db) {
            Users.select { Users.id eq id }
                .map { User(it[Users.id].value, it[Users.name], it[Users.dateOfBirth]) }
                .singleOrNull()
        }?.let { Success(it) } ?: Failure("User $id not found!")

    } catch (e: Exception) {
        Failure("Failure while retrieving user $id - $e")
    }

fun getAllUsers(db: Database): List<User> = transaction(db) {
    Users.selectAll().map {
        User(
            id = it[Users.id].value,
            name = it[Users.name],
            dateOfBirth = it[Users.dateOfBirth]
        )
    }
}

