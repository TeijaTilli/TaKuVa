package com.example.takuukuittivarasto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import kotlinx.android.synthetic.main.activity_main.*
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