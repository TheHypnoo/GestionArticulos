package com.sergigonzalez.gestionarticulos.ui.fragments

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.material.snackbar.Snackbar
import com.sergigonzalez.gestionarticulos.R
import com.sergigonzalez.gestionarticulos.data.Article
import com.sergigonzalez.gestionarticulos.data.ArticleApp
import com.sergigonzalez.gestionarticulos.databinding.FragmentAddArticleBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentAddArticle : Fragment() {
    private var _binding: FragmentAddArticleBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: ArticleApp
    private var family: String = " "
    private var _id: Int = 0
    private var Edit: Boolean = false
    private var article : Article? = null
    private var idArticle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddArticleBinding.inflate(inflater, container, false)
        database = ArticleApp.getDatabase(this@FragmentAddArticle.requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ArrayAdapter.createFromResource(this@FragmentAddArticle.requireContext(), R.array.family, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.familySpinner.adapter = adapter
            }

        binding.familySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //Null
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                family = when (selectedItem) {
                    "Hardware" -> {
                        "Hardware"
                    }
                    "Software" -> {
                        "Software"
                    }
                    "Altres" -> {
                        "Altres"
                    }
                    else -> {
                        " "
                    }
                }
            }
        }
        article = arguments?.getSerializable("Article") as Article?
        Edit = arguments?.getBoolean("Edit") == false
        if (Edit) {
            binding.edtCode.isEnabled = false
            binding.edtStock.isEnabled = false
        }
/*
        if(article?.idArticle!=" ") {
            _id = article!!._id
            binding.edtCode.setText(article!!.idArticle)
            binding.edtDes.setText(article!!.descriptionArticle)
            binding.edtPVP.setText(article!!.priceArticle.toString())
            binding.edtStock.setText(article!!.stockArticle.toString())
            family = article!!.familyArticle
            when (article!!.familyArticle) {
                " " -> {
                    binding.familySpinner.setSelection(0)
                }
                "Hardware" -> {
                    binding.familySpinner.setSelection(1)
                }
                "Software" -> {
                    binding.familySpinner.setSelection(2)
                }
                "Altres" -> {
                    binding.familySpinner.setSelection(3)
                }
            }
            idArticle = article!!.idArticle
        }
*/
        binding.saveBtn.setOnClickListener{
            val id: String
            val descripcion: String
            val precio: Double
            val stock: Int
            //Probar el View...
            val view: View? = this@FragmentAddArticle.view?.findFocus()

            if (binding.edtCode.text.toString().isNotEmpty()) {
                id = binding.edtCode.text.toString()
            } else {
                if (view != null) {
                    val imm = requireActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE
                    ) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
                //snackbarMessage("Debes introducir un codigo")
                return@setOnClickListener
            }

            if (binding.edtDes.text.toString().isNotEmpty()) {
                descripcion = binding.edtDes.text.toString()
            } else {
                if (view != null) {
                    val imm = requireActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE
                    ) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
                //snackbarMessage("Debes introducir una descripción")
                return@setOnClickListener
            }

            if (TryCatchDouble(binding.edtPVP.text.toString().replace(",".toRegex(), "."))) {
                precio =
                    java.lang.Double.valueOf(binding.edtPVP.text.toString().replace(",".toRegex(), "."))
            } else {
                if (view != null) {
                    val imm = requireActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE
                    ) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
                snackbarMessage("Debes introducir un precio")
                return@setOnClickListener
            }

            if (precio < 0) {
                if (view != null) {
                    val imm = requireActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE
                    ) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
                snackbarMessage("El precio ha de ser un numero superior o igual a 0")
                return@setOnClickListener
            }

            if (TryCatchInt(binding.edtStock.text.toString().replace(",".toRegex(), "."))) {
                stock = Integer.valueOf(binding.edtStock.text.toString().replace(",".toRegex(), "."))
            } else {
                if (view != null) {
                    val imm = requireActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE
                    ) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
                snackbarMessage("Debes introducir un Stock")
                return@setOnClickListener
            }

            val article = Article(_id, id, descripcion, family, precio, stock)

            if (idArticle != null) {

                CoroutineScope(Dispatchers.IO).launch {
                    article.idArticle = idArticle as String

                    database.Articles().update(article)

                    if (view != null) {
                        val imm = requireActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE
                        ) as InputMethodManager
                        imm.hideSoftInputFromWindow(view.windowToken, 0)
                    }
                    if (Edit) {
                        snackbarMessage("Has modificado el articulo correctamente")
                    } else {
                        snackbarMessage("Has creado el Articulo correctamente")
                    }
                    Handler(Looper.getMainLooper()).postDelayed({
                        //this@FragmentAddArticle.activity?.finish()
                    }, 500)
                }
            } else {

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        database.Articles().insertAll(article)

                        //this@FragmentAddArticle.activity?.finish()
                    } catch (e: SQLiteConstraintException) {
                        if (view != null) {
                            val imm = requireActivity().getSystemService(
                                Context.INPUT_METHOD_SERVICE
                            ) as InputMethodManager
                            imm.hideSoftInputFromWindow(view.windowToken, 0)
                        }
                        snackbarMessage("Ya existe el mismo codigo")
                    }
                }

            }

        }
        }

    private fun snackbarMessage(_message: String) {
        //Modo prueba
        Snackbar.make(binding.root, _message, Snackbar.LENGTH_SHORT).show()
    }

    private fun TryCatchDouble(_string: String): Boolean {
        var a = true
        try {
            java.lang.Double.valueOf(_string)
        } catch (e: Exception) {
            a = false
        }
        return a
    }

    private fun TryCatchInt(_string: String): Boolean {
        var a = true
        try {
            Integer.valueOf(_string)
        } catch (e: java.lang.Exception) {
            a = false
        }
        return a
    }


}