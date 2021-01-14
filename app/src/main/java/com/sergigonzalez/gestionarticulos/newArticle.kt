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

class newArticle : AppCompatActivity() {

    private lateinit var nombre_et: EditText
    private lateinit var precio_et: EditText
    private lateinit var descripcion_et: EditText
    private lateinit var familySpinner: Spinner
    private lateinit var stock_et: EditText
    private lateinit var save_btn: Button
    private var family: String = " "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_article)

        var idArticle: String? = null

        nombre_et = findViewById(R.id.edtCode)
        descripcion_et = findViewById(R.id.edtDes)
        familySpinner = findViewById(R.id.family_spinner)
        precio_et = findViewById(R.id.edtPVP)
        stock_et = findViewById(R.id.edtStock)
        save_btn = findViewById(R.id.save_btn)

        val database = ArticleApp.getDatabase(this)

        if (intent.hasExtra("Article")) {
            val article = intent.extras?.getSerializable("Article") as Article

            nombre_et.setText(article.idArticle)
            descripcion_et.setText(article.descriptionArticle)
            precio_et.setText(article.priceArticle.toString())
            stock_et.setText(article.stockArticle.toString())
            idArticle = article.idArticle
        }



        ArrayAdapter.createFromResource(this, R.array.family, android.R.layout.simple_spinner_item).also {
            adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            familySpinner.adapter = adapter
        }

        familySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //Null
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                if (selectedItem.equals("Hardware")) {
                    family = "Hardware"
                }else if(selectedItem.equals("Software")) {
                    family = "Software"
                } else if(selectedItem.equals("Altres")){
                    family = "Altres"
                }
            }
        }

        save_btn.setOnClickListener {
            val id : String
            val descripcion: String
            val precio: Double
            val stock: Int
            val view: View? = this.currentFocus

            if(!nombre_et.text.toString().isEmpty()) {
                id = nombre_et.text.toString()
            } else {
                if (view != null) {
                    val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
                snackbarMessage("Debes introducir un codigo")
                return@setOnClickListener
            }

            if(!descripcion_et.text.toString().isEmpty()) {
                descripcion = descripcion_et.text.toString()
            } else {
                if (view != null) {
                    val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
                snackbarMessage("Debes introducir una descripción")
                return@setOnClickListener
            }

            if(family.equals(" ")) {
                if (view != null) {
                    val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
                snackbarMessage("Debes introducir una categoria")
                return@setOnClickListener
            }

            if(TryCatchDouble(precio_et.text.toString().replace(",".toRegex(), "."))) {
                precio = java.lang.Double.valueOf(precio_et.text.toString().replace(",".toRegex(), "."))
            } else {
                if (view != null) {
                    val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
                snackbarMessage("Debes introducir un precio")
                return@setOnClickListener
            }

            if(precio < 0) {
                if (view != null) {
                    val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
                snackbarMessage("El precio ha de ser un numero superior o igual a 0")
                return@setOnClickListener
            }

            if (TryCatchInt(stock_et.text.toString().replace(",".toRegex(), "."))) {
                stock = Integer.valueOf(stock_et.text.toString().replace(",".toRegex(), "."))
            } else {
                if (view != null) {
                    val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
                snackbarMessage("Debes introducir un Stock")
                return@setOnClickListener
            }

            if(precio < 0) {
                if (view != null) {
                    val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
                snackbarMessage("El precio ha de ser un numero superior o igual a 0")
                return@setOnClickListener
            }

            val article = Article(id, descripcion, family, precio, stock)

                if (idArticle != null) {

                    CoroutineScope(Dispatchers.IO).launch {
                        article.idArticle = idArticle

                        database.Articles().update(article)

                        if (view != null) {
                            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(view.windowToken, 0)
                        }
                        snackbarMessage("Has creado el Articulo correctamente")

                        this@newArticle.finish()
                    }
                } else {

                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                        database.Articles().insertAll(article)

                        this@newArticle.finish()
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

    private fun snackbarMessage(_message: String) {
        var message = _message
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }

    private fun TryCatchDouble(_string: String?): Boolean {
        var a = true
        try {
            java.lang.Double.valueOf(_string)
        } catch (e: Exception) {
            a = false
        }
        return a
    }

    private fun TryCatchInt(_string: String?): Boolean {
        var a = true
        try {
            Integer.valueOf(_string)
        } catch (e: java.lang.Exception) {
            a = false
        }
        return a
    }
}