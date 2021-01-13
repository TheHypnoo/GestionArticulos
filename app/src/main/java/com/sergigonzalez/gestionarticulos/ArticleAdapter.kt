package com.sergigonzalez.gestionarticulos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.sergigonzalez.gestionarticulos.data.Article

class ArticleAdapter(private val mContext: Context, private val listArticle: List<Article>) : ArrayAdapter<Article>(mContext, 0, listArticle) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(mContext).inflate(R.layout.item_article, parent, false)

        val article = listArticle[position]

        val name = layout.findViewById<TextView>(R.id.idArticle)
        val price = layout.findViewById<TextView>(R.id.StockArticle)

        name.text = article.idArticle
        price.text = "$${article.priceArticle}"

        return layout
    }

}