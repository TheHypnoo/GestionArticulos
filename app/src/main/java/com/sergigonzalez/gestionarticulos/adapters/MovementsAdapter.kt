package com.sergigonzalez.gestionarticulos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sergigonzalez.gestionarticulos.R
import com.sergigonzalez.gestionarticulos.`object`.DialogCalendar
import com.sergigonzalez.gestionarticulos.data.Article
import com.sergigonzalez.gestionarticulos.data.ArticleApp
import com.sergigonzalez.gestionarticulos.data.Movement
import java.text.ParseException

class MovementsAdapter(private val listMovement : List<Movement>) : RecyclerView.Adapter<MovementsAdapter.MovementHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovementHolder {
        val viewInflater = LayoutInflater.from(parent.context)
        return MovementHolder(viewInflater.inflate(R.layout.elements_movement, parent, false))
    }

    override fun onBindViewHolder(holder: MovementHolder, position: Int) {
        holder.render(listMovement[position])

    }

    override fun getItemCount(): Int = listMovement.size

    class MovementHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var database: ArticleApp
        fun render(Movement: Movement) {
            val day = view.findViewById<TextView>(R.id.tvDate)
            database = ArticleApp.getDatabase(view.context)


            val quantity = view.findViewById<TextView>(R.id.tvCan)

            val type = view.findViewById<TextView>(R.id.tvType)

            quantity.text = Movement.quantity.toString()

            if(Movement.type.toString() == "E") {
                type.text = "Entrada"
            } else {
                type.text = "Salida"
            }


            try {
                day.text = DialogCalendar.changeFormatDate(Movement.day, "yyyy/MM/dd", "dd/MM/yyyy")
            } catch (e: ParseException) {
                e.printStackTrace()
            }

        }
    }
}


/*
class MovementsAdapter(private val mContext: Context, private val listMovements: List<Movement>) : ArrayAdapter<Movement>(
        mContext,
        0,
        listMovements
) {

    private lateinit var database: ArticleApp

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(mContext).inflate(
                R.layout.elements_movement,
                parent,
                false
        )
        val day = layout.findViewById<TextView>(R.id.tvDate)
        database = ArticleApp.getDatabase(mContext)
        val movement = listMovements[position]


        val quantity = layout.findViewById<TextView>(R.id.tvCan)

        val type = layout.findViewById<TextView>(R.id.tvType)

        quantity.text = movement.quantity.toString()

        if(movement.type.toString() == "E") {
            type.text = "Entrada"
        } else {
            type.text = "Salida"
        }


        try {
            day.text = DialogCalendar.changeFormatDate(movement.day, "yyyy/MM/dd", "dd/MM/yyyy")
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return layout
    }
}
 */