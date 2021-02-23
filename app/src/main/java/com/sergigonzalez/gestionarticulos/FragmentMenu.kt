package com.sergigonzalez.gestionarticulos

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class FragmentMenu : Fragment() {

    private var _main: MainActivity? = null

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
        _main = view.context as MainActivity?
        navigationView.selectedItemId = R.id.miHome
        navigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.miHome -> {
                    if(!item.isChecked) {
                        val intent = Intent(_main, MainActivity::class.java)
                        startActivity(intent)
                    }
                        return@setOnNavigationItemSelectedListener true

                }
                R.id.miMovements -> {
                    if(!item.isChecked) {
                        val intent = Intent(_main, MovementsAll::class.java)
                        startActivity(intent)
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.miWeather -> {
                    if(!item.isChecked) {
                        val intent = Intent(_main, WeatherActivity::class.java)
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
            val intent = Intent(_main, NewArticle::class.java)
            startActivity(intent)
        }

        return view
    }

}