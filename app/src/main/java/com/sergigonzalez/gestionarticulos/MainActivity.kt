package com.sergigonzalez.gestionarticulos

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sergigonzalez.gestionarticulos.data.Article
import com.sergigonzalez.gestionarticulos.data.ArticleApp

class MainActivity : AppCompatActivity() {
    private lateinit var lista: ListView
    private lateinit var addArticle: FloatingActionButton
    private lateinit var database: ArticleApp
    private var listaArticles: List<Article> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lista = findViewById(R.id.lista)
        addArticle = findViewById(R.id.addArticle)
        database = ArticleApp.getDatabase(this)

        database.Articles().getAll().observe(this, Observer {
            listaArticles = it

            val adapter = ArticleAdapter(this, listaArticles)

            lista.adapter = adapter

        })

        lista.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, newArticle::class.java)
            intent.putExtra("Article", listaArticles[position])
            intent.putExtra("Edit",true)
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

            R.id.AllArticles -> {
                database.Articles().getAll().observe(this, Observer {
                    listaArticles = it
                    val adapter = ArticleAdapter(this, listaArticles)

                    lista.adapter = adapter

                })
            }

            R.id.ArticlesWithDescription -> {
                database.Articles().getDescription().observe(this, Observer {
                    listaArticles = it
                    val adapter = ArticleAdapter(this, listaArticles)

                    lista.adapter = adapter

                })
            }


            R.id.NoHaveStock -> {
                database.Articles().getWithoutStock().observe(this, Observer {
                    listaArticles = it
                    val adapter = ArticleAdapter(this, listaArticles)

                    lista.adapter = adapter

                })

            }

            R.id.HaveStock -> {
                database.Articles().getWithStock().observe(this, Observer {
                    listaArticles = it
                    val adapter = ArticleAdapter(this, listaArticles)

                    lista.adapter = adapter

                })
            }

            R.id.Ascendent -> {
                database.Articles().getAllAsc().observe(this, Observer {
                    listaArticles = it
                    val adapter = ArticleAdapter(this, listaArticles)

                    lista.adapter = adapter

                })
            }

            R.id.Descendent -> {
                database.Articles().getAllDesc().observe(this, Observer {
                    listaArticles = it
                    val adapter = ArticleAdapter(this, listaArticles)

                    lista.adapter = adapter

                })
            }
        }

        return super.onOptionsItemSelected(item)
    }
}