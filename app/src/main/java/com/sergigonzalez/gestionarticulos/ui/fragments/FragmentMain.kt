package com.sergigonzalez.gestionarticulos.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sergigonzalez.gestionarticulos.R
import com.sergigonzalez.gestionarticulos.adapters.ArticleAdapter
import com.sergigonzalez.gestionarticulos.data.Article
import com.sergigonzalez.gestionarticulos.data.ArticleApp
import com.sergigonzalez.gestionarticulos.databinding.FragmentMainBinding


class FragmentMain : Fragment() {
    private lateinit var database: ArticleApp
    private var listArticles: List<Article> = emptyList()
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = ArticleApp.getDatabase(this@FragmentMain.requireContext())
        database.Articles().getAll().observe(viewLifecycleOwner, {
            listArticles = it
            binding.lista.layoutManager = LinearLayoutManager(this@FragmentMain.context)
            binding.lista.addItemDecoration(
                DividerItemDecoration(
                    this@FragmentMain.context,
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
                    val builder = AlertDialog.Builder(view.context)
                    val position = viewHolder.adapterPosition
                    builder.setMessage("Estas seguro que deseas eliminar el Articulo?\nCon codigo: ${listArticles[position].idArticle}\nDescripción:  ${listArticles[position].descriptionArticle}")


                    builder.setPositiveButton(android.R.string.ok) { _, _ ->
                        ArticleAdapter.ArticleHolder.deleteArticle(
                            listArticles[position],
                            database
                        )
                        binding.lista.adapter!!.notifyItemRemoved(position)
                    }

                    builder.setNegativeButton(android.R.string.cancel) { dialog, _ ->
                        dialog.dismiss()
                        binding.lista.adapter!!.notifyItemChanged(position)
                    }

                    builder.show()

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
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onAttach(context: Context) {
        setHasOptionsMenu(true);
        super.onAttach(context)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            //All filters
            R.id.AllArticles -> {
                database.Articles().getAll().observe(this, {
                    listArticles = it
                    binding.lista.layoutManager =
                        LinearLayoutManager(this@FragmentMain.requireContext())
                    binding.lista.addItemDecoration(
                        DividerItemDecoration(
                            this@FragmentMain.requireContext(),
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
                    binding.lista.layoutManager =
                        LinearLayoutManager(this@FragmentMain.requireContext())
                    binding.lista.addItemDecoration(
                        DividerItemDecoration(
                            this@FragmentMain.requireContext(),
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
                    binding.lista.layoutManager =
                        LinearLayoutManager(this@FragmentMain.requireContext())
                    binding.lista.addItemDecoration(
                        DividerItemDecoration(
                            this@FragmentMain.requireContext(),
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
                    binding.lista.layoutManager =
                        LinearLayoutManager(this@FragmentMain.requireContext())
                    binding.lista.addItemDecoration(
                        DividerItemDecoration(
                            this@FragmentMain.requireContext(),
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
                    binding.lista.layoutManager =
                        LinearLayoutManager(this@FragmentMain.requireContext())
                    binding.lista.addItemDecoration(
                        DividerItemDecoration(
                            this@FragmentMain.requireContext(),
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
                    binding.lista.layoutManager =
                        LinearLayoutManager(this@FragmentMain.requireContext())
                    binding.lista.addItemDecoration(
                        DividerItemDecoration(
                            this@FragmentMain.requireContext(),
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
        }

        return super.onOptionsItemSelected(item)
    }


}