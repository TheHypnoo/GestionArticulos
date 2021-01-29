package com.sergigonzalez.gestionarticulos

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sergigonzalez.gestionarticulos.data.ArticleApp
import com.sergigonzalez.gestionarticulos.data.Movement
import java.text.ParseException

class Movements_All : AppCompatActivity() {
    private var _list: ListView? = null
    private lateinit var edt : EditText
    private lateinit var _calendar : ImageView
    private var listMovements: List<Movement> = emptyList()
    val database = ArticleApp.getDatabase(this)
    private val adapter: Movements_all_Adapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movements_all)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        edt = findViewById(R.id.edtDateM)

        _list = findViewById(R.id.list_movement_all)

        _calendar = findViewById(R.id.calendarM)
        _calendar.setOnClickListener{
            DialogCalendar.Dialog(this@Movements_All, edt)
        }

        val btn = findViewById<View>(R.id.btnSearchM) as FloatingActionButton
        btn.setOnClickListener {
            try {
                SearchMovements()
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }

        val btnR = findViewById<View>(R.id.btnResetM) as FloatingActionButton
        btnR.setOnClickListener { resetMovements() }

    }

    private fun resetMovements(){
        edt.setText("")
        _list?.visibility = View.GONE
    }

    fun SearchMovements(){
        if (!edt.text.toString().isEmpty()) {
            val date = DialogCalendar.ChangeFormatDate(edt.text.toString(), "dd/MM/yyyy", "yyyy/MM/dd")
            database.Articles().dateMovements(date).observe(this, Observer {
                listMovements = it
                val adapter = Movements_all_Adapter(this, listMovements)

                _list!!.adapter = adapter

            })
            _list?.visibility = View.VISIBLE
        } else {
            database.Articles().dateMovementsAll().observe(this, Observer {
                listMovements = it
                val adapter = Movements_all_Adapter(this, listMovements)

                _list!!.adapter = adapter

            })
            _list?.visibility = View.VISIBLE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

class Movements_all_Adapter(private val mContext: Context, private val listMovements: List<Movement>) : ArrayAdapter<Movement>(
        mContext,
        0,
        listMovements
) {

    private lateinit var database: ArticleApp

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

        type.text = movement.type.toString()

        try {
            day.text = DialogCalendar.ChangeFormatDate(movement.day, "yyyy/MM/dd", "dd/MM/yyyy")
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return layout
    }
}