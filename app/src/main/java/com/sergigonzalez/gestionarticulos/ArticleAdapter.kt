package com.sergigonzalez.gestionarticulos

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.sergigonzalez.gestionarticulos.data.Article

class ArticleAdapter(private val mContext: Context, private val listArticle: List<Article>) : ArrayAdapter<Article>(mContext, 0, listArticle) {

    private val NoHaveStock = "#d78290"
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(mContext).inflate(R.layout.item_article, parent, false)

        val article = listArticle[position]

        val LinearLayoutArticle = layout.findViewById<LinearLayout>(R.id.Article)

        val id = layout.findViewById<TextView>(R.id.idArticle)
        val description = layout.findViewById<TextView>(R.id.descriptionArticle)
        val stock = layout.findViewById<TextView>(R.id.stockArticle)
        val price = layout.findViewById<TextView>(R.id.priceArticle)

        val delete = layout.findViewById<ImageView>(R.id.deleteMain)

        id.text = article.idArticle
        description.text = article.descriptionArticle
        stock.text = article.stockArticle.toString()

        if (stock.text.toString().toInt() <= 0) {

            LinearLayoutArticle!!.setBackgroundColor(Color.parseColor(NoHaveStock))
        }

        price.text = "${article.priceArticle}€"

        return layout
    }

}