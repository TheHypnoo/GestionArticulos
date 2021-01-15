package com.sergigonzalez.gestionarticulos

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.sergigonzalez.gestionarticulos.data.Article
import com.sergigonzalez.gestionarticulos.data.ArticleApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class ArticleAdapter(private val mContext: Context, private val listArticle: List<Article>) : ArrayAdapter<Article>(mContext, 0, listArticle) {

    private val NoHaveStock = "#d78290"
    private lateinit var database: ArticleApp

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(mContext).inflate(R.layout.item_article, parent, false)
        database = ArticleApp.getDatabase(mContext)
        val article = listArticle[position]

        val linearLayoutArticle = layout.findViewById<LinearLayout>(R.id.Article)

        val id = layout.findViewById<TextView>(R.id.idArticle)
        val description = layout.findViewById<TextView>(R.id.descriptionArticle)
        val stock = layout.findViewById<TextView>(R.id.stockArticle)
        val price = layout.findViewById<TextView>(R.id.priceArticle)

        val delete = layout.findViewById<ImageView>(R.id.deleteMain)
        val moreStock = layout.findViewById<ImageView>(R.id.Mas)
        val lessStock = layout.findViewById<ImageView>(R.id.Menos)

        id.text = article.idArticle
        description.text = article.descriptionArticle
        stock.text = article.stockArticle.toString()
        price.text = "${article.priceArticle}€"

        if (stock.text.toString().toInt() <= 0) {

            linearLayoutArticle!!.setBackgroundColor(Color.parseColor(NoHaveStock))
        }

        delete.setOnClickListener {
            val builder = AlertDialog.Builder(mContext)
            builder.setMessage("Estas seguro que deseas eliminar el registro?")

            builder.setPositiveButton(android.R.string.ok) { dialog, which ->
                CoroutineScope(Dispatchers.IO).launch {
                    database.Articles().delete(article)
                }
            }

            builder.setNegativeButton(android.R.string.cancel) { dialog, which ->
                dialog.dismiss()
            }

            builder.show()

        }

        moreStock.setOnClickListener {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.modify_stock_alert, null)
            val stock = article.stockArticle.toString()
            val stockEscrito = dialogView.findViewById<EditText>(R.id.alertModifyStock)

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Afegir stock")
            builder.setView(dialogView)
            builder.setPositiveButton("Ok") { dialog, which ->

                if (stockEscrito.text.toString() == "") {
                    stockEscrito.setText("0")
                }
                val sumaStock = stock.toInt() + stockEscrito.text.toString().toInt()
                article.stockArticle = sumaStock
                CoroutineScope(Dispatchers.IO).launch {
                    database.Articles().update(article)
                }
            }
            builder.show()
        }

        lessStock.setOnClickListener {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.modify_stock_alert, null)
            val stock = article.stockArticle.toString()
            val stockEscrito = dialogView.findViewById<EditText>(R.id.alertModifyStock)

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Restar stock")
            builder.setView(dialogView)
            builder.setPositiveButton("Ok") { dialog, which ->

                if (stockEscrito.text.toString() == "") {
                    stockEscrito.setText("0")
                }
                val restaStock = stock.toInt() - stockEscrito.text.toString().toInt()
                article.stockArticle = restaStock
                CoroutineScope(Dispatchers.IO).launch {
                    database.Articles().update(article)
                }
            }
            builder.show()
        }

        return layout
    }
}