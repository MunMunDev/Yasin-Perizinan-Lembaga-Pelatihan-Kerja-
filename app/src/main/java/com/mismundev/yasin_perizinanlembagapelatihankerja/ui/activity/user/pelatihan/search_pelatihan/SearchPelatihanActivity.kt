package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.user.pelatihan.search_pelatihan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mismundev.yasin_perizinanlembagapelatihankerja.adapter.user.PelatihanAdapter
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.DaftarPelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ActivitySearchPelatihanBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.user.pelatihan.detail_pelatihan.DetailPelatihanActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.OnClickItem
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class SearchPelatihanActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchPelatihanBinding
    private val viewModel: SearchPelatihanViewModel by viewModels()
    private lateinit var adapter: PelatihanAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchPelatihanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButton()
        fetchPelatihan()
        getPelatihan()
        setSearchData()
    }

    private fun setButton() {
        binding.topAppBar.btnBack.setOnClickListener{
            finish()
        }
    }

    private fun setSearchData() {
        binding.topAppBar.srcData.apply {
            requestFocus()
            onActionViewExpanded()
            queryHint = ""

            setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    Log.d("MainActivityTAG", "onQueryTextSubmit:1: $query")
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    Log.d("MainActivityTAG", "onQueryTextChange:2: $newText ")
                    adapter.searchData(newText!!)
                    return true
                }

            })
        }

        // Search Data
//        binding.srcData.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                Log.d("MainActivityTAG", "onQueryTextSubmit:1: $query")
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                Log.d("MainActivityTAG", "onQueryTextChange:2: $newText ")
//                adapter.searchData(newText!!)
//                return true
//            }
//
//        })
    }

    private fun fetchPelatihan(){
        viewModel.fetchPelatihan()
    }

    private fun getPelatihan(){
        viewModel.getPelatihan().observe(this@SearchPelatihanActivity){result->
            when(result){
                is UIState.Loading-> setStartShimmerPelatihan()
                is UIState.Success-> setSuccessFetchPelatihan(result.data)
                is UIState.Failure-> setFailureFetchPelatihan(result.message)
            }
        }
    }

    private fun setFailureFetchPelatihan(message: String) {
        Toast.makeText(this@SearchPelatihanActivity, message, Toast.LENGTH_SHORT).show()
        setStopShimmerPelatihan()
    }

    private fun setSuccessFetchPelatihan(data: ArrayList<DaftarPelatihanModel>) {
        if(data.isNotEmpty()){
            setAdapterPelatihan(data)
        } else{
            Toast.makeText(this@SearchPelatihanActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
        setStopShimmerPelatihan()
    }

    private fun setAdapterPelatihan(data: ArrayList<DaftarPelatihanModel>) {
        adapter = PelatihanAdapter(data, object : OnClickItem.ClickPelatihan{
            override fun clickPelatihan(idDaftarPelatihan: Int) {
                val i = Intent(this@SearchPelatihanActivity, DetailPelatihanActivity::class.java)
                i.putExtra("id_daftar_pelatihan", idDaftarPelatihan)
                startActivity(i)
            }
        }, false)

        binding.rvPelatihan.let {
            it.layoutManager = LinearLayoutManager(this@SearchPelatihanActivity, LinearLayoutManager.VERTICAL, false)
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