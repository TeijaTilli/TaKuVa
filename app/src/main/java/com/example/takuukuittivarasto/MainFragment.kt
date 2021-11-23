package com.example.takuukuittivarasto

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.databinding.*;
import androidx.databinding.DataBindingUtil.setContentView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.takuukuittivarasto.databinding.FragmentKuitinTarkasteluBinding.inflate
import com.example.takuukuittivarasto.databinding.FragmentMainBinding
import kotlinx.android.synthetic.main.fragment_kuitti_lisays.*
import java.util.zip.Inflater

class MainFragment : Fragment() {
    private lateinit var menuValikko: MenuValikko
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

        //yö-teeman lisäys näin
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        menuValikko = MenuValikko(inflater = requireActivity().menuInflater,navController = findNavController(),R.id.action_mainFragment_to_kuittiLisaysFragment,fragment = this,activity = null)
        setHasOptionsMenu(true);
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menuValikko.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return menuValikko.onOptionsItemSelected(item)
    }

}