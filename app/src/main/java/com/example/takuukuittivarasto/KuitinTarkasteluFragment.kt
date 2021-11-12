package com.example.takuukuittivarasto

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.takuukuittivarasto.databinding.FragmentKuitinTarkasteluBinding;
import kotlinx.android.synthetic.main.fragment_kuitin_tarkastelu.*
import kotlinx.coroutines.*
import java.util.Date


/**
 * A simple [Fragment] subclass.
 * Use the [KuitinTarkasteluFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class KuitinTarkasteluFragment : Fragment() {
    private lateinit var binding : FragmentKuitinTarkasteluBinding
    // TODO: Rename and change types of parameters
    private lateinit var database: TakuukuittiDB
    private lateinit var dao: TakuukuittiDBDao
    private lateinit var kuitti : Kuitti
    var kuitinId = 1
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
            it.findNavController().navigate(R.id.action_kuitinTarkasteluFragment_to_kuittiVarastoFragment)
        }

        binding.poistaBtn.setOnClickListener {
            poista()
        }

        return binding.root
    }

    fun tiedotRiveille() {
        Log.d("testi","tiedotRiveille()")
        Log.d("testi", Date().time.toString())
        GlobalScope.launch(context = Dispatchers.Default) {
            var kuittilista=dao.haeYksiKuitti(kuitinId)
            kuittilista.forEach{
                kuitti = it
                Log.d("testi", "Tietokannan sisältö: "+it.id.toString() + " " + it.tuotenimi + " " + it.takuupvm)
                //asetetaan tekstit tekstiriveille tietokannasta
                txtTuotteenNimi.setText(it.tuotenimi)
                txtPaivamaara.setText(it.takuupvm.toString())

            }
        }

        Log.d("testi", Date().time.toString())


    }
    fun paivitaKayttoliittyma(tmpKuitti: Kuitti) {
        Log.d("asd", "asd")
        binding.txtTuotteenNimi.setText(tmpKuitti.tuotenimi)
        binding.txtPaivamaara.setText(tmpKuitti.takuupvm.toString())
    }
    fun poista() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage("Oletko varma, että haluat poistaa tämän kuitin?")
            .setNegativeButton("Takaisin",DialogInterface.OnClickListener {
                    dialog, id ->
                dialog.dismiss()
            })
            .setPositiveButton("Kyllä", DialogInterface.OnClickListener {
                    dialog, id ->
                GlobalScope.launch(context = Dispatchers.Default) {
                    dao.poistaKuitti(kuitinId)
                    //tiedoston poisto tähän
                }
                requireView().findNavController().navigate(R.id.action_kuitinTarkasteluFragment_to_kuittiVarastoFragment)
            })

        val alert = builder.create()
        alert.setTitle("Poista kuitti")
        alert.show()
    }

}