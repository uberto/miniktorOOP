package com.ubertob.miniktor

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.transactions.transaction


fun initDatabase(): Database =
    Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
        .also {
            transaction(it) {
                SchemaUtils.create(Users)
            }
        }


object Users : IntIdTable() {
    val name = varchar("name", 50)
    val dateOfBirth = date("date_of_birth")
}