package com.sergigonzalez.gestionarticulos

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Movements_All : AppCompatActivity() {
    private lateinit var edt : EditText
    private lateinit var _calendar : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movements_all)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        edt = findViewById(R.id.edtDateM)
        _calendar = findViewById(R.id.calendarM)
        _calendar.setOnClickListener{
            DialogCalendar.Dialog(this@Movements_All, edt)
        }


        val btnR = findViewById<View>(R.id.btnResetM) as FloatingActionButton
        btnR.setOnClickListener { resetMovements() }


    }

    private fun resetMovements(){
        edt.setText("")
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}