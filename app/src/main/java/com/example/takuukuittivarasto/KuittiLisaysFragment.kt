package com.example.takuukuittivarasto

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.takuukuittivarasto.databinding.FragmentKuittiLisaysBinding;
import kotlinx.android.synthetic.main.activity_kuitin_lisays_sivu.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

// TODO: Rename parameter arguments, choose names that match

/**
 * A simple [Fragment] subclass.
 * Use the [KuittiLisaysFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class KuittiLisaysFragment : Fragment() {
    private val REQUEST_PERMISSION = 100 //luvat kameran käyttöön ja kuvagallerian
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_PICK_IMAGE = 2
    private var IMAGE_BITMAP: Bitmap? = null;
    private lateinit var database: TakuukuittiDB
    private lateinit var dao: TakuukuittiDBDao
    private lateinit var binding : FragmentKuittiLisaysBinding;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        database = TakuukuittiDB.getInstance(requireContext())
        dao=database.takuukuittiDBDao
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_kuitti_lisays, container, false)
        binding.btCapturePhoto.setOnClickListener {
            checkCameraPermission()
            openCamera()
        }
        binding.btOpenGallery.setOnClickListener {
            openGallery()
        }
        binding.tallennaBtn.setOnClickListener {
            tallenna()
        }
        return binding.root
    }
    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_PERMISSION)
        }
    }
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
            Toast.makeText(requireContext(), "Laita nimi!", Toast.LENGTH_LONG).show()
        }

    }
    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            this.activity?.let {
                intent.resolveActivity(it.packageManager)?.also {
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }
    private fun openGallery() {
        Intent(Intent.ACTION_GET_CONTENT).also { intent ->
            intent.type = "image/*"
            this.activity?.let {
                intent.resolveActivity(it.packageManager)?.also {
                    startActivityForResult(intent, REQUEST_PICK_IMAGE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
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


}