package com.sergigonzalez.gestionarticulos.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sergigonzalez.gestionarticulos.R
import com.sergigonzalez.gestionarticulos.`object`.DialogCalendar
import com.sergigonzalez.gestionarticulos.data.ArticleApp
import com.sergigonzalez.gestionarticulos.data.Movement
import com.sergigonzalez.gestionarticulos.databinding.FragmentMovementsAllBinding
import java.text.ParseException


class FragmentMovementsAll : Fragment() {
    private var _binding: FragmentMovementsAllBinding? = null
    private val binding get() = _binding!!
    private var _list: RecyclerView? = null
    private lateinit var _calendar: ImageView
    private var listMovements: List<Movement> = emptyList()
    private lateinit var database: ArticleApp

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMovementsAllBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = ArticleApp.getDatabase(this@FragmentMovementsAll.requireContext())
        binding.calendarM.setOnClickListener {
            DialogCalendar.dialog(this@FragmentMovementsAll.requireContext(), binding.edtDateM)
        }
/*
        binding.btnSearchM.setOnClickListener {
            try {
                searchMovement()
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }

        val btnR = findViewById<View>(R.id.btnResetM) as FloatingActionButton
        btnR.setOnClickListener { resetMovements() }
*/
    }

}