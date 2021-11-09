package com.example.takuukuittivarasto

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.takuukuittivarasto.databinding.FragmentKuittiVarastoBinding
import kotlinx.android.synthetic.main.activity_kuittivarasto.*
import java.lang.Exception
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [KuittiVarastoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class KuittiVarastoFragment : Fragment() {
    private lateinit var binding : FragmentKuittiVarastoBinding
    private lateinit var tietokanta: TakuukuittiDB
    private lateinit var dao: TakuukuittiDBDao
    companion object{
        var kuitit: List<Kuitti> = mutableListOf<Kuitti>()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Tietokannan lisääminen
        tietokanta = TakuukuittiDB.getInstance(requireContext())
        dao = tietokanta.takuukuittiDBDao

        GlobalScope.launch(context = Dispatchers.Default) {
            kuitit = dao.haeKuitit()
        }

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_kuitti_varasto, container, false)
        binding.kuittiRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = KuittiAdapteri()
        }
        return binding.root
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
        Log.d("JKR", "Kuitti")
        holder.bind(kuitti)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val button: Button = itemView.findViewById(R.id.btKuitti)
        fun bind(item: Kuitti){
            button.setText(item.tuotenimi)
        }
    }
}