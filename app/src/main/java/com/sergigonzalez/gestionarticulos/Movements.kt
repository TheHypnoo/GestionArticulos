package com.sergigonzalez.gestionarticulos

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Movements : AppCompatActivity() {
    private lateinit var dateI : EditText
    private lateinit var dateF : EditText
    private lateinit var calendarI : ImageView
    private lateinit var calendarF : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movement)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


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

    }

    fun Reset(){
        dateI.setText("")
        dateF.setText("")
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}