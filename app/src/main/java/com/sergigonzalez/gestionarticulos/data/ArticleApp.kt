package com.sergigonzalez.gestionarticulos.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [Article::class, Movement::class], version = 4)
//@Database(entities = [Article::class], version = 1)
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
                ).fallbackToDestructiveMigration().build()
                //.addMigrations(MIGRATION_1_2).build()

                INSTANCE = instance

                return instance
            }
        }
        /*
        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                    //Todavía debo probar...
                        database.execSQL("DROP TABLE Article")
                        database.execSQL("DROP TABLE Movement")
                        database.execSQL("CREATE TABLE Article (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "idArticle TEXT NOT NULL," +
                            "descriptionArticle TEXT NOT NULL," +
                            "familyArticle TEXT NOT NULL," +
                            "priceArticle REAL NOT NULL," +
                            "stockArticle INTEGER NOT NULL DEFAULT 0)")
                    database.execSQL(
                        "CREATE TABLE Movement (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "idArticleMovement TEXT NOT NULL," +
                                "dayArticle TEXT NOT NULL," +
                                "quantity INTEGER NOT NULL," +
                                "type VARCHAR(1) NOT NULL," +
                                "idArticle INTEGER NOT NULL," +
                                "FOREIGN KEY(idArticle) REFERENCES Article(_id) ON DELETE CASCADE)"
                    )
            }
         */
        //}
    }
}