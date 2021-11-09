package com.example.takuukuittivarasto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_kuittivarasto.*
import kotlinx.android.synthetic.main.activity_kuittivarasto.view.*
import java.util.*

class kuittivarasto : AppCompatActivity() {
    //private lateinit var database: Tietokanta
    //private lateinit var dao: TietokantaDao
    companion object{

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kuittivarasto)

        //database = Tietokanta.getInstance(applicationContext)
        //dao = database.TietokantaDao
        //kuittilista testaukseen

        kuittiRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@kuittivarasto)
            adapter = KuittiAdapteri()
        }
    }
}