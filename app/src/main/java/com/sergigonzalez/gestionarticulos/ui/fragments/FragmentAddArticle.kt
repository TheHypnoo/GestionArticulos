package com.sergigonzalez.gestionarticulos.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sergigonzalez.gestionarticulos.R
import com.sergigonzalez.gestionarticulos.data.ArticleApp
import com.sergigonzalez.gestionarticulos.databinding.FragmentAddArticleBinding
import com.sergigonzalez.gestionarticulos.databinding.FragmentWeatherBinding

class FragmentAddArticle : Fragment() {
    private var _binding: FragmentAddArticleBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: ArticleApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddArticleBinding.inflate(inflater, container, false)
        database = ArticleApp.getDatabase(this@FragmentAddArticle.requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}