package com.sergigonzalez.gestionarticulos.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.sergigonzalez.gestionarticulos.DialogCalendar
import com.sergigonzalez.gestionarticulos.R
import com.sergigonzalez.gestionarticulos.data.ArticleApp
import com.sergigonzalez.gestionarticulos.data.Movement
import java.text.ParseException


class MovementsAllAdapter(private val mContext: Context, private val listMovements: List<Movement>) : ArrayAdapter<Movement>(
        mContext,
        0,
        listMovements
) {

    private lateinit var database: ArticleApp

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(mContext).inflate(
                R.layout.elements_movements_all,
                parent,
                false
        )
        database = ArticleApp.getDatabase(mContext)
        val movement = listMovements[position]

        val code = layout.findViewById<TextView>(R.id.tvCodeM)

        val day = layout.findViewById<TextView>(R.id.tvDateM)

        val quantity = layout.findViewById<TextView>(R.id.tvCanM)

        val type = layout.findViewById<TextView>(R.id.tvTypeM)

        code.text = movement.idArticleMovement

        quantity.text = movement.quantity.toString()

        if(movement.type.toString() == "E") {
            type.text = R.string.Entrada.toString()
        } else {
            type.text = R.string.Salida.toString()
        }

        try {
            day.text = DialogCalendar.changeFormatDate(movement.day, "yyyy/MM/dd", "dd/MM/yyyy")
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return layout
    }
}