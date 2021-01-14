package com.sergigonzalez.gestionarticulos

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sergigonzalez.gestionarticulos.data.Article
import com.sergigonzalez.gestionarticulos.data.ArticleApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var lista: ListView
    private lateinit var addArticle: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lista = findViewById(R.id.lista)
        addArticle = findViewById(R.id.addArticle)


        var listaArticles: List<Article> = emptyList()

        val database = ArticleApp.getDatabase(this)

        database.Articles().getAll().observe(this, Observer {
            listaArticles = it

            val adapter = ArticleAdapter(this, listaArticles)

            lista.adapter = adapter

        })

        lista.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, ArticleActivity::class.java)
            intent.putExtra("id", listaArticles[position].idArticle)
            startActivity(intent)
        }


        addArticle.setOnClickListener {
            val intent = Intent(this, newArticle::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)

        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.NoHaveStock -> {

            }

            R.id.HaveStock -> {

            }

            R.id.Ascendent -> {

            }

            R.id.Descendent -> {

            }
        }

        return super.onOptionsItemSelected(item)
    }
}