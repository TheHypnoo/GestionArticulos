package com.sergigonzalez.gestionarticulos

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.material.snackbar.Snackbar
import com.sergigonzalez.gestionarticulos.data.Article
import com.sergigonzalez.gestionarticulos.data.ArticleApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ArticleAdapter(private val mContext: Context, private val listArticle: List<Article>) : ArrayAdapter<Article>(mContext, 0, listArticle) {

    private val NoHaveStock = "#d78290"
    private lateinit var database: ArticleApp
    private val c = Calendar.getInstance()
    private var dates: String? = null
    private var _year = c[Calendar.YEAR]
    private var _month = c[Calendar.MONTH]
    private var _day = c[Calendar.DAY_OF_MONTH]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(mContext).inflate(R.layout.item_article, parent, false)
        database = ArticleApp.getDatabase(mContext)
        val article = listArticle[position]

        val linearLayoutArticle = layout.findViewById<LinearLayout>(R.id.Article)

        val id = layout.findViewById<TextView>(R.id.idArticle)
        val description = layout.findViewById<TextView>(R.id.descriptionArticle)
        val stock = layout.findViewById<TextView>(R.id.stockArticle)
        val price = layout.findViewById<TextView>(R.id.priceArticle)
        val priceWithIVA = layout.findViewById<TextView>(R.id.priceWithIVA)

        val delete = layout.findViewById<ImageView>(R.id.deleteMain)
        val moreStock = layout.findViewById<ImageView>(R.id.Mas)
        val lessStock = layout.findViewById<ImageView>(R.id.Menos)

        id.text = article.idArticle
        description.text = article.descriptionArticle
        stock.text = article.stockArticle.toString()
        "${article.priceArticle}€".also { price.text = it }

        val IVA = article.priceArticle * 0.21 + article.priceArticle
        "${IVA}€".also { priceWithIVA.text = it }


        if (stock.text.toString().toInt() <= 0) {

            linearLayoutArticle!!.setBackgroundColor(Color.parseColor(NoHaveStock))
        }

        delete.setOnClickListener {
            val builder = AlertDialog.Builder(mContext)
            builder.setMessage("Estas seguro que deseas eliminar el Articulo?")

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
            /*
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

             */
            stockDate(article, true)
        }

        lessStock.setOnClickListener {
            /*
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

             */
            stockDate(article, false)
        }

        return layout
    }

    private fun stockDate(article: Article, moreOrLess: Boolean){
        _year = c[Calendar.YEAR]
        _month = c[Calendar.MONTH]
        _day = c[Calendar.DAY_OF_MONTH]
        val _alert: AlertDialog = AlertDialog.Builder(context).create()

        val dialogView = LayoutInflater.from(context).inflate(R.layout.calendar, null)

        var tv = dialogView!!.findViewById<TextView>(R.id.tvCodeMovement)
        tv.text = tv.text.toString() + " " + article.idArticle

        val stock = dialogView.findViewById<EditText>(R.id.cantidad)

        tv = dialogView.findViewById<View>(R.id.calendar) as TextView

        if (_month < 10 && _day < 10) {
            dates = "0" + _day + "/" + "0" + (_month + 1) + "/" + _year
        } else if (_month < 10) {
            dates = _day.toString() + "/" + "0" + (_month + 1) + "/" + _year
        } else if (_day < 10) {
            dates = "0" + _day + "/" + (_month + 1) + "/" + _year
        } else {
            dates = _day.toString() + "/" + (_month + 1) + "/" + _year
        }

        tv.text = dates

        dialogView.findViewById<View>(R.id.calendardata).setOnClickListener { v -> DialogCalendar.Dialog(v.context, tv) }

        _alert.setView(dialogView)

        _alert.setButton(AlertDialog.BUTTON_POSITIVE, "Aceptar", DialogInterface.OnClickListener { dialog, whichButton ->
            try {
                dates = DialogCalendar.ChangeFormatDate(tv.text.toString(), "dd/MM/yyyy", "yyyy/MM/dd")
                //var Num_Stock = java.lang.Long.valueOf(stock.text.toString())
                if(moreOrLess) {
                    val sumaStock = article.stockArticle.toString().toInt() + stock.text.toString().toInt()
                    article.stockArticle = sumaStock
                } else {
                    val restaStock = article.stockArticle.toString().toInt() - stock.text.toString().toInt()
                    article.stockArticle = restaStock
                }
                CoroutineScope(Dispatchers.IO).launch {
                    database.Articles().update(article)
                }
            } catch (e: Exception) {
                Toast.makeText(context, "El stock no ha sido modificado ya que no has introducido ningún valor.", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
        })

        _alert.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar") { dialog, whichButton -> }

        _alert.show()
    }

}