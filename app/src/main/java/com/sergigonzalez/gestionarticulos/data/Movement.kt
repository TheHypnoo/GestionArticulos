package com.sergigonzalez.gestionarticulos.data

import androidx.room.*
import org.jetbrains.annotations.NotNull
import java.io.Serializable
import java.sql.Date


@Entity(tableName = "Movement",foreignKeys = [ForeignKey(entity = Article::class,
        parentColumns = arrayOf("idArticle"),
        childColumns = arrayOf("idTableArticle"),
        onDelete = ForeignKey.CASCADE)])
class Movement (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var _id: Int,
    var idArticleMovement: String,
    @ColumnInfo(name = "day")
    val day: String,
    @ColumnInfo(name = "quantity")
    val quantity: Int,
    @ColumnInfo(name = "type")
    val type: Char,
    @ColumnInfo(name = "idTableArticle")
    var idTableArticle: String
) : Serializable