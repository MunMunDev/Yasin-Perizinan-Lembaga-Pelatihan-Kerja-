package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.fragment.user.history

import android.annotation.SuppressLint
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
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.FragmentRiwayatBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.user.pelatihan.detail_pelatihan.DetailPelatihanActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.user.pelatihan.search_pelatihan.SearchPelatihanActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.OnClickItem
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.SharedPreferencesLogin
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class RiwayatFragment : Fragment() {
    private lateinit var binding : FragmentRiwayatBinding
    private val viewModel: RiwayatViewModel by viewModels()
    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferencesLogin
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRiwayatBinding.inflate(layoutInflater)
        context = this.requireContext().applicationContext

        setSharedPreferences()
        setTopAppBar()
        fetchRiwayat()
        getRiwayat()
        setSwipeRefreshLayout()

        return binding.root
    }

    private fun setSharedPreferences() {
        sharedPreferences = SharedPreferencesLogin(context)
    }

    private fun setSwipeRefreshLayout() {
        binding.swipeRefresh.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeRefresh.isRefreshing = false
                fetchRiwayat()
            }, 2000)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setTopAppBar() {
        binding.topAppBar.apply {
            tvTitle.text = "Cari Riwayat Pelatihan"
            llSearchPelatihan.setOnClickListener {
                val i = Intent(context, SearchPelatihanActivity::class.java)
                i.putExtra("id_check", 1)
                startActivity(i)
            }
        }
    }

    private fun fetchRiwayat(){
        viewModel.fetchRiwayat(sharedPreferences.getIdUser())
    }

    private fun getRiwayat() {
        viewModel.getRiwayat().observe(this.viewLifecycleOwner){result->
            when(result){
                is UIState.Loading-> setStartShimmerRiwayat()
                is UIState.Success-> setSuccessFetchRiwayat(result.data)
                is UIState.Failure-> setFailureFetchRiwayat(result.message)
            }
        }
    }

    private fun setFailureFetchRiwayat(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        setStopShimmerRiwayat()
    }

    private fun setSuccessFetchRiwayat(data: ArrayList<DaftarPelatihanModel>) {
        if(data.isNotEmpty()){
            setAdapterRiwayat(data)
        } else{
            Toast.makeText(context, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
        setStopShimmerRiwayat()
    }

    private fun setAdapterRiwayat(data: ArrayList<DaftarPelatihanModel>) {
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

    private fun setStartShimmerRiwayat(){
        binding.apply {
            smPelatihan.startShimmer()
            rvPelatihan.visibility = View.GONE
            smPelatihan.visibility = View.VISIBLE
        }
    }

    private fun setStopShimmerRiwayat(){
        binding.apply {
            smPelatihan.stopShimmer()
            rvPelatihan.visibility = View.VISIBLE
            smPelatihan.visibility = View.GONE
        }
    }
}