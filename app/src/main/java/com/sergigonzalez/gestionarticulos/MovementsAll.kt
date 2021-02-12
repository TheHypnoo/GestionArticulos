package com.sergigonzalez.gestionarticulos

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sergigonzalez.gestionarticulos.`object`.DialogCalendar
import com.sergigonzalez.gestionarticulos.adapters.ArticleAdapter
import com.sergigonzalez.gestionarticulos.adapters.MovementsAdapter
import com.sergigonzalez.gestionarticulos.adapters.MovementsAllAdapter
import com.sergigonzalez.gestionarticulos.data.ArticleApp
import com.sergigonzalez.gestionarticulos.data.Movement
import java.text.ParseException

class MovementsAll : AppCompatActivity() {
    private var _list: RecyclerView? = null
    private lateinit var edt: EditText
    private lateinit var _calendar: ImageView
    private var listMovements: List<Movement> = emptyList()
    private val database = ArticleApp.getDatabase(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movements_all)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        edt = findViewById(R.id.edtDateM)

        _list = findViewById(R.id.list_movement_all)

        _calendar = findViewById(R.id.calendarM)
        _calendar.setOnClickListener {
            DialogCalendar.dialog(this@MovementsAll, edt)
        }

        val btn = findViewById<View>(R.id.btnSearchM) as FloatingActionButton
        btn.setOnClickListener {
            try {
                searchMovement()
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }

        val btnR = findViewById<View>(R.id.btnResetM) as FloatingActionButton
        btnR.setOnClickListener { resetMovements() }

    }

    private fun resetMovements() {
        edt.setText("")
        _list?.visibility = View.GONE
    }

    private fun searchMovement() {
        if (edt.text.toString().isNotEmpty()) {
            val date =
                DialogCalendar.changeFormatDate(edt.text.toString(), "dd/MM/yyyy", "yyyy/MM/dd")

            database.Articles().dateMovements(date).observe(this, {
                listMovements = it
                val adapter = MovementsAllAdapter(listMovements)
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
            database.Articles().dateMovementsAll().observe(this, {
                listMovements = it
                val adapter = MovementsAllAdapter(listMovements)
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}