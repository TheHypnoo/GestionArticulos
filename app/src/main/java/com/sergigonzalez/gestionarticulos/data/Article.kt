package com.sergigonzalez.gestionarticulos.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.io.Serializable

@Entity(tableName = "Articles",
    indices = [Index(value = ["idArticle"],unique = true)])
class Article(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var _id: Int,
    @ColumnInfo(name = "idArticle")
    var idArticle: String,
    @ColumnInfo(name = "descriptionArticle")
    var descriptionArticle: String,
    @ColumnInfo(name = "familyArticle")
    var familyArticle: String,
    @ColumnInfo(name = "priceArticle")
    var priceArticle: Double,
    @ColumnInfo(name = "stockArticle")
    var stockArticle: Int
) : Serializable