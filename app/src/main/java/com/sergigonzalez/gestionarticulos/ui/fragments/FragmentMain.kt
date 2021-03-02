package com.sergigonzalez.gestionarticulos.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sergigonzalez.gestionarticulos.adapters.ArticleAdapter
import com.sergigonzalez.gestionarticulos.data.Article
import com.sergigonzalez.gestionarticulos.data.ArticleApp
import com.sergigonzalez.gestionarticulos.databinding.FragmentMainBinding

class FragmentMain : Fragment() {
    private lateinit var database: ArticleApp
    private var listArticles: List<Article> = emptyList()
    private var _binding:FragmentMainBinding? = null
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
    }



}