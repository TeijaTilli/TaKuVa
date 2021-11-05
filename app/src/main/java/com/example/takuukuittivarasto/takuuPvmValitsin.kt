package com.example.takuukuittivarasto

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.CalendarView
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_kuitin_lisays_sivu.*
import kotlinx.android.synthetic.main.activity_takuu_pvm_valitsin.*
import kotlinx.android.synthetic.main.activity_takuu_pvm_valitsin.pvmTakuu
import java.text.SimpleDateFormat
import java.util.*

class takuuPvmValitsin : AppCompatActivity() {
    private lateinit var viewModel:LisattavaKuitti
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_takuu_pvm_valitsin)
        btVuosiTaaksepain.setOnClickListener {
            var pvm = pvmTakuu.date-31557600000
            pvmTakuu.date = pvm;
        }
        btVuosiEteenpain.setOnClickListener {
            var pvm = pvmTakuu.date+31557600000
            pvmTakuu.date = pvm;
        }
        pvmTakuu.setOnDateChangeListener( CalendarView.OnDateChangeListener{ kalenteri, vuosi, kuukausi, paiva ->
            Log.d("testi", "${pvmTakuu.date}, $vuosi, $kuukausi, $paiva")
            var kuukausi_t = kuukausi + 1 //kuukaudet alkaa nollasta joten lisätään 1
            var uusi_date_format = SimpleDateFormat("yyyy-MM-dd") // tehdään jotta voidaan muokata stringi dateksi
            var uusi_date = uusi_date_format.parse("$vuosi-$kuukausi_t-$paiva") // muunnetaan stringi dateksi
            pvmTakuu.setDate(uusi_date.time) // laitetaan kalenterin ajaksi tämä, muuten ei päivitä aikaa jostain syystä :)
            viewModel.takuupvm = Date(pvmTakuu.date)
        })
        btTallenna.setOnClickListener {

            val siirry = Intent(this, kuitin_lisays_sivu::class.java)
            viewModel.takuupvm = Date(pvmTakuu.date)
            startActivity(siirry)
        }
    }
    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        viewModel = ViewModelProvider(this).get(LisattavaKuitti::class.java)
        return super.onCreateView(name, context, attrs)
    }
}