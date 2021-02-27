package com.sergigonzalez.gestionarticulos.adapters

import android.R.attr.fragment
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.sergigonzalez.gestionarticulos.R
import com.sergigonzalez.gestionarticulos.`object`.DialogCalendar
import com.sergigonzalez.gestionarticulos.data.Article
import com.sergigonzalez.gestionarticulos.data.ArticleApp
import com.sergigonzalez.gestionarticulos.data.Movement
import com.sergigonzalez.gestionarticulos.ui.activitys.MainActivity
import com.sergigonzalez.gestionarticulos.ui.activitys.Movements
import com.sergigonzalez.gestionarticulos.ui.fragments.FragmentAddArticle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class ArticleAdapter(private val listArticle: List<Article>) : RecyclerView.Adapter<ArticleAdapter.ArticleHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        val viewInflater = LayoutInflater.from(parent.context)
        return ArticleHolder(viewInflater.inflate(R.layout.item_article, parent, false))
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        holder.render(listArticle[position])

    }

    override fun getItemCount(): Int = listArticle.size

    class ArticleHolder(private val view: View): RecyclerView.ViewHolder(view) {
        companion object {
            fun deleteArticle(article: Article, view: View, database: ArticleApp) {
                val builder = AlertDialog.Builder(view.context)
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
        }
        private val noHaveStock = "#d78290"
        private lateinit var database: ArticleApp
        private val c = Calendar.getInstance()
        private var dates: String? = null
        private var _year = c[Calendar.YEAR]
        private var _month = c[Calendar.MONTH]
        private var _day = c[Calendar.DAY_OF_MONTH]
        private var _main: MainActivity? = null
        private var fragmentAddArticle : FragmentAddArticle = FragmentAddArticle()

        fun render(article: Article) {
            _main = view.context as MainActivity?
            database = ArticleApp.getDatabase(view.context)

            val linearLayoutArticle = view.findViewById<LinearLayout>(R.id.Article)
            val movement = view.findViewById<ImageView>(R.id.Storie)

            val id = view.findViewById<TextView>(R.id.idArticle)
            val description = view.findViewById<TextView>(R.id.descriptionArticle)
            val stock = view.findViewById<TextView>(R.id.stockArticle)
            val price = view.findViewById<TextView>(R.id.priceArticle)
            val priceWithIVA = view.findViewById<TextView>(R.id.priceWithIVA)

            val delete = view.findViewById<ImageView>(R.id.deleteMain)
            val moreStock = view.findViewById<ImageView>(R.id.Mas)
            val lessStock = view.findViewById<ImageView>(R.id.Menos)

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
                deleteArticle(article, view, database)

            }


            moreStock.setOnClickListener {
                stockDate(article, true)
            }

            lessStock.setOnClickListener {
                stockDate(article, false)
            }

            movement.setOnClickListener {
                val intent = Intent(view.context, Movements::class.java)
                intent.putExtra("idArticle", article.idArticle)
                _main?.startActivity(intent)
            }

            view.setOnClickListener{
                val bundle = Bundle()
                bundle.putSerializable("Article", article)
                bundle.putBoolean("Edit", true)
                fragmentAddArticle.arguments = bundle
                replaceFragment(fragmentAddArticle)
            }


        }

        private fun replaceFragment(fragment: Fragment) {
            (view.context as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment).commit()
        }

        @SuppressLint("SetTextI18n", "ShowToast")
        private fun stockDate(article: Article, moreOrLess: Boolean) {
            _year = c[Calendar.YEAR]
            _month = c[Calendar.MONTH]
            _day = c[Calendar.DAY_OF_MONTH]
            val alert: AlertDialog = AlertDialog.Builder(view.context).create()

            val dialogView = LayoutInflater.from(view.context).inflate(R.layout.calendar, null)

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

            dialogView.findViewById<View>(R.id.calendardata)
                .setOnClickListener { v -> DialogCalendar.dialog(v.context, tv) }

            alert.setView(dialogView)
            var type: Char

            alert.setButton(
                AlertDialog.BUTTON_POSITIVE,
                "Aceptar",
                DialogInterface.OnClickListener { _, _ ->
                    try {
                        dates = DialogCalendar.changeFormatDate(
                            tv.text.toString(),
                            "dd/MM/yyyy",
                            "yyyy/MM/dd"
                        )
                        if (moreOrLess) {
                            val sumaStock =
                                article.stockArticle.toString().toInt() + stock.text.toString()
                                    .toInt()
                            article.stockArticle = sumaStock
                            type = 'E'
                        } else {
                            val restaStock =
                                article.stockArticle.toString().toInt() - stock.text.toString()
                                    .toInt()
                            article.stockArticle = restaStock
                            type = 'S'
                        }

                        val movement = Movement(
                            0,
                            article.idArticle,
                            dates.toString(),
                            stock.text.toString().toInt(),
                            type
                        )

                        CoroutineScope(Dispatchers.IO).launch {
                            database.Articles().update(article)
                            database.Articles().insertMovement(movement)
                        }
                        Toast.makeText(
                            view.context,
                            "Movimiento y Articulo actualizado",
                            Toast.LENGTH_SHORT
                        )
                    } catch (e: Exception) {
                        Toast.makeText(
                            view.context,
                            "El stock no ha sido modificado ya que no has introducido ningún valor.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@OnClickListener
                    }
                })

            alert.show()
        }

    }
}