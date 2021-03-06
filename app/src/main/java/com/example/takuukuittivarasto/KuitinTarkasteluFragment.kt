package com.example.takuukuittivarasto

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.util.TypedValue
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.takuukuittivarasto.databinding.FragmentKuitinTarkasteluBinding;
import kotlinx.android.synthetic.main.fragment_kuitin_tarkastelu.*
import kotlinx.coroutines.*
import java.io.File
import java.text.DateFormat
import java.util.Date
//import androidx.test.core.app.ApplicationProvider.getApplicationContext





/**
 * A simple [Fragment] subclass.
 * Use the [KuitinTarkasteluFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class KuitinTarkasteluFragment : Fragment() {
    private lateinit var menuValikko: MenuValikko
    private lateinit var binding : FragmentKuitinTarkasteluBinding
    // TODO: Rename and change types of parameters
    private lateinit var database: TakuukuittiDB
    private lateinit var dao: TakuukuittiDBDao
    private lateinit var kuitti : Kuitti
    private var kuvaIsona : Boolean = false
    var kuitinId = 1
    var kuitinKuvanNimi = "" //otetaan kiinni kuvan tiedostosta poistoa varten

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        database = TakuukuittiDB.getInstance(requireContext())
        dao=database.takuukuittiDBDao
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_kuitin_tarkastelu, container, false)
        kuitinId = arguments?.getInt("id")!!
        if(kuitinId != 0) {
            tiedotRiveille()
        }

        binding.btnTakaisin.setOnClickListener {
            (activity as AppCompatActivity?)!!.supportActionBar!!.show()
            it.findNavController().navigate(R.id.action_kuitinTarkasteluFragment_to_kuittiVarastoFragment)
        }

        binding.poistaBtn.setOnClickListener {
            poista()
        }
        binding.ivKuitti.setOnClickListener {
            var iv = it as ImageView
            Log.d("testi", kuvaIsona.toString())
            if(kuvaIsona) {
                kuvaIsona = false
                var pixeltodp = (400*resources.displayMetrics.density).toInt()
                iv.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, pixeltodp)
                iv.adjustViewBounds = true
                iv.scaleType = ImageView.ScaleType.FIT_CENTER
                (activity as AppCompatActivity?)!!.supportActionBar!!.show()
            } else {
                kuvaIsona = true
                iv.adjustViewBounds = true
                var pixeltodp = (1000*resources.displayMetrics.density).toInt()
                iv.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, pixeltodp)
                iv.scaleType = ImageView.ScaleType.FIT_CENTER
                (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
            }
        }

        menuValikko = MenuValikko(inflater = requireActivity().menuInflater,navController = findNavController(),R.id.action_kuitinTarkasteluFragment_to_kuittiLisaysFragment,fragment = this,activity = null)
        setHasOptionsMenu(true);

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menuValikko.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return menuValikko.onOptionsItemSelected(item)
    }

    fun tiedotRiveille() {
        GlobalScope.launch(context = Dispatchers.IO) {
            var kuittilista=dao.haeYksiKuitti(kuitinId)
            kuittilista.forEach{
                kuitti = it
                //asetetaan tekstit tekstiriveille tietokannasta

            }
            withContext(Dispatchers.Main) {
                binding.txtTuotteenNimi.setText(kuitti.tuotenimi)
                binding.txtPaivamaara.setText(DateFormat.getDateInstance().format(Date(kuitti.takuupvm)))
                var bitmap = BitmapFactory.decodeFile(kuitti.kuva)
                binding.ivKuitti.setImageBitmap(bitmap)
                kuitinKuvanNimi = kuitti.kuva
            }
        }
    }
    fun poista() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage("Oletko varma, ett?? haluat poistaa t??m??n kuitin?")
            .setNegativeButton("Takaisin",DialogInterface.OnClickListener {
                    dialog, id ->
                dialog.dismiss()
            })
            .setPositiveButton("Kyll??", DialogInterface.OnClickListener {
                    dialog, id ->
                    d("testi", "Kuvaa aloitetaan poistamaan." + kuitinKuvanNimi)
                var kuvanNimiMillisekunteina = kuitinKuvanNimi.substring(kuitinKuvanNimi.length-17)
                   d("testi", "Kuvan nimi millisekunteina, 18 kirjainta " +kuvanNimiMillisekunteina)
                    var file : File = requireContext().getFileStreamPath(kuvanNimiMillisekunteina)//kaatuu
                    d("testi", "T??t?? poistetaan: ." +kuvanNimiMillisekunteina) //"163 688 945 204 9.jpg"
                GlobalScope.launch(context = Dispatchers.Default) {
                    dao.poistaKuitti(kuitinId)
                    //kuvatiedoston poisto t??h??n:
                    if (file.exists()) {
                        d("testi", "Haettu kuvatiedosto on olemassa:" + kuitinKuvanNimi)
                        file.delete()
                        d("testi", "Kuva on nyt poistettu tiedostosta.")
                    }
                }
                d("testi", "Palataan kuittivarastolistaukseen seuraavaksi.")
                (activity as AppCompatActivity?)!!.supportActionBar!!.show()
                requireView().findNavController().navigate(R.id.action_kuitinTarkasteluFragment_to_kuittiVarastoFragment)
            })

        val alert = builder.create()
        alert.setTitle("Poista kuitti")
        alert.show()
    }

}