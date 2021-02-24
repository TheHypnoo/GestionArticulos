package com.sergigonzalez.gestionarticulos.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sergigonzalez.gestionarticulos.*
import com.sergigonzalez.gestionarticulos.ui.activitys.MainActivity
import com.sergigonzalez.gestionarticulos.ui.activitys.MovementsAll
import com.sergigonzalez.gestionarticulos.ui.activitys.NewArticle
import com.sergigonzalez.gestionarticulos.ui.activitys.WeatherActivity


class FragmentMenu : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_menu, container, false)
        //NavigationView
        val navigationView = view.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        navigationView.background = null
        navigationView.menu.getItem(2).isEnabled = false
        //Main
        navigationView.selectedItemId = R.id.miHome
        navigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.miHome -> {
                    if(!item.isChecked) {
                        val intent = Intent(this@FragmentMenu.context, MainActivity::class.java)
                        startActivity(intent)
                    }
                        return@setOnNavigationItemSelectedListener true
                }
                R.id.miMovements -> {
                    if(!item.isChecked) {
                        val intent = Intent(this@FragmentMenu.context, MovementsAll::class.java)
                        startActivity(intent)
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.miWeather -> {
                    if(!item.isChecked) {
                        val intent = Intent(this@FragmentMenu.context, WeatherActivity::class.java)
                        startActivity(intent)
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }

        //FAB
        val boton = view.findViewById<FloatingActionButton>(R.id.fab)
        boton.setOnClickListener {
            val intent = Intent(this@FragmentMenu.context, NewArticle::class.java)
            startActivity(intent)
        }

        return view
    }

}