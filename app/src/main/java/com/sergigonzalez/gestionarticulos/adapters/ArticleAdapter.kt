package com.sergigonzalez.gestionarticulos.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.sergigonzalez.gestionarticulos.DialogCalendar
import com.sergigonzalez.gestionarticulos.MainActivity
import com.sergigonzalez.gestionarticulos.Movements
import com.sergigonzalez.gestionarticulos.R
import com.sergigonzalez.gestionarticulos.data.Article
import com.sergigonzalez.gestionarticulos.data.ArticleApp
import com.sergigonzalez.gestionarticulos.data.Movement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ArticleAdapter(private val mContext: Context, private val listArticle: List<Article>) : ArrayAdapter<Article>(mContext, 0, listArticle) {

    private val noHaveStock = "#d78290"
    private lateinit var database: ArticleApp
    private val c = Calendar.getInstance()
    private var dates: String? = null
    private var _year = c[Calendar.YEAR]
    private var _month = c[Calendar.MONTH]
    private var _day = c[Calendar.DAY_OF_MONTH]
    private var _main: MainActivity? = null


    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        _main = context as MainActivity?
        val layout = LayoutInflater.from(mContext).inflate(R.layout.item_article, parent, false)
        database = ArticleApp.getDatabase(mContext)
        val article = listArticle[position]

        val linearLayoutArticle = layout.findViewById<LinearLayout>(R.id.Article)
        val movement = layout.findViewById<ImageView>(R.id.Storie)

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

        val priceIVA = article.priceArticle * 0.21 + article.priceArticle
        "${priceIVA}€".also { priceWithIVA.text = it }


        if (stock.text.toString().toInt() <= 0) {

            linearLayoutArticle!!.setBackgroundColor(Color.parseColor(noHaveStock))
        }

        delete.setOnClickListener {
            val builder = AlertDialog.Builder(mContext)
            builder.setMessage("Estas seguro que deseas eliminar el Articulo?")

            builder.setPositiveButton(android.R.string.ok) { _, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    database.Articles().delete(article)
                }
            }

            builder.setNegativeButton(android.R.string.cancel) { dialog, _ ->
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

        movement.setOnClickListener{
            val intent = Intent(context, Movements::class.java)
            intent.putExtra("idArticle", article.idArticle)
            _main?.startActivity(intent)
        }

        return layout
    }

    @SuppressLint("SetTextI18n", "ShowToast")
    private fun stockDate(article: Article, moreOrLess: Boolean){
        _year = c[Calendar.YEAR]
        _month = c[Calendar.MONTH]
        _day = c[Calendar.DAY_OF_MONTH]
        val alert: AlertDialog = AlertDialog.Builder(context).create()

        val dialogView = LayoutInflater.from(context).inflate(R.layout.calendar, null)

        var tv = dialogView!!.findViewById<TextView>(R.id.tvCodeMovement)
        tv.text = tv.text.toString() + " " + article.idArticle

        val description = dialogView.findViewById<TextView>(R.id.tvDescriptionMovement)
        description.text = description.text.toString() + " " + article.descriptionArticle

        val stock = dialogView.findViewById<EditText>(R.id.cantidad)

        tv = dialogView.findViewById<View>(R.id.calendar) as TextView

        dates = if (_month < 10 && _day < 10) {
            "0" + _day + "/" + "0" + (_month + 1) + "/" + _year
        } else if (_month < 10) {
            _day.toString() + "/" + "0" + (_month + 1) + "/" + _year
        } else if (_day < 10) {
            "0" + _day + "/" + (_month + 1) + "/" + _year
        } else {
            _day.toString() + "/" + (_month + 1) + "/" + _year
        }

        tv.text = dates

        dialogView.findViewById<View>(R.id.calendardata).setOnClickListener { v -> DialogCalendar.dialog(v.context, tv) }

        alert.setView(dialogView)
        var type: Char

        alert.setButton(AlertDialog.BUTTON_POSITIVE, "Aceptar", DialogInterface.OnClickListener { _, _ ->
            try {
                dates = DialogCalendar.changeFormatDate(tv.text.toString(), "dd/MM/yyyy", "yyyy/MM/dd")
                if (moreOrLess) {
                    val sumaStock = article.stockArticle.toString().toInt() + stock.text.toString().toInt()
                    article.stockArticle = sumaStock
                    type = 'E'
                } else {
                    val restaStock = article.stockArticle.toString().toInt() - stock.text.toString().toInt()
                    article.stockArticle = restaStock
                    type = 'S'
                }

                val movement = Movement(0,article.idArticle, dates.toString(),stock.text.toString().toInt(),type)

                CoroutineScope(Dispatchers.IO).launch {
                    database.Articles().update(article)
                    database.Articles().insertMovement(movement)
                }
                Toast.makeText(context,"Movimiento y Articulo actualizado",Toast.LENGTH_SHORT)
            } catch (e: Exception) {
                Toast.makeText(context, "El stock no ha sido modificado ya que no has introducido ningún valor.", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
        })

        alert.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar") { _, _ -> }

        alert.show()
    }

}