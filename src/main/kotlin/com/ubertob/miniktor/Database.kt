package com.ubertob.miniktor

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate


fun initDatabase() {
    // Replace with your actual database connection parameters
    Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
    transaction {
        SchemaUtils.create(Users)
    }
}

object Users : IntIdTable() {
    val name = varchar("name", 50)
    val dateOfBirth = date("date_of_birth")
}