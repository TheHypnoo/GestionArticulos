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
    var _id: Int = 0,
    @ColumnInfo(name = "idArticle")
    var idArticle: String = "idArticle",
    @ColumnInfo(name = "descriptionArticle")
    var descriptionArticle: String = "description",
    @ColumnInfo(name = "familyArticle")
    var familyArticle: String = "family",
    @ColumnInfo(name = "priceArticle")
    var priceArticle: Double = 1.0,
    @ColumnInfo(name = "stockArticle")
    var stockArticle: Int = 1
) : Serializable