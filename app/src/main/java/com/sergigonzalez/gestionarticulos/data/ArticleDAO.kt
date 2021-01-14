package com.sergigonzalez.gestionarticulos.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ArticleDAO {
    @Query("SELECT * FROM Articles")
    fun getAll(): LiveData<List<Article>>

    @Query("SELECT * FROM Articles WHERE idArticle = :id")
    fun get(id: String?): LiveData<Article>

    @Insert
    fun insertAll(vararg Articles: Article)

    @Update
    fun update(Article: Article)

    @Delete
    fun delete(Article: Article)

}