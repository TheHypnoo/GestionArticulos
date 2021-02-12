package com.sergigonzalez.gestionarticulos.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.sergigonzalez.gestionarticulos.*
import com.sergigonzalez.gestionarticulos.`object`.DialogCalendar
import com.sergigonzalez.gestionarticulos.data.ArticleApp
import com.sergigonzalez.gestionarticulos.data.Movement
import java.text.ParseException

class MovementsAllAdapter(private val listMovements: List<Movement>) :
    RecyclerView.Adapter<MovementsAllAdapter.MovementsAllHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovementsAllHolder {
        val viewInflater = LayoutInflater.from(parent.context)
        return MovementsAllHolder(
            viewInflater.inflate(
                R.layout.elements_movements_all,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovementsAllHolder, position: Int) {
        holder.render(listMovements[position])
    }

    override fun getItemCount(): Int = listMovements.size


    class MovementsAllHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var database: ArticleApp

        fun render(Movement: Movement) {
            database = ArticleApp.getDatabase(view.context)

            val code = view.findViewById<TextView>(R.id.tvCodeM)

            val day = view.findViewById<TextView>(R.id.tvDateM)

            val quantity = view.findViewById<TextView>(R.id.tvCanM)

            val type = view.findViewById<TextView>(R.id.tvTypeM)

            code.text = Movement.idArticleMovement

            quantity.text = Movement.quantity.toString()

            if (Movement.type.toString() == "E") {
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