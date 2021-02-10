package com.sergigonzalez.gestionarticulos

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sergigonzalez.gestionarticulos.adapters.ArticleAdapter
import com.sergigonzalez.gestionarticulos.data.Article
import com.sergigonzalez.gestionarticulos.data.ArticleApp

class MainActivity : AppCompatActivity() {

    private lateinit var lista: ListView
    private lateinit var addArticle: FloatingActionButton
    private lateinit var database: ArticleApp
    private var listArticles: List<Article> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lista = findViewById(R.id.lista)
        addArticle = findViewById(R.id.addArticle)
        database = ArticleApp.getDatabase(this)

        database.Articles().getAll().observe(this, {
            listArticles = it

            val adapter = ArticleAdapter(this, listArticles)

            lista.adapter = adapter

        })

        lista.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, NewArticle::class.java)
            intent.putExtra("Article", listArticles[position])
            intent.putExtra("Edit", true)
            startActivity(intent)
        }

        addArticle.setOnClickListener {
            val intent = Intent(this, NewArticle::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflated the menu to filter
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            //All filters
            R.id.AllArticles -> {
                database.Articles().getAll().observe(this, {
                    listArticles = it
                    val adapter = ArticleAdapter(this, listArticles)

                    lista.adapter = adapter

                })
            }

            R.id.ArticlesWithDescription -> {
                database.Articles().getDescription().observe(this, {
                    listArticles = it
                    val adapter = ArticleAdapter(this, listArticles)

                    lista.adapter = adapter

                })
            }

            R.id.NoHaveStock -> {
                database.Articles().getWithoutStock().observe(this, {
                    listArticles = it
                    val adapter = ArticleAdapter(this, listArticles)

                    lista.adapter = adapter

                })

            }

            R.id.HaveStock -> {
                database.Articles().getWithStock().observe(this, {
                    listArticles = it
                    val adapter = ArticleAdapter(this, listArticles)

                    lista.adapter = adapter

                })
            }

            R.id.Ascendent -> {
                database.Articles().getAllAsc().observe(this, {
                    listArticles = it
                    val adapter = ArticleAdapter(this, listArticles)

                    lista.adapter = adapter

                })
            }

            R.id.Descendent -> {
                database.Articles().getAllDesc().observe(this, {
                    listArticles = it
                    val adapter = ArticleAdapter(this, listArticles)

                    lista.adapter = adapter

                })
            }

            //Intent Movements All
            R.id.Movements -> {
                startActivity(Intent(this, MovementsAll::class.java))
            }
            R.id.Weather -> {
                startActivity(Intent(this, WeatherActivity::class.java))
            }
        }



        return super.onOptionsItemSelected(item)
    }
}