package com.sergigonzalez.gestionarticulos

import android.app.DatePickerDialog
import android.content.Context
import android.widget.TextView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DialogCalendar {
    private val c = Calendar.getInstance()
    var dates: String? = null
    fun Dialog(_contex: Context?, edt: TextView) {
        val _year = c[Calendar.YEAR]
        val _month = c[Calendar.MONTH]
        val _day = c[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(_contex!!, { datePicker, years, months, days ->
            if (months < 10 && days < 10) {
                dates = "0" + days + "/" + "0" + (months + 1) + "/" + years
            } else if (months < 10) {
                dates = days.toString() + "/" + "0" + (months + 1) + "/" + years
            } else if (days < 10) {
                dates = "0" + days + "/" + (months + 1) + "/" + years
            } else {
                dates = days.toString() + "/" + (months + 1) + "/" + years
            }
            edt.text = dates
        }, _year, _month, _day)
        datePickerDialog.show()
    }

    @Throws(ParseException::class)
    fun ChangeFormatDate(_dia: String?, FormatOrigin: String?, FormatFinal: String?): String {
        val parseador = SimpleDateFormat(FormatOrigin)
        val formateador = SimpleDateFormat(FormatFinal)
        val date = parseador.parse(_dia)
        return formateador.format(date)
    }
}