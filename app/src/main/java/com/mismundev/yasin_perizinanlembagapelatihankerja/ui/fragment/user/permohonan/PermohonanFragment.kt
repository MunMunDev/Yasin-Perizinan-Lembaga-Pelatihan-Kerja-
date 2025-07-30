package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.fragment.user.permohonan

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mismundev.yasin_perizinanlembagapelatihankerja.R
import com.mismundev.yasin_perizinanlembagapelatihankerja.adapter.user.PermohonanAdapter
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.PermohonanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.FragmentPermohonanBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.pdf.PdfActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.user.permohonan.DetailPermohonanActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.OnClickItem
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.SharedPreferencesLogin
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class PermohonanFragment : Fragment() {
    private lateinit var binding: FragmentPermohonanBinding
    private val viewModel: PermohonanViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferencesLogin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSharedPreferences()
        fetchPermohonan(sharedPreferences.getIdUser())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPermohonanBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSwipeRefreshLayout()
        getPermohonan()
    }

    private fun setSharedPreferences() {
        sharedPreferences = SharedPreferencesLogin(requireContext())
    }

    private fun setSwipeRefreshLayout() {
        binding.swipeRefresh.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeRefresh.isRefreshing = false
                fetchPermohonan(sharedPreferences.getIdUser())
            }, 2000)
        }
    }

    private fun fetchPermohonan(idUser: Int){
        viewModel.fetchPermohonan(idUser)
    }

    private fun getPermohonan() {
        viewModel.getPermohonan().observe(this.viewLifecycleOwner){result->
            when(result){
                is UIState.Loading-> setStartShimmerPermohonan()
                is UIState.Success-> setSuccessFetchPermohonan(result.data)
                is UIState.Failure-> setFailureFetchPermohonan(result.message)
            }
        }
    }

    private fun setFailureFetchPermohonan(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        setStopShimmerPermohonan()
    }

    private fun setSuccessFetchPermohonan(data: ArrayList<PermohonanModel>) {
        if(data.isNotEmpty()){
            setAdapterPermohonan(data)
        } else{
            Toast.makeText(context, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
        setStopShimmerPermohonan()
    }

    private fun setAdapterPermohonan(data: ArrayList<PermohonanModel>) {
        val adapter = PermohonanAdapter(data, object : OnClickItem.ClickPermohonan{
            override fun clickDownload(file: String) {
                val i = Intent(context, PdfActivity::class.java)
                i.putExtra("file", file)
                startActivity(i)
            }

            override fun clickFile(permohonan: PermohonanModel) {
                val i = Intent(context, DetailPermohonanActivity::class.java)
                i.putExtra("id_user", sharedPreferences.getIdUser())
                i.putExtra("id_permohonan", permohonan.id_permohonan)
                i.putExtra("ket", permohonan.ket)
                startActivity(i)
            }
        })

        binding.rvPermohonan.let {
            it.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            it.adapter = adapter
        }
    }

    private fun setStartShimmerPermohonan(){
        binding.apply {
            smPermohonan.startShimmer()
            rvPermohonan.visibility = View.GONE
            smPermohonan.visibility = View.VISIBLE
        }
    }

    private fun setStopShimmerPermohonan(){
        binding.apply {
            smPermohonan.stopShimmer()
            rvPermohonan.visibility = View.VISIBLE
            smPermohonan.visibility = View.GONE
        }
    }
}