package com.sergigonzalez.gestionarticulos

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sergigonzalez.gestionarticulos.data.ArticleApp
import com.sergigonzalez.gestionarticulos.data.Movement
import java.text.ParseException

class Movements : AppCompatActivity() {
    private var _list: ListView? = null
    private lateinit var dateI : EditText
    private lateinit var dateF : EditText
    private lateinit var calendarI : ImageView
    private lateinit var calendarF : ImageView
    private lateinit var idArticle: String
    private val _adapter: Movements_adapter? = null
    private var listMovements: List<Movement> = emptyList()
    val database = ArticleApp.getDatabase(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movement)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        idArticle = intent.getStringExtra("idArticle").toString()


        _list = findViewById(R.id.listMovement)


        dateI = findViewById(R.id.edtDateI)
        dateF = findViewById(R.id.edtDateF)

        calendarI = findViewById(R.id.calendarI)
        calendarI.setOnClickListener{
            DialogCalendar.Dialog(this@Movements, dateI)
        }

        calendarF = findViewById(R.id.calendarF)
        calendarF.setOnClickListener{
            DialogCalendar.Dialog(this@Movements, dateF)
        }

        val btnC = findViewById<View>(R.id.ButtonClean) as FloatingActionButton
        btnC.setOnClickListener { Reset() }

        val btn = findViewById<View>(R.id.buttonSearch) as FloatingActionButton
        btn.setOnClickListener {
            try {
                Search()
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }

    }

    fun Search(){
        val _dayI: String
        val _dayF: String

        if(dateI.text.toString().isNotEmpty() && dateF.text.toString().isNotEmpty()) {
            _dayI = DialogCalendar.ChangeFormatDate(
                    dateI.text.toString(),
                    "dd/MM/yyyy",
                    "yyyy/MM/dd"
            )
            _dayF = DialogCalendar.ChangeFormatDate(
                    dateF.text.toString(),
                    "dd/MM/yyyy",
                    "yyyy/MM/dd"
            )
            database.Articles().dateIDateF(idArticle,_dayF, _dayI).observe(this, Observer {
                listMovements = it
                val adapter = Movements_adapter(this, listMovements)

                _list!!.adapter = adapter

            })
            _list?.visibility = View.VISIBLE

        } else if(dateI.text.toString().isNotEmpty()) {
            _dayI = DialogCalendar.ChangeFormatDate(
                    dateI.text.toString(),
                    "dd/MM/yyyy",
                    "yyyy/MM/dd"
            )
            database.Articles().dateI(idArticle, _dayI).observe(this, Observer {
                listMovements = it
                val adapter = Movements_adapter(this, listMovements)

                _list!!.adapter = adapter

            })
            _list?.visibility = View.VISIBLE

        } else if(dateF.text.toString().isNotEmpty()) {
            _dayF = DialogCalendar.ChangeFormatDate(
                    dateF.text.toString(),
                    "dd/MM/yyyy",
                    "yyyy/MM/dd"
            )
            database.Articles().dateF(idArticle, _dayF).observe(this, Observer {
                listMovements = it
                val adapter = Movements_adapter(this, listMovements)

                _list!!.adapter = adapter

            })
            _list?.visibility = View.VISIBLE
        } else {
            database.Articles().getAllMovements().observe(this, Observer {
                listMovements = it
                val adapter = Movements_adapter(this, listMovements)

                _list!!.adapter = adapter

            })
            _list?.visibility = View.VISIBLE
        }
    }

    fun Reset(){
        dateI.setText("")
        dateF.setText("")
        _list?.visibility = View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

class Movements_adapter(private val mContext: Context, private val listMovements: List<Movement>) : ArrayAdapter<Movement>(
        mContext,
        0,
        listMovements
) {

    private lateinit var database: ArticleApp

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

        type.text = movement.type.toString()

        try {
            day.text = DialogCalendar.ChangeFormatDate(movement.day, "yyyy/MM/dd", "dd/MM/yyyy")
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return layout
    }
}