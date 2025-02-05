package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.fragment.user.pelatihan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mismundev.yasin_perizinanlembagapelatihankerja.adapter.user.PelatihanAdapter
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.DaftarPelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.FragmentPelatihanBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.user.pelatihan.detail_pelatihan.DetailPelatihanActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.user.pelatihan.search_pelatihan.SearchPelatihanActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.OnClickItem
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class PelatihanFragment : Fragment() {
    private lateinit var binding : FragmentPelatihanBinding
    private val viewModel: PelatihanViewModel by viewModels()
    private lateinit var context: Context
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPelatihanBinding.inflate(layoutInflater)
        context = this.requireContext().applicationContext

        setTopAppBar()
        fetchPelatihan()
        getPelatihan()

        return binding.root
    }

    private fun setTopAppBar() {
        binding.topAppBar.apply {
            llSearchPelatihan.setOnClickListener {
                startActivity(Intent(context, SearchPelatihanActivity::class.java))
            }
        }
    }

    private fun fetchPelatihan(){
        viewModel.fetchPelatihan()
    }

    private fun getPelatihan() {
        viewModel.getPelatihan().observe(this.viewLifecycleOwner){result->
            when(result){
                is UIState.Loading-> setStartShimmerPelatihan()
                is UIState.Success-> setSuccessFetchPelatihan(result.data)
                is UIState.Failure-> setFailureFetchPelatihan(result.message)
            }
        }
    }

    private fun setFailureFetchPelatihan(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        setStopShimmerPelatihan()
    }

    private fun setSuccessFetchPelatihan(data: ArrayList<DaftarPelatihanModel>) {
        if(data.isNotEmpty()){
            setAdapterPelatihan(data)
        } else{
            Toast.makeText(context, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
        setStopShimmerPelatihan()
    }

    private fun setAdapterPelatihan(data: ArrayList<DaftarPelatihanModel>) {
        val adapter = PelatihanAdapter(data, object : OnClickItem.ClickPelatihan{
            override fun clickPelatihan(idDaftarPelatihan: Int, namaPelatihan: String) {
                val i = Intent(context, DetailPelatihanActivity::class.java)
                i.putExtra("id_daftar_pelatihan", idDaftarPelatihan)
                i.putExtra("nama_pelatihan", namaPelatihan)
                startActivity(i)
            }
        }, false)

        binding.rvPelatihan.let {
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            it.adapter = adapter
        }
    }

    private fun setStartShimmerPelatihan(){
        binding.apply {
            smPelatihan.startShimmer()
            rvPelatihan.visibility = View.GONE
            smPelatihan.visibility = View.VISIBLE
        }
    }

    private fun setStopShimmerPelatihan(){
        binding.apply {
            smPelatihan.stopShimmer()
            rvPelatihan.visibility = View.VISIBLE
            smPelatihan.visibility = View.GONE
        }
    }
}
