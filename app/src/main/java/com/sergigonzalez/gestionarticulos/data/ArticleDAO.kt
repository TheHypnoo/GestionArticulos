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

    @Insert
    fun insertAll(vararg Articles: Article)

    @Insert
    fun insertArticle(idArticle: String,descriptionArticle: String,familyArticle: String,priceArticle: Double,stockArticle: Int)

    @Update
    fun update(Article: Article)

    @Update
    fun updateMovement(Movement: Movement)

    @Insert
    fun insertMovement(Movement: Movement)

    @Delete
    fun delete(Article: Article)

}