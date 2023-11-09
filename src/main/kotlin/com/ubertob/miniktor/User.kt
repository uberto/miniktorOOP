package com.ubertob.miniktor

import java.time.LocalDate

data class User(
    val id: Int,
    val name: String,
    val dateOfBirth: LocalDate
)