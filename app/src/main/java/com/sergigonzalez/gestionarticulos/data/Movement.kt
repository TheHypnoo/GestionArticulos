package com.sergigonzalez.gestionarticulos.data

import androidx.room.*
import org.jetbrains.annotations.NotNull
import java.io.Serializable
import java.sql.Date


@Entity(tableName = "Movement",foreignKeys = [ForeignKey(entity = Article::class,
        parentColumns = arrayOf("idArticle"),
        childColumns = arrayOf("idArticle"),
        onDelete = ForeignKey.CASCADE)],
        indices = [Index(value = ["idArticle", "idArticle"])])
class Movement (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "idArticle")
    @NotNull
    var idArticle: String,
    @ColumnInfo(name = "day")
    val day: Date,
    @ColumnInfo(name = "quantity")
    val quantity: Int,
    @ColumnInfo(name = "type")
    val type: Char
) : Serializable