package com.sergigonzalez.gestionarticulos.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.sergigonzalez.gestionarticulos.R
import com.sergigonzalez.gestionarticulos.databinding.FragmentInfoBinding


class FragmentInfo : Fragment() {
    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fadein = AnimationUtils.loadAnimation(this@FragmentInfo.requireContext(), R.anim.fade_in)
        val rotate = AnimationUtils.loadAnimation(this@FragmentInfo.requireContext(), R.anim.rotate)
        binding.tvCreatedBy.startAnimation(fadein)
        binding.tvCreatedBy.startAnimation(rotate)
        binding.tvGithub.startAnimation(rotate)
        binding.tvGithub.startAnimation(fadein)
        binding.ivGithub.startAnimation(rotate)
        binding.ivGithub.startAnimation(fadein)
        binding.tvGithub.setOnClickListener{
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://github.com/TheHypnoo")
            startActivity(i)
        }
    }

}