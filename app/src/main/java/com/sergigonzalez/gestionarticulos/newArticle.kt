package com.sergigonzalez.gestionarticulos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.sergigonzalez.gestionarticulos.data.Article
import com.sergigonzalez.gestionarticulos.data.ArticleApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class newArticle : AppCompatActivity() {

    private lateinit var nombre_et: EditText
    private lateinit var precio_et: EditText
    private lateinit var descripcion_et: EditText
    private lateinit var save_btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_article)

        var idArticle: String? = null

        nombre_et = findViewById(R.id.edtCode)
        precio_et = findViewById(R.id.edtPVP)
        descripcion_et = findViewById(R.id.edtDes)
        save_btn = findViewById(R.id.save_btn)

        if (intent.hasExtra("Article")) {
            val article = intent.extras?.getSerializable("Article") as Article

            nombre_et.setText(article.idArticle)
            precio_et.setText(article.priceArticle.toString())
            descripcion_et.setText(article.descriptionArticle)
            idArticle = article.idArticle
        }

        val database = ArticleApp.getDatabase(this)

        save_btn.setOnClickListener {
            val nombre = nombre_et.text.toString()
            val precio = precio_et.text.toString()
            val descripcion = descripcion_et.text.toString()

            val article = Article(nombre, precio, descripcion, 0.0, 0)

            if (idArticle != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    article.idArticle = idArticle

                    database.Articles().update(article)

                    this@newArticle.finish()
                }
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    database.Articles().insertAll(article)

                    this@newArticle.finish()
                }
            }
        }
    }
}