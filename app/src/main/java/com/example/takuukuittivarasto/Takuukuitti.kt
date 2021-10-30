package com.example.takuukuittivarasto

import android.graphics.Bitmap
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.util.*

data class Takuukuitti(val id: Int, val otsikko: String, val paattymispvm : Date, val kuva : Bitmap?) {
    fun teeNappi(sivu: AppCompatActivity):Button {
        val nappi = Button(sivu)
        nappi.text="${this.otsikko} > ${this.paattymispvm}"
        return nappi
    }
}