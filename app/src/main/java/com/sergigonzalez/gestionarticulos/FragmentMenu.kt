package com.sergigonzalez.gestionarticulos

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
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
        val boton = view.findViewById<FloatingActionButton>(R.id.floatBoton)
        boton.setOnClickListener{
            _main = view.context as MainActivity?
            val intent = Intent(_main, NewArticle::class.java)
            startActivity(intent)
        }
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.Movements -> {
                startActivity(Intent(_main, MovementsAll::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}