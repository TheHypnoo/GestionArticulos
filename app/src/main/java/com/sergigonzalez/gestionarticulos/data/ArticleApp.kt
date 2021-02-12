package com.sergigonzalez.gestionarticulos.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Article::class, Movement::class], version = 2)
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

                //.fallbackToDestructiveMigration().build()

                INSTANCE = instance

                return instance
            }
        }

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //Si la version es inferior, creo la nueva tabla de Movements para implementar las nuevas funcionalidades!
                if (database.version < 1) {
                    database.execSQL(
                        "CREATE TABLE Movement (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "idArticleMovement TEXT NOT NULL," +
                                "day TEXT NOT NULL," +
                                "quantity INTEGER NOT NULL," +
                                "type VARCHAR(1) NOT NULL," +
                                "FOREIGN KEY(idArticleMovement) REFERENCES Article(idArticle) ON DELETE CASCADE)"
                    )
                }
            }
        }
    }
}