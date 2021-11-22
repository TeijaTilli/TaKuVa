package com.example.takuukuittivarasto

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_kuitti_lisays.*

// Tommi Puurunen
//Teija



//Roosa T

class MainActivity : AppCompatActivity() {
    private lateinit var menuValikko: MenuValikko
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        menuValikko = MenuValikko(menuInflater,navController = NavController(this),0,fragment = null,activity = this)

    }
//Jonna
} //MainActivity loppuu