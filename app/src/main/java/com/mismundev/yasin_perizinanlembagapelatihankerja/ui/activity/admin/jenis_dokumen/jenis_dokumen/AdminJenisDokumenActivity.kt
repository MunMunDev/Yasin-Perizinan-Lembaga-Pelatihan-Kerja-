package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.jenis_dokumen.jenis_dokumen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mismundev.yasin_perizinanlembagapelatihankerja.R
import com.mismundev.yasin_perizinanlembagapelatihankerja.adapter.admin.AdminJenisDokumenAdapter
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.JenisDokumenModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.ResponseModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ActivityAdminJenisDokumenBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.AlertDialogJenisDokumenBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.AlertDialogKeteranganBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.AlertDialogKonfirmasiBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.AlertDialogPermohonanDokumenBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.LoadingAlertDialog
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.OnClickItem
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.SharedPreferencesLogin
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@AndroidEntryPoint
class AdminJenisDokumenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminJenisDokumenBinding
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    private val viewModel: AdminJenisDokumenViewModel by viewModels()
    @Inject
    lateinit var loading: LoadingAlertDialog
    private lateinit var adapter: AdminJenisDokumenAdapter
    private var idDaftarPelatihan = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminJenisDokumenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDataSebelumnya()
        setButton()
        setSharedPreferencesLogin()
        getJenisDokumen()
        getPostTambahData()
        getPostUpdateData()
        getPostDeleteData()
    }


    private fun setDataSebelumnya() {
        val i = intent.extras
        if(i != null){
            idDaftarPelatihan = i.getInt("id_daftar_pelatihan", 0)
            fetchJenisDokumen(idDaftarPelatihan)
        }
    }

    private fun setSharedPreferencesLogin() {
        sharedPreferencesLogin = SharedPreferencesLogin((this@AdminJenisDokumenActivity))
    }

    private fun setButton() {
        binding.apply {
            myAppBar.apply {
                ivNavDrawer.visibility = View.GONE
                ivBack.visibility = View.VISIBLE
                tvTitle.text = "Jenis Dokumen"

                ivBack.setOnClickListener {
                    finish()
                }
            }

            btnTambah.setOnClickListener {
                setShowDialogTambah()
            }
        }
    }

    private fun setShowDialogTambah() {
        val view = AlertDialogJenisDokumenBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminJenisDokumenActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tVTitle.text = "Tambah Jenis Dokumen"

            var ekstensi = ""

            val arrayAdapterEkstensi = ArrayAdapter.createFromResource(
                this@AdminJenisDokumenActivity,
                R.array.ekstensi,
                android.R.layout.simple_spinner_item
            )
            arrayAdapterEkstensi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spEkstensi.adapter = arrayAdapterEkstensi
            spEkstensi.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    ekstensi = parent?.getItemAtPosition(position).toString()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            btnSimpan.setOnClickListener {
                var cek = false
                if (etEditJenisDokumen.text.toString().isEmpty()) {
                    etEditJenisDokumen.error = "Tidak Boleh Kosong"
                    cek = true
                }

                if (!cek) {
                    val jenisDokumen = etEditJenisDokumen.text.toString()
                    postTambahDataJenisDokumen(
                        idDaftarPelatihan, jenisDokumen, ekstensi
                    )

                    dialogInputan.dismiss()
                }
            }

            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postTambahDataJenisDokumen(
        idDaftarPelatihan: Int,
        jenisDokumen: String,
        ekstensi: String,
    ){
        viewModel.postTambahJenisDokumen(
            idDaftarPelatihan, jenisDokumen, ekstensi
        )
    }

    private fun getPostTambahData(){
        viewModel.getTambahPermohonan().observe(this@AdminJenisDokumenActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminJenisDokumenActivity)
                is UIState.Success-> setSuccessTambahData(result.data)
                is UIState.Failure-> setFailureTambahData(result.message)
                else -> {}
            }
        }
    }

    private fun setFailureTambahData(message: String) {
        Toast.makeText(this@AdminJenisDokumenActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessTambahData(data: ResponseModel?) {
        if(data != null){
            if(data.status == "0"){
                fetchJenisDokumen(idDaftarPelatihan)
            } else{
                Toast.makeText(this@AdminJenisDokumenActivity, data.message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminJenisDokumenActivity, "Gagal: Ada kesalahan di API", Toast.LENGTH_SHORT).show()
        }
        loading.alertDialogCancel()
    }

    private fun fetchJenisDokumen(idPermohonan: Int) {
        viewModel.fetchJenisDokumen(idPermohonan)
    }

    private fun getJenisDokumen(){
        viewModel.getJenisDokumen().observe(this@AdminJenisDokumenActivity){result->
            when(result){
                is UIState.Loading-> setStartShimmer()
                is UIState.Failure-> setFailureFetchJenisDokumen(result.message)
                is UIState.Success-> setSuccessFetchJenisDokumen(result.data)
                else -> {}
            }
        }
    }

    private fun setFailureFetchJenisDokumen(message: String) {
        setStopShimmer()
        Toast.makeText(this@AdminJenisDokumenActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessFetchJenisDokumen(data: ArrayList<JenisDokumenModel>) {
        setStopShimmer()
        if(data.isNotEmpty()){
            setAdapterPermohonan(data)
        } else{
            Toast.makeText(this@AdminJenisDokumenActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapterPermohonan(data: ArrayList<JenisDokumenModel>) {
        adapter = AdminJenisDokumenAdapter(data, object : OnClickItem.AdminClickJenisDokumen{
            override fun clickJenisDokumen(keterangan: String, title: String) {
                showKeterangan(title, keterangan)
            }

            override fun clickItemSetting(jenisDokumen: JenisDokumenModel, it: View) {
                val popupMenu = PopupMenu(this@AdminJenisDokumenActivity, it)
                popupMenu.inflate(R.menu.popup_edit_hapus)
                popupMenu.setOnMenuItemClickListener(object :
                    PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                        when (menuItem!!.itemId) {
                            R.id.edit -> {
                                setShowDialogEdit(jenisDokumen)
                                return true
                            }
                            R.id.hapus -> {
                                setShowDialogHapus(jenisDokumen)
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
            rvPermohonan.layoutManager = LinearLayoutManager(this@AdminJenisDokumenActivity, LinearLayoutManager.VERTICAL, false)
            rvPermohonan.adapter = adapter
        }
    }

    private fun showKeterangan(title:String, keterangan: String) {
        val view = AlertDialogKeteranganBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminJenisDokumenActivity)
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

    private fun setShowDialogEdit(jenisDokumen: JenisDokumenModel) {
        val view = AlertDialogJenisDokumenBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminJenisDokumenActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            etEditJenisDokumen.setText(jenisDokumen.jenis_dokumen)
            var ekstensi = ""

            val arrayAdapterEkstensi = ArrayAdapter.createFromResource(
                this@AdminJenisDokumenActivity,
                R.array.ekstensi,
                android.R.layout.simple_spinner_item
            )
            arrayAdapterEkstensi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spEkstensi.adapter = arrayAdapterEkstensi
            spEkstensi.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    ekstensi = parent?.getItemAtPosition(position).toString()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
            val position = arrayAdapterEkstensi.getPosition(jenisDokumen.ekstensi)
            spEkstensi.setSelection(position)

            btnSimpan.setOnClickListener {
                var cek = false
                if (etEditJenisDokumen.text.toString().isEmpty()) {
                    etEditJenisDokumen.error = "Tidak Boleh Kosong"
                    cek = true
                }

                if (!cek) {
                    val idJenisDokumen = jenisDokumen.id_jenis_dokumen!!
                    val jenisDokumen = etEditJenisDokumen.text.toString()

                    postUpdateDataPermohonan(
                        idJenisDokumen, jenisDokumen, ekstensi
                    )

                    dialogInputan.dismiss()
                }
            }

            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postUpdateDataPermohonan(
        idPermohonan: Int,
        jenisDokumen: String,
        ekstensi: String,
    ){
        viewModel.postUpdateJenisDokumen(
            idPermohonan, jenisDokumen, ekstensi
        )
    }

    private fun getPostUpdateData(){
        viewModel.getUpdatePermohonan().observe(this@AdminJenisDokumenActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminJenisDokumenActivity)
                is UIState.Success-> setSuccessUpdateData(result.data)
                is UIState.Failure-> setFailureUpdateData(result.message)
                else -> {}
            }
        }
    }

    private fun setFailureUpdateData(message: String) {
        Toast.makeText(this@AdminJenisDokumenActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessUpdateData(data: ResponseModel?) {
        if(data != null){
            if(data.status == "0"){
                viewModel.fetchJenisDokumen(idDaftarPelatihan)
            } else{
                Toast.makeText(this@AdminJenisDokumenActivity, data.message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminJenisDokumenActivity, "Gagal: Ada kesalahan di API", Toast.LENGTH_SHORT).show()
        }
        loading.alertDialogCancel()
    }

    @SuppressLint("SetTextI18n")
    private fun setShowDialogHapus(dokumen: JenisDokumenModel) {
        val view = AlertDialogKonfirmasiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminJenisDokumenActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKonfirmasi.text = "Yakin Hapus Data Ini ?"
            tvBodyKonfirmasi.text = "Data akan terhapus dan tidak dapat dikembalikan"

            btnKonfirmasi.setOnClickListener {
                postDeleteData(dokumen.id_jenis_dokumen!!)
                dialogInputan.dismiss()
            }

            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postDeleteData(idDokumen: Int){
        viewModel.postDeleteJenisDokumen(idDokumen)
    }

    private fun getPostDeleteData(){
        viewModel.getDeletePermohonan().observe(this@AdminJenisDokumenActivity){result->
            when(result){
                is UIState.Loading-> setStartShimmer()
                is UIState.Success-> setSuccessDeleteData(result.data)
                is UIState.Failure-> setFailureDeleteData(result.message)
                else -> {}
            }
        }
    }

    private fun setFailureDeleteData(message: String) {
        Toast.makeText(this@AdminJenisDokumenActivity, message, Toast.LENGTH_SHORT).show()
        setStopShimmer()
    }

    private fun setSuccessDeleteData(data: ResponseModel?) {
        if(data != null){
            if(data.status == "0"){
                fetchJenisDokumen(idDaftarPelatihan)
            } else{
                Toast.makeText(this@AdminJenisDokumenActivity, data.message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminJenisDokumenActivity, "Gagal: Ada kesalahan di API", Toast.LENGTH_SHORT).show()
        }
        setStopShimmer()
    }

    private fun setStartShimmer(){
        binding.apply {
            smPermohonan.startShimmer()
            smPermohonan.visibility = View.VISIBLE
            rvPermohonan.visibility = View.GONE
        }
    }

    private fun setStopShimmer(){
        binding.apply {
            smPermohonan.stopShimmer()
            smPermohonan.visibility = View.GONE
            rvPermohonan.visibility = View.VISIBLE
        }
    }

}