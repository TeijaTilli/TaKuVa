package com.example.takuukuittivarasto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.takuukuittivarasto.databinding.FragmentKuitinTarkasteluBinding;


/**
 * A simple [Fragment] subclass.
 * Use the [KuitinTarkasteluFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class KuitinTarkasteluFragment : Fragment() {
    private lateinit var binding : FragmentKuitinTarkasteluBinding
    // TODO: Rename and change types of parameters


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_kuitin_tarkastelu, container, false)
        return binding.root
    }
}