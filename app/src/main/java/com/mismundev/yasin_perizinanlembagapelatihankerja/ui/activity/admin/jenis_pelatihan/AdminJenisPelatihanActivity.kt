package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.jenis_pelatihan

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mismundev.yasin_perizinanlembagapelatihankerja.R
import com.mismundev.yasin_perizinanlembagapelatihankerja.adapter.admin.AdminJenisPelatihanAdapter
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.JenisPelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.ResponseModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ActivityAdminJenisPelatihanBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.AlertDialogJenisPelatihanBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.AlertDialogKeteranganBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.main.AdminMainActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.KontrolNavigationDrawer
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.LoadingAlertDialog
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.OnClickItem
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.SharedPreferencesLogin
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList
import javax.inject.Inject

@AndroidEntryPoint
class AdminJenisPelatihanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminJenisPelatihanBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    private val viewModel: AdminJenisPelatihanViewModel by viewModels()
    @Inject
    lateinit var loading: LoadingAlertDialog
    private lateinit var adapter: AdminJenisPelatihanAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminJenisPelatihanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        setButton()
        setSharedPreferencesLogin()
        fetchJenisPelatihan()
        getJenisPelatihan()
        getPostTambahData()
        getPostUpdateData()
    }

    private fun setSharedPreferencesLogin() {
        sharedPreferencesLogin = SharedPreferencesLogin((this@AdminJenisPelatihanActivity))
    }

    @SuppressLint("SetTextI18n")
    private fun setKontrolNavigationDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminJenisPelatihanActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, myAppBar.ivNavDrawer, this@AdminJenisPelatihanActivity)

            myAppBar.tvTitle.text = "Jenis Pelatiihan"
        }
    }

    private fun setButton() {
        binding.apply {
            btnTambah.setOnClickListener {
                setShowDialogTambah()
            }
        }
    }

    private fun setShowDialogTambah() {
        val view = AlertDialogJenisPelatihanBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminJenisPelatihanActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tVTitle.text = "Tambah Jenis Pelatihan"
            btnSimpan.setOnClickListener {
                var cek = false
                if (etEditJenisPelatihan.toString().isEmpty()) {
                    etEditJenisPelatihan.error = "Tidak Boleh Kosong"
                    cek = true
                }

                if (!cek) {
                    postTambahDataJenisPelatihan(
                        etEditJenisPelatihan.text.toString()
                    )

                    dialogInputan.dismiss()
                }
            }

            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postTambahDataJenisPelatihan(jenisPelatihan: String){
        viewModel.postTambahJenisPelatihan(
            jenisPelatihan
        )
    }

    private fun getPostTambahData(){
        viewModel.getTambahJenisPelatihan().observe(this@AdminJenisPelatihanActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminJenisPelatihanActivity)
                is UIState.Success-> setSuccessTambahData(result.data)
                is UIState.Failure-> setFailureTambahData(result.message)
            }
        }
    }

    private fun setFailureTambahData(message: String) {
        Toast.makeText(this@AdminJenisPelatihanActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessTambahData(data: ResponseModel?) {
        if(data != null){
            if(data.status == "0"){
                fetchJenisPelatihan()
            } else{
                Toast.makeText(this@AdminJenisPelatihanActivity, data.message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminJenisPelatihanActivity, "Gagal: Ada kesalahan di API", Toast.LENGTH_SHORT).show()
        }
        loading.alertDialogCancel()
    }

    private fun fetchJenisPelatihan() {
        viewModel.fetchJenisPelatihan()
    }
    private fun getJenisPelatihan(){
        viewModel.getJenisPelatihan().observe(this@AdminJenisPelatihanActivity){result->
            when(result){
                is UIState.Loading-> setStartShimmer()
                is UIState.Failure-> setFailureFetchJenisPelatihan(result.message)
                is UIState.Success-> setSuccessFetchJenisPelatihan(result.data)
            }
        }
    }

    private fun setFailureFetchJenisPelatihan(message: String) {
        setStopShimmer()
        Toast.makeText(this@AdminJenisPelatihanActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessFetchJenisPelatihan(data: ArrayList<JenisPelatihanModel>) {
        setStopShimmer()
        if(data.isNotEmpty()){
            setAdapterJenisPelatihan(data)
        } else{
            Toast.makeText(this@AdminJenisPelatihanActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapterJenisPelatihan(data: ArrayList<JenisPelatihanModel>) {
        adapter = AdminJenisPelatihanAdapter(data, object : OnClickItem.AdminClickJenisPelatihan{
            override fun clickJenisPelatihan(
                jenisPelatihan: String,
                title: String
            ) {
                showKeterangan(title, jenisPelatihan)
            }

            override fun clickItemSetting(jenisPelatihan: JenisPelatihanModel, it: View) {
                val popupMenu = PopupMenu(this@AdminJenisPelatihanActivity, it)
                popupMenu.inflate(R.menu.popup_edit)
                popupMenu.setOnMenuItemClickListener(object :
                    PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                        when (menuItem!!.itemId) {
                            R.id.edit -> {
                                setShowDialogEdit(jenisPelatihan)
                                return true
                            }
                        }
                        return true
                    }

                })
                popupMenu.show()
            }

        })

        binding.apply {
            rvJenisPelatihan.layoutManager = LinearLayoutManager(this@AdminJenisPelatihanActivity, LinearLayoutManager.VERTICAL, false)
            rvJenisPelatihan.adapter = adapter
        }
    }

    private fun showKeterangan(title:String, keterangan: String) {
        val view = AlertDialogKeteranganBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminJenisPelatihanActivity)
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

    private fun setShowDialogEdit(jenisPelatihan: JenisPelatihanModel) {
        val view = AlertDialogJenisPelatihanBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminJenisPelatihanActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {

            etEditJenisPelatihan.setText(jenisPelatihan.jenisPelatihan)

            btnSimpan.setOnClickListener {
                var cek = false
                if (etEditJenisPelatihan.toString().isEmpty()) {
                    etEditJenisPelatihan.error = "Tidak Boleh Kosong"
                    cek = true
                }

                if (!cek) {
                    postUpdateDataJenisPelatihan(
                        jenisPelatihan.idJenisPelatihan!!,
                        etEditJenisPelatihan.text.toString(),
                    )

                    dialogInputan.dismiss()
                }
            }

            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postUpdateDataJenisPelatihan(idJenisPelatihan: Int, jenisPelatihan: String){
        viewModel.postUpdateJenisPelatihan(
            idJenisPelatihan, jenisPelatihan
        )
    }

    private fun getPostUpdateData(){
        viewModel.getUpdateJenisPelatihan().observe(this@AdminJenisPelatihanActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminJenisPelatihanActivity)
                is UIState.Success-> setSuccessUpdateData(result.data)
                is UIState.Failure-> setFailureUpdateData(result.message)
            }
        }
    }

    private fun setFailureUpdateData(message: String) {
        Toast.makeText(this@AdminJenisPelatihanActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessUpdateData(data: ResponseModel?) {
        if(data != null){
            if(data.status == "0"){
                fetchJenisPelatihan()
            } else{
                Toast.makeText(this@AdminJenisPelatihanActivity, data.message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminJenisPelatihanActivity, "Gagal: Ada kesalahan di API", Toast.LENGTH_SHORT).show()
        }
        loading.alertDialogCancel()
    }


    private fun setStartShimmer(){
        binding.apply {
            smJenisPelatihan.startShimmer()
            smJenisPelatihan.visibility = View.VISIBLE
            rvJenisPelatihan.visibility = View.GONE
        }
    }

    private fun setStopShimmer(){
        binding.apply {
            smJenisPelatihan.stopShimmer()
            smJenisPelatihan.visibility = View.GONE
            rvJenisPelatihan.visibility = View.VISIBLE
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@AdminJenisPelatihanActivity, AdminMainActivity::class.java))
        finish()
    }
}