package com.ubertob.miniktor

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

class UserService {
    fun getAllUsers(): List<User> = transaction {
        Users.selectAll().map {
            User(
                id = it[Users.id].value,
                name = it[Users.name],
                dateOfBirth = it[Users.dateOfBirth]
            )
        }
    }


    fun addUser(name: String, dateOfBirth: LocalDate): User {
        var newUser: User? = null

        transaction {
            // Assuming that you've called Database.connect somewhere before
            val userId = Users.insert {
                it[Users.name] = name
                it[Users.dateOfBirth] = dateOfBirth
            } get Users.id

            newUser = User(userId.value, name, dateOfBirth)
        }

        return newUser ?: throw IllegalStateException("User could not be created")
    }



}

fun getUserById(db: Database, id: Int): Outcome<User> =
    try {
        transaction(db) {
                Users.select { Users.id eq id }
                    .map { User(it[Users.id].value, it[Users.name], it[Users.dateOfBirth]) }
                    .singleOrNull()
            }?.asSuccess() ?: Failure("User $id not present!")
    } catch (e: Exception){
        Failure("Error during db: $e")
    }



