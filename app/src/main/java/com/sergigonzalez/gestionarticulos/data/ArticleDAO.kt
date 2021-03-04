package com.sergigonzalez.gestionarticulos.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ArticleDAO {

    //getAll

    @Query("SELECT * FROM Articles")
    fun getAll(): LiveData<List<Article>>

    @Query("SELECT * FROM MOVEMENT")
    fun getAllMovements(): LiveData<List<Movement>>

    //Filtros para el Articulo

    @Query("SELECT * FROM Articles WHERE idArticle = :id")
    fun get(id: String?): LiveData<Article>

    @Query("SELECT * FROM Articles ORDER BY :order")
    fun getAllOrder(order: String): LiveData<List<Article>>

    @Query("SELECT * FROM Articles WHERE descriptionArticle LIKE '%' || :Word || '%' ORDER BY :order")
    fun getDescriptionWithWord(Word: String, order: String): LiveData<List<Article>>

    @Query("SELECT * FROM Articles WHERE descriptionArticle LIKE '%' || :Word || '%' AND stockArticle <= 0 ORDER BY :order")
    fun getDescriptionWithWordAndStock(Word: String, order: String): LiveData<List<Article>>

    @Query("SELECT * FROM Articles WHERE stockArticle != 0")
    fun getWithStock(): LiveData<List<Article>>

    @Query("SELECT * FROM Articles WHERE stockArticle <= 0 ORDER BY :order")
    fun getWithoutStock(order: String): LiveData<List<Article>>

    @Query("SELECT * FROM Articles ORDER BY priceArticle DESC")
    fun getPriceLow(): LiveData<List<Article>>

    @Query("SELECT * FROM Articles ORDER BY priceArticle ASC")
    fun getPriceHigh(): LiveData<List<Article>>


    //Movimientos del articulo

    @Query("SELECT * FROM Movement WHERE day <= :dateF AND idArticleMovement = :idArticle ORDER BY day DESC")
    fun dateF(idArticle: String, dateF: String): LiveData<List<Movement>>

    @Query("SELECT * FROM Movement WHERE day >= :dateI AND idArticleMovement = :idArticle ORDER BY day DESC")
    fun dateI(idArticle: String, dateI: String): LiveData<List<Movement>>

    @Query("SELECT * FROM Movement WHERE day BETWEEN :dateI AND :dateF AND idArticleMovement = :idArticle ORDER BY day DESC")
    fun dateIDateF(idArticle: String, dateF: String, dateI: String): LiveData<List<Movement>>

    //Movimientos de todos los articulos
    @Query("SELECT * FROM  Articles INNER JOIN Movement ON idArticle = idArticleMovement WHERE idArticle = :idArticle ORDER BY idArticle ASC")
    fun dateMovements(idArticle: String): LiveData<List<Movement>>

    @Query("SELECT * FROM Articles INNER JOIN Movement ON idArticle = idArticleMovement ORDER BY idArticle ASC")
    fun dateMovementsAll(): LiveData<List<Movement>>


    //Extras

    @Insert
    fun insertAll(vararg Articles: Article)

    @Update
    fun update(Article: Article)

    @Insert
    fun insertMovement(vararg Movement: Movement)

    @Delete
    fun delete(Article: Article)

}