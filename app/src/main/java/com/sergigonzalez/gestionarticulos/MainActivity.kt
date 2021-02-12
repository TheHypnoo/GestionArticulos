package com.sergigonzalez.gestionarticulos

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sergigonzalez.gestionarticulos.adapters.ArticleAdapter
import com.sergigonzalez.gestionarticulos.adapters.MovementsAdapter
import com.sergigonzalez.gestionarticulos.data.Article
import com.sergigonzalez.gestionarticulos.data.ArticleApp
import com.sergigonzalez.gestionarticulos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: ArticleApp
    private var listArticles: List<Article> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = ArticleApp.getDatabase(this)

        binding.addArticle.setOnClickListener {
            val intent = Intent(this, NewArticle::class.java)
            startActivity(intent)
        }
        database.Articles().getAll().observe(this, {
            listArticles = it
            binding.lista.layoutManager = LinearLayoutManager(this)
            binding.lista.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
            //1
            val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, viewHolder1: RecyclerView.ViewHolder): Boolean {
                    //2
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                    //3
                    val position = viewHolder.adapterPosition
                    ArticleAdapter.ArticleHolder.deleteArticle(listArticles[position],binding.root,database)
                    binding.lista.adapter!!.notifyItemRemoved(position)
                }
            }

            //4
            val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
            itemTouchHelper.attachToRecyclerView(binding.lista)

            val adapter = ArticleAdapter(listArticles)

            binding.lista.adapter = adapter

        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
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
                    binding.lista.layoutManager = LinearLayoutManager(this)
                    binding.lista.addItemDecoration(
                        DividerItemDecoration(
                            this,
                            DividerItemDecoration.VERTICAL
                        )
                    )

                    val adapter = ArticleAdapter(listArticles)

                    binding.lista.adapter = adapter

                })
            }

            R.id.ArticlesWithDescription -> {
                database.Articles().getDescription().observe(this, {
                    listArticles = it
                    binding.lista.layoutManager = LinearLayoutManager(this)
                    binding.lista.addItemDecoration(
                        DividerItemDecoration(
                            this,
                            DividerItemDecoration.VERTICAL
                        )
                    )

                    val adapter = ArticleAdapter(listArticles)

                    binding.lista.adapter = adapter

                })
            }

            R.id.NoHaveStock -> {
                database.Articles().getWithoutStock().observe(this, {
                    listArticles = it
                    binding.lista.layoutManager = LinearLayoutManager(this)
                    binding.lista.addItemDecoration(
                        DividerItemDecoration(
                            this,
                            DividerItemDecoration.VERTICAL
                        )
                    )

                    val adapter = ArticleAdapter(listArticles)

                    binding.lista.adapter = adapter
                })

            }

            R.id.HaveStock -> {
                database.Articles().getWithStock().observe(this, {
                    listArticles = it
                    binding.lista.layoutManager = LinearLayoutManager(this)
                    binding.lista.addItemDecoration(
                        DividerItemDecoration(
                            this,
                            DividerItemDecoration.VERTICAL
                        )
                    )

                    val adapter = ArticleAdapter(listArticles)

                    binding.lista.adapter = adapter

                })
            }

            R.id.Ascendent -> {
                database.Articles().getAllAsc().observe(this, {
                    listArticles = it
                    binding.lista.layoutManager = LinearLayoutManager(this)
                    binding.lista.addItemDecoration(
                        DividerItemDecoration(
                            this,
                            DividerItemDecoration.VERTICAL
                        )
                    )

                    val adapter = ArticleAdapter(listArticles)

                    binding.lista.adapter = adapter

                })
            }

            R.id.Descendent -> {
                database.Articles().getAllDesc().observe(this, {
                    listArticles = it
                    binding.lista.layoutManager = LinearLayoutManager(this)
                    binding.lista.addItemDecoration(
                        DividerItemDecoration(
                            this,
                            DividerItemDecoration.VERTICAL
                        )
                    )

                    val adapter = ArticleAdapter(listArticles)

                    binding.lista.adapter = adapter

                })
            }

            //Intent Movements All
            R.id.Movements -> {
                startActivity(Intent(this, MovementsAll::class.java))
            }
            //Intent Weather
            R.id.Weather -> {
                startActivity(Intent(this, WeatherActivity::class.java))
            }
        }



        return super.onOptionsItemSelected(item)
    }


}