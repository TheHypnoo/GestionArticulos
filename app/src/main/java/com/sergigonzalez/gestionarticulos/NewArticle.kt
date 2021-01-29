package com.sergigonzalez.gestionarticulos

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
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

    private lateinit var idArticleET: EditText
    private lateinit var priceET: EditText
    private lateinit var descriptionET: EditText
    private lateinit var familySpinner: Spinner
    private lateinit var stockET: EditText
    private lateinit var saveBTN: Button
    private var family: String = " "
    private var edit: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_article)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var idArticle: String? = null

        idArticleET = findViewById(R.id.edtCode)
        descriptionET = findViewById(R.id.edtDes)
        familySpinner = findViewById(R.id.family_spinner)
        priceET = findViewById(R.id.edtPVP)
        stockET = findViewById(R.id.edtStock)
        saveBTN = findViewById(R.id.save_btn)
        stockET.setText("0")
        stockET.isEnabled = false

        val database = ArticleApp.getDatabase(this)

        ArrayAdapter.createFromResource(this, R.array.family, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            familySpinner.adapter = adapter
        }

        familySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //Null
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                family = when(selectedItem){
                    "Hardware" -> "Hardware"
                    "Software" -> "Software"
                    "Altres" -> "Altres"
                    else -> " "
                }
            }
        }

        if(intent.hasExtra("Edit")) {
            edit = intent.getBooleanExtra("Edit", false)
            if(edit) {
                idArticleET.isEnabled = false
                stockET.isEnabled = false
            }
        }
        if (intent.hasExtra("Article")) {
            val article = intent.extras?.getSerializable("Article") as Article

            idArticleET.setText(article.idArticle)
            descriptionET.setText(article.descriptionArticle)
            priceET.setText(article.priceArticle.toString())
            stockET.setText(article.stockArticle.toString())
            family = article.familyArticle
            when(article.familyArticle) {
                " " -> familySpinner.setSelection(0)
                "Hardware" -> familySpinner.setSelection(1)
                "Software" -> familySpinner.setSelection(2)
                "Altres" -> familySpinner.setSelection(3)
            }
            idArticle = article.idArticle
        }

        saveBTN.setOnClickListener {
            val id: String
            val description: String
            val price: Double
            val stock: Int
            val view: View? = this.currentFocus

            if(idArticleET.text.toString().isNotEmpty()) {
                id = idArticleET.text.toString()
            } else {
                if (view != null) {
                    val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
                snackbarMessage("Debes introducir un codigo")
                return@setOnClickListener
            }

            if(descriptionET.text.toString().isNotEmpty()) {
                description = descriptionET.text.toString()
            } else {
                if (view != null) {
                    val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
                snackbarMessage("Debes introducir una descripción")
                return@setOnClickListener
            }

            if(tryCatchDouble(priceET.text.toString().replace(",".toRegex(), "."))) {
                price = java.lang.Double.valueOf(priceET.text.toString().replace(",".toRegex(), "."))
            } else {
                if (view != null) {
                    val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
                snackbarMessage("Debes introducir un precio")
                return@setOnClickListener
            }

            if(price < 0) {
                if (view != null) {
                    val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
                snackbarMessage("El precio ha de ser un numero superior o igual a 0")
                return@setOnClickListener
            }

            if (tryCatchInt(stockET.text.toString().replace(",".toRegex(), "."))) {
                stock = Integer.valueOf(stockET.text.toString().replace(",".toRegex(), "."))
            } else {
                if (view != null) {
                    val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
                snackbarMessage("Debes introducir un Stock")
                return@setOnClickListener
            }

            if(price < 0) {
                if (view != null) {
                    val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
                snackbarMessage("El precio ha de ser un numero superior o igual a 0")
                return@setOnClickListener
            }

            val article = Article(0,id, description, family, price, stock)

            if (idArticle != null) {

                CoroutineScope(Dispatchers.IO).launch {
                    article.idArticle = idArticle
                    database.Articles().update(article)

                    if (view != null) {
                        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view.windowToken, 0)
                    }
                    snackbarMessage("Has creado el Articulo correctamente")

                    this@NewArticle.finish()
                }
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                    database.Articles().insertAll(article)

                    this@NewArticle.finish()
                    } catch (e: SQLiteConstraintException) {
                        if (view != null) {
                            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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

    private fun tryCatchDouble(_string: String): Boolean {
        var a = true
        try {
            java.lang.Double.valueOf(_string)
        } catch (e: Exception) {
            a = false
        }
        return a
    }

    private fun tryCatchInt(_string: String): Boolean {
        var a = true
        try {
            Integer.valueOf(_string)
        } catch (e: java.lang.Exception) {
            a = false
        }
        return a
    }

}