package com.sergigonzalez.gestionarticulos.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [Article::class], version = 2)
abstract class ArticleApp : RoomDatabase() {

    abstract fun Articles(): ArticleDAO

    companion object {
        @Volatile
        private var INSTANCE: ArticleApp? = null

        fun getDatabase(context: Context): ArticleApp {
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        ArticleApp::class.java,
                        "Articles"
                ).addMigrations(MIGRATION_1_2).build()

                INSTANCE = instance

                return instance
            }
        }
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Since we didn't alter the table, there's nothing else to do here.
            }
        }
    }
}