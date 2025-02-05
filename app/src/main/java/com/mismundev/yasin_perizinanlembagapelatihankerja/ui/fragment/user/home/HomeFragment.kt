package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.fragment.user.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mismundev.yasin_perizinanlembagapelatihankerja.adapter.user.PelatihanAdapter
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.DaftarPelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.FragmentHomeBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.user.main.MainActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.user.pelatihan.detail_pelatihan.DetailPelatihanActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.user.pelatihan.search_pelatihan.SearchPelatihanActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.OnClickItem
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.SharedPreferencesLogin
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferencesLogin
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        context = requireContext().applicationContext
        setButton()
        setSharedPreferences()
        fetchPelatihanTerdaftar(sharedPreferences.getIdUser())
        getPelatihanTerdaftar()
        fetchPelatihan()
        getPelatihan()
        setSwipeRefreshLayout()

        return binding.root
    }

    private fun setSwipeRefreshLayout() {
        binding.swipeRefresh.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeRefresh.isRefreshing = false
                fetchPelatihanTerdaftar(sharedPreferences.getIdUser())
                fetchPelatihan()
            }, 2000)
        }
    }

    private fun setSharedPreferences() {
        sharedPreferences = SharedPreferencesLogin(context)
    }

    private fun setButton(){
        binding.apply {
            topAppBar.apply {
                llSearchPelatihan.setOnClickListener {
                    val i = Intent(context, SearchPelatihanActivity::class.java)
                    i.putExtra("id_check", 0)
                    startActivity(i)
                }
            }
            tvLihatPelatihan.setOnClickListener {
                (activity as MainActivity).clickPelatihan()
            }
        }
    }

    private fun fetchPelatihanTerdaftar(idUser: Int){
        viewModel.fetchPelatihanTerdaftar(idUser)
    }

    private fun getPelatihanTerdaftar(){
        viewModel.getPelatihanTerdaftar().observe(viewLifecycleOwner){result->
            when(result){
                is UIState.Loading-> setStartShimmerPelatihanTerdaftar()
                is UIState.Success-> setSuccessFetchPelatihanTerdaftar(result.data)
                is UIState.Failure-> setFailureFetchPelatihanTerdaftar(result.message)
            }
        }
    }

    private fun setFailureFetchPelatihanTerdaftar(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        setStopShimmerPelatihanTerdaftar()
    }

    private fun setSuccessFetchPelatihanTerdaftar(data: ArrayList<DaftarPelatihanModel>) {
        if(data.isNotEmpty()){
            setAdapterPelatihanTerdaftar(data)
        } else{
            Toast.makeText(context, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
        setStopShimmerPelatihanTerdaftar()
    }

    private fun setAdapterPelatihanTerdaftar(data: ArrayList<DaftarPelatihanModel>) {
        val adapter = PelatihanAdapter(data, object : OnClickItem.ClickPelatihan{
            override fun clickPelatihan(idDaftarPelatihan: Int, namaPelatihan: String) {
                val i = Intent(context, DetailPelatihanActivity::class.java)
                i.putExtra("id_daftar_pelatihan", idDaftarPelatihan)
                i.putExtra("nama_pelatihan", namaPelatihan)
                startActivity(i)
            }
        }, false)

        binding.rvPelatihanTerdaftar.let {
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            it.adapter = adapter
        }
    }

    private fun fetchPelatihan(){
        viewModel.fetchPelatihan()
    }

    private fun getPelatihan(){
        viewModel.getPelatihan().observe(viewLifecycleOwner){result->
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
        }, true)

        binding.rvPelatihan.let {
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            it.adapter = adapter
        }
    }

    private fun setStartShimmerPelatihanTerdaftar(){
        binding.apply {
            smPelatihanTerdaftar.startShimmer()
            rvPelatihanTerdaftar.visibility = View.GONE
            smPelatihanTerdaftar.visibility = View.VISIBLE
        }
    }

    private fun setStopShimmerPelatihanTerdaftar(){
        binding.apply {
            smPelatihanTerdaftar.stopShimmer()
            rvPelatihanTerdaftar.visibility = View.VISIBLE
            smPelatihanTerdaftar.visibility = View.GONE
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