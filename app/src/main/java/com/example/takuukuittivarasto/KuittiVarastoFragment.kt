package com.example.takuukuittivarasto

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.takuukuittivarasto.databinding.FragmentKuittiVarastoBinding
import kotlinx.android.synthetic.main.kuittirivi.*
import kotlinx.coroutines.*
import java.lang.Exception
import java.text.DateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [KuittiVarastoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class KuittiVarastoFragment : Fragment() {
    private lateinit var binding : FragmentKuittiVarastoBinding
    private lateinit var tietokanta: TakuukuittiDB
    private lateinit var dao: TakuukuittiDBDao
    private lateinit var menuValikko: MenuValikko
    companion object {
        var kuitit: List<Kuitti> = mutableListOf<Kuitti>()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Tietokannan lisääminen
        tietokanta = TakuukuittiDB.getInstance(requireContext())
        dao = tietokanta.takuukuittiDBDao
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_kuitti_varasto, container, false)
        binding.kuittiRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = KuittiAdapteri()
            Log.d("testi", "<-recyclerview.apply")

            binding.btTakaisinVarastosta.setOnClickListener {
                it.findNavController().navigate(R.id.action_kuittiVarastoFragment_to_mainFragment)
            }
        }
        GlobalScope.launch(context = Dispatchers.IO) {
            kuitit = dao.haeKuitit()
            withContext(Dispatchers.Main) {
                binding.kuittiRecyclerView.adapter?.notifyDataSetChanged()
            }
        }

        menuValikko = MenuValikko(inflater = requireActivity().menuInflater,navController = findNavController(),R.id.action_kuittiVarastoFragment_to_kuittiLisaysFragment,fragment = this,activity = null)
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
class KuittiAdapteri: RecyclerView.Adapter<KuittiAdapteri.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.kuittirivi, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = KuittiVarastoFragment.kuitit.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val kuitteja = KuittiVarastoFragment.kuitit
        val num = kuitteja.size
        val kuitti = kuitteja[position%num]

        holder.bind(kuitti)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val button: Button = itemView.findViewById(R.id.btKuitti)
        fun bind(item: Kuitti){
            button.setText(item.tuotenimi + " " + DateFormat.getDateInstance().format(Date(item.takuupvm)))
            button.setOnClickListener {
                val bundle = bundleOf(
                    Pair("id", item.id)
                )
                it.findNavController().navigate(R.id.action_kuittiVarastoFragment_to_kuitinTarkasteluFragment, bundle)
            }
           if (item.takuupvm < Calendar.getInstance().timeInMillis){
                button.setBackgroundColor(Color.parseColor("teal_700"))
            }
        }
    }
}