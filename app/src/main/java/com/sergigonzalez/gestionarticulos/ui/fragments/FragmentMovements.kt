package com.sergigonzalez.gestionarticulos.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sergigonzalez.gestionarticulos.R
import com.sergigonzalez.gestionarticulos.`object`.DialogCalendar
import com.sergigonzalez.gestionarticulos.adapters.MovementsAdapter
import com.sergigonzalez.gestionarticulos.data.ArticleApp
import com.sergigonzalez.gestionarticulos.data.Movement
import com.sergigonzalez.gestionarticulos.databinding.FragmentMainBinding
import com.sergigonzalez.gestionarticulos.databinding.FragmentMovementsBinding

class FragmentMovements : Fragment() {
    private var _binding: FragmentMovementsBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: ArticleApp
    private var listMovements: List<Movement> = emptyList()
    private lateinit var idArticle: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMovementsBinding.inflate(inflater, container, false)
        database = ArticleApp.getDatabase(this@FragmentMovements.requireContext())
        binding.calendarI.setOnClickListener{
            DialogCalendar.dialog(this@FragmentMovements.requireContext(), binding.edtDateI)
        }

        binding.calendarF.setOnClickListener{
            DialogCalendar.dialog(this@FragmentMovements.requireContext(), binding.edtDateF)
        }
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        idArticle = arguments?.getString("idArticle").toString()
        activity?.title = "Article Movements: $idArticle"
    }

    fun search(){
        val dayI: String
        val dayF: String

        if(binding.edtDateI.text.toString().isNotEmpty() && binding.edtDateF.text.toString().isNotEmpty()) {
            dayI = DialogCalendar.changeFormatDate(
                binding.edtDateI.text.toString(),
                "dd/MM/yyyy",
                "yyyy/MM/dd"
            )
            dayF = DialogCalendar.changeFormatDate(
                binding.edtDateF.text.toString(),
                "dd/MM/yyyy",
                "yyyy/MM/dd"
            )


            database.Articles().dateIDateF(idArticle,dayF, dayI).observe(this, {
                listMovements = it
                if(listMovements.isEmpty()) {
                    binding.tvEmptyVievMovements.visibility = View.VISIBLE
                    binding.tvEmptyVievMovements.text = "No hay ningún movimiento"
                } else {
                    binding.tvEmptyVievMovements.visibility = View.GONE
                    binding.listMovement.visibility = View.VISIBLE
                }
                val adapter = MovementsAdapter(listMovements)
                binding.listMovement.layoutManager = LinearLayoutManager(this@FragmentMovements.requireContext())
                binding.listMovement.addItemDecoration(
                    DividerItemDecoration(
                        this@FragmentMovements.requireContext(),
                        DividerItemDecoration.VERTICAL
                    )
                )
                binding.listMovement.adapter = adapter
            })
            binding.listMovement.visibility = View.VISIBLE
            reset()
        } else if(binding.edtDateI.text.toString().isNotEmpty()) {
            dayI = DialogCalendar.changeFormatDate(
                binding.edtDateI.text.toString(),
                "dd/MM/yyyy",
                "yyyy/MM/dd"
            )
            database.Articles().dateI(idArticle, dayI).observe(this, {
                listMovements = it
                if(listMovements.isEmpty()) {
                    binding.tvEmptyVievMovements.visibility = View.VISIBLE
                    binding.tvEmptyVievMovements.text = "No hay ningún movimiento"
                } else {
                    binding.tvEmptyVievMovements.visibility = View.GONE
                    binding.listMovement.visibility = View.VISIBLE
                }
                val adapter = MovementsAdapter(listMovements)
                binding.listMovement.layoutManager = LinearLayoutManager(this@FragmentMovements.requireContext())
                binding.listMovement.addItemDecoration(
                    DividerItemDecoration(
                        this@FragmentMovements.requireContext(),
                        DividerItemDecoration.VERTICAL
                    )
                )
                binding.listMovement.adapter = adapter
            })
            binding.listMovement.visibility = View.VISIBLE
            reset()

        } else if(binding.edtDateF.text.toString().isNotEmpty()) {
            dayF = DialogCalendar.changeFormatDate(
                binding.edtDateF.text.toString(),
                "dd/MM/yyyy",
                "yyyy/MM/dd"
            )
            database.Articles().dateF(idArticle, dayF).observe(this, {
                listMovements = it
                if(listMovements.isEmpty()) {
                    binding.tvEmptyVievMovements.visibility = View.VISIBLE
                    binding.tvEmptyVievMovements.text = "No hay ningún movimiento"
                } else {
                    binding.tvEmptyVievMovements.visibility = View.GONE
                    binding.listMovement.visibility = View.VISIBLE
                }
                val adapter = MovementsAdapter(listMovements)
                binding.listMovement.layoutManager = LinearLayoutManager(this@FragmentMovements.requireContext())
                binding.listMovement.addItemDecoration(
                    DividerItemDecoration(
                        this@FragmentMovements.requireContext(),
                        DividerItemDecoration.VERTICAL
                    )
                )
                binding.listMovement.adapter = adapter
            })
            binding.listMovement.visibility = View.VISIBLE
            reset()
        } else {
            database.Articles().dateMovements(idArticle).observe(this, {
                listMovements = it
                if(listMovements.isEmpty()) {
                    binding.tvEmptyVievMovements.visibility = View.VISIBLE
                    binding.tvEmptyVievMovements.text = "No hay ningún movimiento"
                } else {
                    binding.tvEmptyVievMovements.visibility = View.GONE
                    binding.listMovement.visibility = View.VISIBLE
                }
                val adapter = MovementsAdapter(listMovements)
                binding.listMovement.layoutManager = LinearLayoutManager(this@FragmentMovements.requireContext())
                binding.listMovement.addItemDecoration(
                    DividerItemDecoration(
                        this@FragmentMovements.requireContext(),
                        DividerItemDecoration.VERTICAL
                    )
                )
                binding.listMovement.adapter = adapter
            })
            binding.listMovement.visibility = View.VISIBLE
            reset()
        }
    }

    private fun reset(){
        binding.edtDateI.setText("")
        binding.edtDateF.setText("")
        //binding.listMovement.visibility = View.GONE
    }

}