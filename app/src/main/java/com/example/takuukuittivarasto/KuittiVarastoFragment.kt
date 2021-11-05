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
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [KuittiVarastoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class KuittiVarastoFragment : Fragment() {
    private lateinit var binding : FragmentKuittiVarastoBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_kuitti_varasto, container, false)
        kuittivarasto.kuitit = listOf(Takuukuitti(1, "kuitti", Date(2022, 12, 2), null),
            Takuukuitti(2, "kuitti2", Date(2033, 11, 4), null)) as MutableList<Takuukuitti>
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

    override fun getItemCount() = kuittivarasto.kuitit.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val kuitit = kuittivarasto.kuitit
        val num = kuitit.size
        val kuitti = kuitit[position%num]
        Log.d("JKR", "Kuitti")
        holder.bind(kuitti)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val button: Button = itemView.findViewById(R.id.btKuitti)
        fun bind(item: Takuukuitti){
            button.setText(item.otsikko)
        }
    }
}