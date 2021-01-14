package com.sergigonzalez.gestionarticulos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.sergigonzalez.gestionarticulos.data.Article
import com.sergigonzalez.gestionarticulos.data.ArticleApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArticleActivity : AppCompatActivity() {

    private lateinit var database: ArticleApp
    private lateinit var Article: Article
    private lateinit var articleLiveData: LiveData<Article>
    private lateinit var name_Article: TextView
    private lateinit var price_Article: TextView
    private lateinit var description_Article: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        database = ArticleApp.getDatabase(this)
        name_Article = findViewById(R.id.nombre_producto)
        price_Article = findViewById(R.id.precio_producto)
        description_Article = findViewById(R.id.detalles_producto)

        val idArticle = intent.getStringExtra("id")

        articleLiveData = database.Articles().get(idArticle)

        articleLiveData.observe(this, Observer {
            Article = it


            name_Article.text = Article.idArticle
            price_Article.text = Article.priceArticle.toString()
            description_Article.text = Article.descriptionArticle
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.article_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit_item -> {
                val intent = Intent(this, newArticle::class.java)
                intent.putExtra("Article", Article)
                startActivity(intent)
            }

            R.id.delete_item -> {
                articleLiveData.removeObservers(this)

                CoroutineScope(Dispatchers.IO).launch {
                    database.Articles().delete(Article)
                    this@ArticleActivity.finish()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
