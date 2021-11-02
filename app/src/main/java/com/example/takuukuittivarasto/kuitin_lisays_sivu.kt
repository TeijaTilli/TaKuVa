package com.example.takuukuittivarasto

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.CalendarView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import kotlinx.android.synthetic.main.activity_kuitin_lisays_sivu.*
import java.text.SimpleDateFormat
import java.util.*

class kuitin_lisays_sivu : AppCompatActivity() {
    private val REQUEST_PERMISSION = 100 //luvat kameran käyttöön ja kuvagallerian
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_PICK_IMAGE = 2
    private var IMAGE_BITMAP:Bitmap? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kuitin_lisays_sivu)

        takaisin1Btn.setOnClickListener { takaisinMainActivityyn() }
        btCapturePhoto.setOnClickListener {
            checkCameraPermission()
            openCamera()
            checkCameraPermission()
        }
        btOpenGallery.setOnClickListener {
            openGallery()
        }
        tallenna1Btn.setOnClickListener {
            tallenna()
        }
        pvmTakuu.setOnDateChangeListener( CalendarView.OnDateChangeListener{ kalenteri, vuosi, kuukausi, paiva ->
            Log.d("testi", "${pvmTakuu.date}, $vuosi, $kuukausi, $paiva")
            var kuukausi_t = kuukausi + 1 //kuukaudet alkaa nollasta joten lisätään 1
            var uusi_date_format = SimpleDateFormat("yyyy-MM-dd") // tehdään jotta voidaan muokata stringi dateksi
            var uusi_date = uusi_date_format.parse("$vuosi-$kuukausi_t-$paiva") // muunnetaan stringi dateksi
            pvmTakuu.setDate(uusi_date.time) // laitetaan kalenterin ajaksi tämä, muuten ei päivitä aikaa jostain syystä :)
        })
    }//onCreate

    fun tallenna() {
        var kuitti = Takuukuitti(1, txtNimi.text.toString(), Date(pvmTakuu.date), IMAGE_BITMAP)
        Log.d("testi", "${pvmTakuu.getDate()}")
        Log.d("testi", "${kuitti.toString()}")
    }
    fun takaisinMainActivityyn(){
        val siirry = Intent(this, MainActivity::class.java)
        startActivity(siirry)
    }

    //seuraavat funktiot kuvien ottoa varten:
    override fun onResume() {
        super.onResume()
        checkCameraPermission()
    }
    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_PERMISSION)
        }
    }

    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(packageManager)?.also {
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }
    private fun openGallery() {
        Intent(Intent.ACTION_GET_CONTENT).also { intent ->
            intent.type = "image/*"
            intent.resolveActivity(packageManager)?.also {
                startActivityForResult(intent, REQUEST_PICK_IMAGE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                val bitmap = data?.extras?.get("data") as Bitmap
                ivImage.setImageBitmap(bitmap)
                IMAGE_BITMAP = ivImage.drawable.toBitmap()
            }
            else if (requestCode == REQUEST_PICK_IMAGE) {
                val uri = data?.getData()
                ivImage.setImageURI(uri)
                IMAGE_BITMAP = ivImage.drawable.toBitmap()
            }
        }
    }



}//kuitin_lisays_sivu