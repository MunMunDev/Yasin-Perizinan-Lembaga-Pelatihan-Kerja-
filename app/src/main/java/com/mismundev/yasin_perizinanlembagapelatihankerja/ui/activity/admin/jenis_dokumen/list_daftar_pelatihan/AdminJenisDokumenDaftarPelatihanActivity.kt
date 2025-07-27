package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.jenis_dokumen.list_daftar_pelatihan

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mismundev.yasin_perizinanlembagapelatihankerja.adapter.admin.AdminJenisDokumenDaftarPelatihanAdapter
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.DaftarPelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ActivityAdminJenisDokumenDaftarPelatihanBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.AlertDialogKeteranganBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.main.AdminMainActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.KontrolNavigationDrawer
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.LoadingAlertDialog
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.OnClickItem
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.SharedPreferencesLogin
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AdminJenisDokumenDaftarPelatihanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminJenisDokumenDaftarPelatihanBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    private val viewModel: AdminJenisDokumenDaftarPelatihanViewModel by viewModels()
    @Inject
    lateinit var loading: LoadingAlertDialog
    private lateinit var adapter: AdminJenisDokumenDaftarPelatihanAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminJenisDokumenDaftarPelatihanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        setSharedPreferencesLogin()
        fetchDaftarPelatihan()
        getDaftarPelatihan()
    }


    private fun setSharedPreferencesLogin() {
        sharedPreferencesLogin = SharedPreferencesLogin((this@AdminJenisDokumenDaftarPelatihanActivity))
    }

    @SuppressLint("SetTextI18n")
    private fun setKontrolNavigationDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminJenisDokumenDaftarPelatihanActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, myAppBar.ivNavDrawer, this@AdminJenisDokumenDaftarPelatihanActivity)

            myAppBar.tvTitle.text = "List DaftarPelatihan - Jenis Dokumen"
        }
    }

    private fun fetchDaftarPelatihan(){
        viewModel.fetchDaftarPelatihan()
    }

    private fun getDaftarPelatihan(){
        viewModel.getDaftarPelatihan().observe(this@AdminJenisDokumenDaftarPelatihanActivity){ result->
            when(result){
                is UIState.Loading-> setStartShimmer()
                is UIState.Failure-> setFailureFetchDaftarPelatihan(result.message)
                is UIState.Success-> setSuccessFetchDaftarPelatihan(result.data)
            }
        }
    }

    private fun setFailureFetchDaftarPelatihan(message: String) {
        setStopShimmer()
        Toast.makeText(this@AdminJenisDokumenDaftarPelatihanActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessFetchDaftarPelatihan(data: ArrayList<DaftarPelatihanModel>) {
        setStopShimmer()
        if(data.isNotEmpty()){
            setAdapterDaftarPelatihan(data)
        } else{
            Toast.makeText(this@AdminJenisDokumenDaftarPelatihanActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapterDaftarPelatihan(data: ArrayList<DaftarPelatihanModel>) {
        adapter = AdminJenisDokumenDaftarPelatihanAdapter(data, object : OnClickItem.AdminClickDaftarPelatihanJenisDokumen{

            override fun clickJenisPelatihan(keterangan: String, title: String) {
                showKeterangan(title, keterangan)
            }

            override fun clickNamaPelatihan(keterangan: String, title: String) {
                showKeterangan(title, keterangan)
            }


        })

        binding.apply {
            rvDaftarPelatihan.layoutManager = LinearLayoutManager(this@AdminJenisDokumenDaftarPelatihanActivity, LinearLayoutManager.VERTICAL, false)
            rvDaftarPelatihan.adapter = adapter
        }
    }

    private fun setStartShimmer(){
        binding.apply {
            smDaftarPelatihan.startShimmer()
            smDaftarPelatihan.visibility = View.VISIBLE
            rvDaftarPelatihan.visibility = View.GONE
        }
    }

    private fun setStopShimmer(){
        binding.apply {
            smDaftarPelatihan.stopShimmer()
            smDaftarPelatihan.visibility = View.GONE
            rvDaftarPelatihan.visibility = View.VISIBLE
        }
    }


    private fun showKeterangan(title:String, keterangan: String) {
        val view = AlertDialogKeteranganBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminJenisDokumenDaftarPelatihanActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKeterangan.text = title
            tvBodyKeterangan.text = keterangan

            btnClose.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@AdminJenisDokumenDaftarPelatihanActivity, AdminMainActivity::class.java))
        finish()
    }
}