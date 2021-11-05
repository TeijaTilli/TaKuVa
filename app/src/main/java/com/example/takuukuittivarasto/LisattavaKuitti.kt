package com.example.takuukuittivarasto

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import java.util.*

class LisattavaKuitti : ViewModel() {
    lateinit var otsikko : String
    var takuupvm : Date = Date()
        get() {
            Log.d("testi", field.toString())
            return field
        }
        set(value) {
            field = value
        }
    var kuva : Bitmap? = null
    init {
        Log.d("testi", "viewmodel created")
    }

    override fun onCleared() {
        Log.d("testi", "viewmodel exited")
        super.onCleared()
    }
}