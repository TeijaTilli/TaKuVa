package com.example.takuukuittivarasto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
// Tommi Puurunen
//Teija
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lisaaTakuukuittiBtn.setOnClickListener { siirryLisaamaanKuittia() }

    }

    fun siirryLisaamaanKuittia(){
        val siirry = Intent(this, kuitin_lisays_sivu::class.java)
        startActivity(siirry)
    }

} //MainActivity loppuu