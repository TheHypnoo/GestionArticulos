package com.sergigonzalez.gestionarticulos.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
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
    private var filterDescription: Boolean = false
    private var filterWithOutStock: Boolean = false
    private lateinit var description: String
    private var singlePosition = 0

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
        activity?.title = "Home"
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

            R.id.menu_btn_filter -> {
                alertFilter()
            }

            R.id.menu_btn_order -> {
                orderAlert()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun alertFilter() {
        val alert = androidx.appcompat.app.AlertDialog.Builder(this@FragmentMain.requireContext())
        alert.setTitle("Select filters")
        val filtres = arrayOf(
            "Article With description",
            "Article Without stock"
        )
        val filtresSeleccionats = booleanArrayOf(filterDescription, filterWithOutStock)
        alert.setMultiChoiceItems(
            filtres, filtresSeleccionats
        ) { dialog, which, isChecked -> filtresSeleccionats[which] = isChecked }
        alert.setPositiveButton(
            android.R.string.ok
        ) { dialog, which ->
            filterDescription = filtresSeleccionats[0]
            filterWithOutStock = filtresSeleccionats[1]
            if (filterDescription) {
                alertDescription()
            } else {
                selectOrder()
            }
        }
        alert.setNegativeButton(android.R.string.cancel, null)
        alert.setNeutralButton(
            "Restart"
        ) { dialog, which -> deleteFilters() }
        alert.show()
    }

    private fun deleteFilters() {
        filterDescription = false;
        description = "";
        filterWithOutStock = false;
        singlePosition = 0
        selectOrder()
    }

    private fun alertDescription() {
        if (filterDescription) {
            val alert = AlertDialog.Builder(this@FragmentMain.requireContext()).create()
            alert.setTitle("Write the word you want to filter from the article description")
            val edtDescription = EditText(this@FragmentMain.requireContext())
            alert.setView(edtDescription)
            alert.setButton(
                AlertDialog.BUTTON_POSITIVE,"Aceptar"
            ) { dialog, which ->
                try {
                    description = edtDescription.text.toString().toLowerCase()
                } catch (e: Exception) {
                    description = ""
                    snackbarMessage("Articles could not be filtered",false)
                }
                if (description.isEmpty()) {
                    filterDescription = false
                }
                selectOrder()
            }
            alert.setButton(
                androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE,
                getString(android.R.string.cancel)
            ) { dialog, which ->
                // No fa res
            }
            alert.show()
        }
    }

    private fun orderAlert() {
        val alert = AlertDialog.Builder(this@FragmentMain.requireContext())
        alert.setTitle("Order")
        val sorts = arrayOf(
            "Insert date (old ones first)",
            "Insert date (newest first)",
            "Item code (A-Z)",
            "Item code (Z-A)",
            "Price from lowest to highest",
            "Price from high to low"
        )
        alert.setSingleChoiceItems(
            sorts, singlePosition
        ) { dialog, which -> singlePosition = which }
        alert.setPositiveButton(
            android.R.string.ok
        ) { dialog, which -> selectOrder() }
        alert.setNegativeButton(android.R.string.cancel, null)
        alert.setNeutralButton(
            "Restart"
        ) { dialog, which -> deleteFilters() }
        alert.show()
    }

    private fun selectOrder() {
        if(filterDescription && filterWithOutStock) {
            database.Articles().getDescriptionWithWordAndStock(description).observe(this, {
                listArticles = it

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
        } else if(filterDescription) {
            database.Articles().getDescriptionWithWord(description).observe(this, {
                listArticles = it

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
        } else if(filterWithOutStock) {
            database.Articles().getWithoutStock().observe(this, {
                listArticles = it

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
        } else {
            database.Articles().getAll().observe(this, {
                listArticles = it

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
        when (singlePosition) {
            1 -> {
                database.Articles().getDescriptionOld().observe(this, {
                    listArticles = it

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
            2 -> {
                database.Articles().getDescriptionNew().observe(this, {
                    listArticles = it

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
            3 -> {
                database.Articles().getAllDesc().observe(this, {
                    listArticles = it

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
            4 -> {
                database.Articles().getAllAsc().observe(this, {
                    listArticles = it

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
            5 -> {
                database.Articles().getStockLow().observe(this, {
                    listArticles = it

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
            6 -> {
                database.Articles().getStockHigh().observe(this, {
                    listArticles = it

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
    }

    private fun snackbarMessage(_message: String, CorrectorIncorrect: Boolean) {
        if (CorrectorIncorrect) Snackbar.make(binding.root, _message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(Color.parseColor("#ff669900")).show()
        else
            Snackbar.make(binding.root, _message, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(Color.parseColor("#B00020")).show()
    }
}