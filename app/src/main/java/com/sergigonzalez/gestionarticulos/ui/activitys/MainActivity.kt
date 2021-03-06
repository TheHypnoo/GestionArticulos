package com.sergigonzalez.gestionarticulos.ui.activitys

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sergigonzalez.gestionarticulos.R
import com.sergigonzalez.gestionarticulos.data.ArticleApp
import com.sergigonzalez.gestionarticulos.databinding.ActivityMainBinding
import com.sergigonzalez.gestionarticulos.ui.fragments.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: ArticleApp

    private val fragmentMain : FragmentMain = FragmentMain()
    private val fragmentMovementsAll : FragmentMovementsAll = FragmentMovementsAll()
    private val fragmentWeather : FragmentWeather = FragmentWeather()
    private val fragmentInfo : FragmentInfo = FragmentInfo()

    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var fab: FloatingActionButton
    lateinit var appbar: BottomAppBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = ArticleApp.getDatabase(this)
        bottomNavigationView = binding.bottomNavigationView
        fab = binding.fab
        appbar = binding.bottomAppBar
        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.selectedItemId = R.id.miHome
        replaceFragment(fragmentMain)
        binding.bottomNavigationView.menu.getItem(2).isEnabled = false
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.miHome -> {
                    replaceFragment(fragmentMain)
                    binding.fab.setImageResource(R.drawable.ic_add_final)
                    binding.fab.setOnClickListener {
                        replaceFragment(fragment = FragmentAddArticle())
                        binding.bottomAppBar.performShow()
                        binding.bottomNavigationView.menu.getItem(2).isChecked = true
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.miMovements -> {
                    replaceFragment(fragmentMovementsAll)
                    binding.fab.setImageResource(R.drawable.ic_search_black_24dp)
                    binding.fab.setOnClickListener {
                        binding.bottomNavigationView.menu.getItem(1).isChecked = true
                        binding.bottomAppBar.performShow()
                        fragmentMovementsAll.SearchMovement()
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.miWeather -> {
                    replaceFragment(fragmentWeather)
                    binding.fab.setImageResource(R.drawable.ic_search_black_24dp)
                    binding.fab.setOnClickListener {
                        binding.bottomNavigationView.menu.getItem(3).isChecked = true
                        binding.bottomAppBar.performShow()
                        fragmentWeather.Search()
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.miInfo -> {
                    replaceFragment(fragmentInfo)
                    binding.fab.setImageResource(R.drawable.ic_add_final)
                    binding.fab.setOnClickListener {
                        replaceFragment(fragment = FragmentAddArticle())
                        binding.bottomAppBar.performShow()
                        binding.bottomNavigationView.menu.getItem(2).isChecked = true
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }
        binding.fab.setOnClickListener{
            replaceFragment(fragment = FragmentAddArticle())
            binding.bottomNavigationView.menu.getItem(2).isChecked = true
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }




        /*
//        fragmentTransaction = supportFragmentManager.beginTransaction()
//        fragmentTransaction.add(R.id.FragmentMenu, fragment)
//        fragmentTransaction.commit()


        binding.addArticle.setOnClickListener {
            val intent = Intent(this, NewArticle::class.java)
            startActivity(intent)
        }
        database.Articles().getAll().observe(this, {
            listArticles = it
            binding.lista.layoutManager = LinearLayoutManager(this)
            binding.lista.addItemDecoration(
                DividerItemDecoration(
                    this,
                    DividerItemDecoration.VERTICAL
                )
            )
            //1
            val itemTouchCallback = object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    viewHolder1: RecyclerView.ViewHolder
                ): Boolean {
                    //2
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                    //3
                    val position = viewHolder.adapterPosition
                    ArticleAdapter.ArticleHolder.deleteArticle(
                        listArticles[position],
                        binding.root,
                        database
                    )
                    binding.lista.adapter!!.notifyItemRemoved(position)
                }
            }

            //4
            val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
            itemTouchHelper.attachToRecyclerView(binding.lista)

            val adapter = ArticleAdapter(listArticles)

            binding.lista.adapter = adapter
            if (listArticles.isEmpty()) {
                binding.emptyView.visibility = View.VISIBLE
                binding.lista.visibility = View.GONE
            } else {
                binding.emptyView.visibility = View.GONE
                binding.lista.visibility = View.VISIBLE
            }
        })
*/
    //
/*
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
                    if (listArticles.isEmpty()) {
                        binding.emptyView.visibility = View.VISIBLE
                        binding.lista.visibility = View.GONE
                    } else {
                        binding.emptyView.visibility = View.GONE
                        binding.lista.visibility = View.VISIBLE
                    }

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
                    if (listArticles.isEmpty()) {
                        binding.emptyView.visibility = View.VISIBLE
                        binding.lista.visibility = View.GONE
                    } else {
                        binding.emptyView.visibility = View.GONE
                        binding.lista.visibility = View.VISIBLE
                    }

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
                    if (listArticles.isEmpty()) {
                        binding.emptyView.visibility = View.VISIBLE
                        binding.lista.visibility = View.GONE
                    } else {
                        binding.emptyView.visibility = View.GONE
                        binding.lista.visibility = View.VISIBLE
                    }

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
                    if (listArticles.isEmpty()) {
                        binding.emptyView.visibility = View.VISIBLE
                        binding.lista.visibility = View.GONE
                    } else {
                        binding.emptyView.visibility = View.GONE
                        binding.lista.visibility = View.VISIBLE
                    }

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
                    if (listArticles.isEmpty()) {
                        binding.emptyView.visibility = View.VISIBLE
                        binding.lista.visibility = View.GONE
                    } else {
                        binding.emptyView.visibility = View.GONE
                        binding.lista.visibility = View.VISIBLE
                    }

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
                    if (listArticles.isEmpty()) {
                        binding.emptyView.visibility = View.VISIBLE
                        binding.lista.visibility = View.GONE
                    } else {
                        binding.emptyView.visibility = View.GONE
                        binding.lista.visibility = View.VISIBLE
                    }

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
*/

}