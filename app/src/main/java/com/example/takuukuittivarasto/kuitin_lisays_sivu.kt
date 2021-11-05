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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import kotlinx.android.synthetic.main.activity_kuitin_lisays_sivu.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class kuitin_lisays_sivu : AppCompatActivity() {
    private val REQUEST_PERMISSION = 100 //luvat kameran käyttöön ja kuvagallerian
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_PICK_IMAGE = 2
    private var IMAGE_BITMAP:Bitmap? = null;
    private lateinit var database: TakuukuittiDB
    private lateinit var dao: TakuukuittiDBDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kuitin_lisays_sivu)
        Log.d("testi", "Saavuttiin kuitin_lisäys -sivulle.")
        database = TakuukuittiDB.getInstance(applicationContext)
        dao=database.takuukuittiDBDao

        takaisin1Btn.setOnClickListener { takaisinMainActivityyn() }

        btCapturePhoto.setOnClickListener {
            Log.d("testi", "kameran luvat tarkastetaan seuraavaksi..")
            checkCameraPermission()
            openCamera()
        }
        btOpenGallery.setOnClickListener {
            Log.d("testi", "galleria avataan seuraavaksi..")
            openGallery()
        }
        tallennaBtn.setOnClickListener {
            Log.d("testi", "tallenna() -kohtaa kutsutaan seuraavaksi..")
            tallenna()
        }
        /*pvmTakuu.setOnDateChangeListener( CalendarView.OnDateChangeListener{ kalenteri, vuosi, kuukausi, paiva ->
            Log.d("testi", "${pvmTakuu.date}, $vuosi, $kuukausi, $paiva")
            var kuukausi_t = kuukausi + 1 //kuukaudet alkaa nollasta joten lisätään 1
            var uusi_date_format = SimpleDateFormat("yyyy-MM-dd") // tehdään jotta voidaan muokata stringi dateksi
            var uusi_date = uusi_date_format.parse("$vuosi-$kuukausi_t-$paiva") // muunnetaan stringi dateksi
            pvmTakuu.setDate(uusi_date.time) // laitetaan kalenterin ajaksi tämä, muuten ei päivitä aikaa jostain syystä :)
        })*/
    }//onCreate

    fun tallenna() {
        Log.d("testi", "tallenna() -kohdassa ollaan.")
       // kuva pitää tallentaa bytearrayna!
        var kuitti = Takuukuitti(1, txtNimi.text.toString(), Date(), IMAGE_BITMAP)

        Log.d("testi", "${kuitti.toString()}")

        if(txtNimi.text.toString() != ""){ //tallennetaan vain jos nimi määrätty, voi laittaa muitakin ehtoja
            //seuraava tapahtuu eri säikeessä:
            GlobalScope.launch(context = Dispatchers.Default) {
                dao.lisaaUusiKuitti(txtNimi.text.toString(),123444555,"") //id tulee automaattisesti
                var kuittilistaus=dao.haeKuitit()
                kuittilistaus.forEach{
                    Log.d("testi", "Tietokannan sisältö: "+it.id.toString() + " " + it.tuotenimi + " " + it.takuupvm)
                }
            }
        }else{
            Toast.makeText(getApplicationContext(), "Laita nimi!", Toast.LENGTH_LONG).show()
        }

    }



    fun takaisinMainActivityyn(){
        Log.d("testi", "siirrytään mainActivityyn seuraavaksi..")
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
                ivKuitti.setImageBitmap(bitmap)
                IMAGE_BITMAP = ivKuitti.drawable.toBitmap()
            }
            else if (requestCode == REQUEST_PICK_IMAGE) {
                val uri = data?.getData()
                ivKuitti.setImageURI(uri)
                IMAGE_BITMAP = ivKuitti.drawable.toBitmap()
            }
        }
    }



}//kuitin_lisays_sivu