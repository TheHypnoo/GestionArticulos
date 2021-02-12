package com.sergigonzalez.gestionarticulos.data

import androidx.room.*
import org.jetbrains.annotations.NotNull
import java.io.Serializable
import java.sql.Date


@Entity(tableName = "Movement",foreignKeys = [ForeignKey(entity = Article::class,
        parentColumns = arrayOf("idArticle"),
        childColumns = arrayOf("idArticleMovement"),
        onDelete = ForeignKey.CASCADE)])
class Movement (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var _id: Int = 0,
    var idArticleMovement: String = "idArticleMovement",
    @ColumnInfo(name = "day")
    val day: String = "01/01/2000",
    @ColumnInfo(name = "quantity")
    val quantity: Int = 1,
    @ColumnInfo(name = "type")
    val type: Char = 'E',
) : Serializable