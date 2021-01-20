package com.sergigonzalez.gestionarticulos.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.io.Serializable

@Entity(tableName = "Articles")
class Article(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "idArticle")
    @NotNull
    var idArticle: String,
    @ColumnInfo(name = "descriptionArticle")
    val descriptionArticle: String,
    @ColumnInfo(name = "familyArticle")
    val familyArticle: String,
    @ColumnInfo(name = "priceArticle")
    val priceArticle: Double,
    @ColumnInfo(name = "stockArticle")
    var stockArticle: Int
) : Serializable