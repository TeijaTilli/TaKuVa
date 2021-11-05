package com.example.takuukuittivarasto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.*;
import androidx.navigation.findNavController
import com.example.takuukuittivarasto.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private lateinit var binding : FragmentMainBinding;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_main, container, false);
        binding.lisaaTakuukuittiBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_mainFragment_to_kuittiLisaysFragment)
        }
        binding.katsoKuittejaBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_mainFragment_to_kuittiVarastoFragment)
        }
        return binding.root
    }
}