package com.sergigonzalez.gestionarticulos

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.sergigonzalez.gestionarticulos.data.Article
import com.sergigonzalez.gestionarticulos.data.ArticleApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewArticle : AppCompatActivity() {

    private lateinit var id_et: EditText
    private lateinit var precio_et: EditText
    private lateinit var descripcion_et: EditText
    private lateinit var familySpinner: Spinner
    private lateinit var stock_et: EditText
    private lateinit var save_btn: Button
    private var family: String = " "
    private var _id: Int = 0
    private var Edit: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_article)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var idArticle: String? = null

        id_et = findViewById(R.id.edtCode)
        descripcion_et = findViewById(R.id.edtDes)
        familySpinner = findViewById(R.id.family_spinner)
        precio_et = findViewById(R.id.edtPVP)
        stock_et = findViewById(R.id.edtStock)
        save_btn = findViewById(R.id.save_btn)
        stock_et.setText("0")
        stock_et.isEnabled = false

        val database = ArticleApp.getDatabase(this)

        ArrayAdapter.createFromResource(this, R.array.family, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                familySpinner.adapter = adapter
            }

        familySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
                family = when {
                    selectedItem.equals("Hardware") -> {
                        "Hardware"
                    }
                    selectedItem.equals("Software") -> {
                        "Software"
                    }
                    selectedItem.equals("Altres") -> {
                        "Altres"
                    }
                    else -> {
                        " "
                    }
                }
            }
        }

        if (intent.hasExtra("Edit")) {
            Edit = intent.getBooleanExtra("Edit", false)
            if (Edit) {
                id_et.isEnabled = false
                stock_et.isEnabled = false
            }
        }
        if (intent.hasExtra("Article")) {
            val article = intent.extras?.getSerializable("Article") as Article
            _id = article._id
            id_et.setText(article.idArticle)
            descripcion_et.setText(article.descriptionArticle)
            precio_et.setText(article.priceArticle.toString())
            stock_et.setText(article.stockArticle.toString())
            family = article.familyArticle
            when (article.familyArticle) {
                " " -> {
                    familySpinner.setSelection(0)
                }
                "Hardware" -> {
                    familySpinner.setSelection(1)
                }
                "Software" -> {
                    familySpinner.setSelection(2)
                }
                "Altres" -> {
                    familySpinner.setSelection(3)
                }
            }
            idArticle = article.idArticle
        }

        save_btn.setOnClickListener {
            val id: String
            val descripcion: String
            val precio: Double
            val stock: Int
            val view: View? = this.currentFocus

            if (id_et.text.toString().isNotEmpty()) {
                id = id_et.text.toString()
            } else {
                if (view != null) {
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
                snackbarMessage("Debes introducir un codigo")
                return@setOnClickListener
            }

            if (descripcion_et.text.toString().isNotEmpty()) {
                descripcion = descripcion_et.text.toString()
            } else {
                if (view != null) {
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
                snackbarMessage("Debes introducir una descripción")
                return@setOnClickListener
            }

            if (TryCatchDouble(precio_et.text.toString().replace(",".toRegex(), "."))) {
                precio =
                    java.lang.Double.valueOf(precio_et.text.toString().replace(",".toRegex(), "."))
            } else {
                if (view != null) {
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
                snackbarMessage("Debes introducir un precio")
                return@setOnClickListener
            }

            if (precio < 0) {
                if (view != null) {
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
                snackbarMessage("El precio ha de ser un numero superior o igual a 0")
                return@setOnClickListener
            }

            if (TryCatchInt(stock_et.text.toString().replace(",".toRegex(), "."))) {
                stock = Integer.valueOf(stock_et.text.toString().replace(",".toRegex(), "."))
            } else {
                if (view != null) {
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
                snackbarMessage("Debes introducir un Stock")
                return@setOnClickListener
            }

            val article = Article(_id, id, descripcion, family, precio, stock)

            if (idArticle != null) {

                CoroutineScope(Dispatchers.IO).launch {
                    article.idArticle = idArticle

                    database.Articles().update(article)

                    if (view != null) {
                        val imm: InputMethodManager =
                            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view.windowToken, 0)
                    }
                    if (Edit) {
                        snackbarMessage("Has modificado el articulo correctamente")
                    } else {
                        snackbarMessage("Has creado el Articulo correctamente")
                    }
                    Handler(Looper.getMainLooper()).postDelayed({
                        this@NewArticle.finish()
                    }, 500)
                }
            } else {

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        database.Articles().insertAll(article)

                        this@NewArticle.finish()
                    } catch (e: SQLiteConstraintException) {
                        if (view != null) {
                            val imm: InputMethodManager =
                                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(view.windowToken, 0)
                        }
                        snackbarMessage("Ya existe el mismo codigo")
                    }
                }

            }

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun snackbarMessage(_message: String) {
        Snackbar.make(findViewById(android.R.id.content), _message, Snackbar.LENGTH_SHORT).show()
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