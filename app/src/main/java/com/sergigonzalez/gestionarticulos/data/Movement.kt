package com.sergigonzalez.gestionarticulos.data

import androidx.room.*
import org.jetbrains.annotations.NotNull
import java.io.Serializable
import java.sql.Date


@Entity(tableName = "Movement",foreignKeys = [ForeignKey(entity = Article::class,
        parentColumns = arrayOf("_id"),
        childColumns = arrayOf("_idTableArticle"),
        onDelete = ForeignKey.CASCADE)])
class Movement (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    @NotNull
    var _id: Int,
    var idArticle: String,
    @ColumnInfo(name = "day")
    val day: String,
    @ColumnInfo(name = "quantity")
    val quantity: Int,
    @ColumnInfo(name = "type")
    val type: Char,
    @ColumnInfo(name = "_idTableArticle")
    var _idTableArticle: Int
) : Serializable