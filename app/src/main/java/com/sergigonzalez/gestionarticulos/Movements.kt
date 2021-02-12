package com.sergigonzalez.gestionarticulos

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sergigonzalez.gestionarticulos.`object`.DialogCalendar
import com.sergigonzalez.gestionarticulos.adapters.MovementsAdapter
import com.sergigonzalez.gestionarticulos.data.ArticleApp
import com.sergigonzalez.gestionarticulos.data.Movement
import java.text.ParseException

class Movements : AppCompatActivity() {
    private var _list: RecyclerView? = null
    private lateinit var dateI : EditText
    private lateinit var dateF : EditText
    private lateinit var calendarI : ImageView
    private lateinit var calendarF : ImageView
    private lateinit var idArticle: String
    private var listMovements: List<Movement> = emptyList()
    private val database = ArticleApp.getDatabase(this)

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
            DialogCalendar.dialog(this@Movements, dateI)
        }

        calendarF = findViewById(R.id.calendarF)
        calendarF.setOnClickListener{
            DialogCalendar.dialog(this@Movements, dateF)
        }

        val btnC = findViewById<View>(R.id.ButtonClean) as FloatingActionButton
        btnC.setOnClickListener { reset() }

        val btn = findViewById<View>(R.id.buttonSearch) as FloatingActionButton
        btn.setOnClickListener {
            try {
                search()
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }

    }

    private fun search(){
        val dayI: String
        val dayF: String

        if(dateI.text.toString().isNotEmpty() && dateF.text.toString().isNotEmpty()) {
            dayI = DialogCalendar.changeFormatDate(
                    dateI.text.toString(),
                    "dd/MM/yyyy",
                    "yyyy/MM/dd"
            )
            dayF = DialogCalendar.changeFormatDate(
                    dateF.text.toString(),
                    "dd/MM/yyyy",
                    "yyyy/MM/dd"
            )


            database.Articles().dateIDateF(idArticle,dayF, dayI).observe(this, {
                listMovements = it
                val adapter = MovementsAdapter(listMovements)
                _list?.layoutManager = LinearLayoutManager(this)
                _list?.addItemDecoration(
                    DividerItemDecoration(
                        this,
                        DividerItemDecoration.VERTICAL
                    ))
                _list?.adapter = adapter
            })
            _list?.visibility = View.VISIBLE

        } else if(dateI.text.toString().isNotEmpty()) {
            dayI = DialogCalendar.changeFormatDate(
                    dateI.text.toString(),
                    "dd/MM/yyyy",
                    "yyyy/MM/dd"
            )
            database.Articles().dateI(idArticle, dayI).observe(this, {
                listMovements = it
                val adapter = MovementsAdapter(listMovements)
                _list?.layoutManager = LinearLayoutManager(this)
                _list?.addItemDecoration(
                    DividerItemDecoration(
                        this,
                        DividerItemDecoration.VERTICAL
                    ))
                _list?.adapter = adapter
            })
            _list?.visibility = View.VISIBLE

        } else if(dateF.text.toString().isNotEmpty()) {
            dayF = DialogCalendar.changeFormatDate(
                    dateF.text.toString(),
                    "dd/MM/yyyy",
                    "yyyy/MM/dd"
            )
            database.Articles().dateF(idArticle, dayF).observe(this, {
                listMovements = it
                val adapter = MovementsAdapter(listMovements)
                _list?.layoutManager = LinearLayoutManager(this)
                _list?.addItemDecoration(
                    DividerItemDecoration(
                        this,
                        DividerItemDecoration.VERTICAL
                    ))
                _list?.adapter = adapter
            })
            _list?.visibility = View.VISIBLE
        } else {
            database.Articles().getAllMovements().observe(this, {
                listMovements = it
                val adapter = MovementsAdapter(listMovements)
                _list?.layoutManager = LinearLayoutManager(this)
                _list?.addItemDecoration(
                    DividerItemDecoration(
                        this,
                        DividerItemDecoration.VERTICAL
                    ))
                _list?.adapter = adapter

            })
            _list?.visibility = View.VISIBLE
        }
    }

    private fun reset(){
        dateI.setText("")
        dateF.setText("")
        _list?.visibility = View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}