package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.log_pembayaran

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mismundev.yasin_perizinanlembagapelatihankerja.adapter.admin.AdminLogPembayaranAdapter
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.PembayaranModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ActivityAdminLogPembayaranBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.main.AdminMainActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.KontrolNavigationDrawer
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.LoadingAlertDialog
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.SharedPreferencesLogin
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList
import javax.inject.Inject

@AndroidEntryPoint
class AdminLogPembayaranActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminLogPembayaranBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    private val viewModel: AdminLogPembayaranViewModel by viewModels()
    @Inject
    lateinit var loading: LoadingAlertDialog
    private lateinit var adapter: AdminLogPembayaranAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminLogPembayaranBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        setSharedPreferencesLogin()
        fetchLogPembayaran()
        getLogPembayaran()
    }

    private fun setSharedPreferencesLogin() {
        sharedPreferencesLogin = SharedPreferencesLogin((this@AdminLogPembayaranActivity))
    }

    @SuppressLint("SetTextI18n")
    private fun setKontrolNavigationDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminLogPembayaranActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, myAppBar.ivNavDrawer, this@AdminLogPembayaranActivity)

            myAppBar.tvTitle.text = "Log Pembayaran"
        }
    }

    private fun fetchLogPembayaran() {
        viewModel.fetchPembayaran()
    }
    private fun getLogPembayaran(){
        viewModel.getPembayaran().observe(this@AdminLogPembayaranActivity){ result->
            when(result){
                is UIState.Loading-> setStartShimmer()
                is UIState.Failure-> setFailureFetchLogPembayaran(result.message)
                is UIState.Success-> setSuccessFetchLogPembayaran(result.data)
            }
        }
    }

    private fun setFailureFetchLogPembayaran(message: String) {
        setStopShimmer()
        Toast.makeText(this@AdminLogPembayaranActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessFetchLogPembayaran(data: ArrayList<PembayaranModel>) {
        setStopShimmer()
        if(data.isNotEmpty()){
            setAdapterLogPembayaran(data)
        } else{
            Toast.makeText(this@AdminLogPembayaranActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapterLogPembayaran(data: ArrayList<PembayaranModel>) {
        adapter = AdminLogPembayaranAdapter(data)

        binding.apply {
            rvLogPembayaran.layoutManager = LinearLayoutManager(this@AdminLogPembayaranActivity, LinearLayoutManager.VERTICAL, false)
            rvLogPembayaran.adapter = adapter
        }
    }

    private fun setStartShimmer(){
        binding.apply {
            smLogPembayaran.startShimmer()
            smLogPembayaran.visibility = View.VISIBLE
            hsLogPembayaran.visibility = View.GONE
        }
    }

    private fun setStopShimmer(){
        binding.apply {
            smLogPembayaran.stopShimmer()
            smLogPembayaran.visibility = View.GONE
            hsLogPembayaran.visibility = View.VISIBLE
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@AdminLogPembayaranActivity, AdminMainActivity::class.java))
        finish()
    }
}