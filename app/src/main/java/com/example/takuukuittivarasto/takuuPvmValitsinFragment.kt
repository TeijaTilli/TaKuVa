package com.example.takuukuittivarasto

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.takuukuittivarasto.databinding.FragmentTakuuPvmValitsinBinding
import java.text.SimpleDateFormat
import java.util.*


class takuuPvmValitsinFragment : Fragment() {
    private lateinit var binding: FragmentTakuuPvmValitsinBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_takuu_pvm_valitsin, container, false)
        if(KuittiLisaysFragment.takuuPvm != 0L) {
            binding.pvmTakuu.setDate(KuittiLisaysFragment.takuuPvm)
        }
        binding.btVuosiTaaksepain.setOnClickListener {
            var pvm = binding.pvmTakuu.date-31557600000
            binding.pvmTakuu.date = pvm;
        }
        binding.btVuosiEteenpain.setOnClickListener {
            var pvm = binding.pvmTakuu.date+31557600000
            binding.pvmTakuu.date = pvm;
        }
        binding.pvmTakuu.setOnDateChangeListener( CalendarView.OnDateChangeListener{ kalenteri, vuosi, kuukausi, paiva ->
            Log.d("testi", "${binding.pvmTakuu.date}, $vuosi, $kuukausi, $paiva")
            var kuukausi_t = kuukausi + 1 //kuukaudet alkaa nollasta joten lisätään 1
            var uusi_date_format = SimpleDateFormat("yyyy-MM-dd") // tehdään jotta voidaan muokata stringi dateksi
            var uusi_date = uusi_date_format.parse("$vuosi-$kuukausi_t-$paiva") // muunnetaan stringi dateksi
            binding.pvmTakuu.setDate(uusi_date.time) // laitetaan kalenterin ajaksi tämä, muuten ei päivitä aikaa jostain syystä :)

        })
        binding.btTallenna.setOnClickListener {
            val bundle = bundleOf(
                Pair("date", binding.pvmTakuu.date)
            )
            it.findNavController().navigate(R.id.action_takuuPvmValitsinFragment_to_kuittiLisaysFragment, bundle)
        }
        return binding.root
    }

}