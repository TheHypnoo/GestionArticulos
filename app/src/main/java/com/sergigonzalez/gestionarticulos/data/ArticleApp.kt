package com.sergigonzalez.gestionarticulos.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Article::class], version = 1)
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
                ).build()

                INSTANCE = instance

                return instance
            }
        }
    }
}