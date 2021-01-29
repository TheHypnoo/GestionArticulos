package com.sergigonzalez.gestionarticulos.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ArticleDAO {
    @Query("SELECT * FROM Articles")
    fun getAll(): LiveData<List<Article>>

    @Query("SELECT * FROM Articles ORDER BY idArticle ASC")
    fun getAllAsc(): LiveData<List<Article>>

    @Query("SELECT * FROM Articles ORDER BY idArticle DESC")
    fun getAllDesc(): LiveData<List<Article>>

    @Query("SELECT * FROM Articles WHERE idArticle = :id")
    fun get(id: String?): LiveData<Article>

    @Query("SELECT * FROM Articles WHERE descriptionArticle != ' ' ")
    fun getDescription(): LiveData<List<Article>>

    @Query("SELECT * FROM Articles WHERE stockArticle != 0")
    fun getWithStock(): LiveData<List<Article>>

    @Query("SELECT * FROM Articles WHERE stockArticle <= 0")
    fun getWithoutStock(): LiveData<List<Article>>

    @Query("SELECT * FROM Movement WHERE day <= :dateF AND idArticleMovement = :idArticle ORDER BY day DESC")
    fun dateF(idArticle: String, dateF: String): LiveData<List<Movement>>

    @Query("SELECT * FROM Movement WHERE day >= :dateI AND idArticleMovement = :idArticle ORDER BY day DESC")
    fun dateI(idArticle: String, dateI: String): LiveData<List<Movement>>

    @Query("SELECT * FROM Movement WHERE day BETWEEN :dateI AND :dateF AND idArticleMovement = :idArticle ORDER BY day DESC")
    fun dateIDateF(idArticle: String, dateF: String, dateI: String): LiveData<List<Movement>>


    @Insert
    fun insertAll(vararg Articles: Article)

    @Update
    fun update(Article: Article)

    @Update
    fun updateMovement(Movement: Movement)

    @Insert
    fun insertMovement(vararg Movement: Movement)

    @Delete
    fun delete(Article: Article)

}