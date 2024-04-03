package com.ubertob.miniktor

import org.jetbrains.exposed.sql.Database

data class DbRunner<B, C>(private val db: Database,
                          val dbFn: (Database, B) -> C): (B) -> C {
    override fun invoke(b: B): C = dbFn(db, b)

}

data class DbRunner1< C>(private val db: Database,
                          val dbFn: (Database) -> C): () -> C {
    override fun invoke(): C = dbFn(db)

}