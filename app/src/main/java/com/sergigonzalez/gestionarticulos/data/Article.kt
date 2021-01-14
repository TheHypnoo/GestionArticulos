package com.sergigonzalez.gestionarticulos.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Articles")
class Article(
    @PrimaryKey(autoGenerate = false)
    var idArticle: String,
    val descriptionArticle: String,
    val familyArticle: String,
    val priceArticle: Double,
    var stockArticle: Int
) : Serializable