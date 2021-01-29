package com.sergigonzalez.gestionarticulos

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sergigonzalez.gestionarticulos.data.Article
import com.sergigonzalez.gestionarticulos.data.ArticleApp
import java.text.ParseException

class Movements : AppCompatActivity() {
    private var _list: ListView? = null
    private lateinit var dateI : EditText
    private lateinit var dateF : EditText
    private lateinit var calendarI : ImageView
    private lateinit var calendarF : ImageView
    private lateinit var idArticle: String
    val database = ArticleApp.getDatabase(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movement)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        idArticle = intent.hasExtra("idArticle").toString()

        _list = findViewById(R.id.listMovement)

        if(_list != null) {
            _list!!.visibility = View.VISIBLE
        }


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

        if(!dateI.text.toString().isEmpty() && !dateF.text.toString().isEmpty()) {
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
            database.Articles().dateIDateF(_dayI, _dayF, idArticle)
        } else if(!dateI.text.toString().isEmpty()) {
            _dayI = DialogCalendar.ChangeFormatDate(
                dateI.text.toString(),
                "dd/MM/yyyy",
                "yyyy/MM/dd"
            )
            database.Articles().dateI(_dayI, idArticle)
        } else if(!dateF.text.toString().isEmpty()) {
            _dayF = DialogCalendar.ChangeFormatDate(
                dateF.text.toString(),
                "dd/MM/yyyy",
                "yyyy/MM/dd"
            )
            database.Articles().dateF(_dayF, idArticle)
        }
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

class Movements_adapter(private val mContext: Context, private val listArticle: List<Article>) : ArrayAdapter<Article>(
    mContext,
    0,
    listArticle
) {

    private lateinit var database: ArticleApp
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(mContext).inflate(
            R.layout.elements_movement,
            parent,
            false
        )
        var dateI = layout.findViewById<EditText>(R.id.edtDateI)
        var dateF = layout.findViewById<EditText>(R.id.edtDateF)

        database = ArticleApp.getDatabase(mContext)



        return layout
    }
}