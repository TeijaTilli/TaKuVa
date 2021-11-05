package com.example.takuukuittivarasto

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.CalendarView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_kuitin_lisays_sivu.*
import java.text.SimpleDateFormat
import java.util.*

class kuitin_lisays_sivu : AppCompatActivity() {
    private val REQUEST_PERMISSION = 100 //luvat kameran käyttöön ja kuvagallerian
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_PICK_IMAGE = 2
    private var IMAGE_BITMAP:Bitmap? = null;
    private var TAKUUPVM:Date = Date();
    private lateinit var viewModel:LisattavaKuitti

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kuitin_lisays_sivu)


        takaisin1Btn.setOnClickListener { takaisinMainActivityyn() }
        Log.d("testi", "init kuitin lisays")
        btCapturePhoto.setOnClickListener {
            checkCameraPermission()
            openCamera()
        }
        btOpenGallery.setOnClickListener {
            openGallery()
        }
        tallenna1Btn.setOnClickListener {
            tallenna()
        }
        btKalenteriin.setOnClickListener {
            val siirry = Intent(this, takuuPvmValitsin::class.java)
            startActivity(siirry)
        }
    }//onCreate

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        viewModel = ViewModelProvider(this).get(LisattavaKuitti::class.java)
        Log.d("testi", viewModel.takuupvm.toString())
        return super.onCreateView(name, context, attrs)
    }
    fun tallenna() {
        var kuitti = Takuukuitti(1, txtNimi.text.toString(), TAKUUPVM, IMAGE_BITMAP)
        Log.d("testi", "${TAKUUPVM}")
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
                val uri = data?.data
                ivImage.setImageURI(uri)
                IMAGE_BITMAP = ivImage.drawable.toBitmap()
            }
        }
    }



}//kuitin_lisays_sivu