package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.pendaftar

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.mismundev.yasin_perizinanlembagapelatihankerja.R
import com.mismundev.yasin_perizinanlembagapelatihankerja.adapter.admin.AdminPendaftarAdapter
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.ArrayListSpinnerModal
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.DaftarPelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.PendaftarModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.ResponseModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.UsersModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ActivityAdminPendaftarBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.AlertDialogKeteranganBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.AlertDialogKonfirmasiBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.AlertDialogPendaftarBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.AlertDialogShowImageBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.main.AdminMainActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.Constant
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.KontrolNavigationDrawer
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.LoadingAlertDialog
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.OnClickItem
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.SharedPreferencesLogin
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.TanggalDanWaktu
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.ArrayList
import javax.inject.Inject

@AndroidEntryPoint
class AdminPendaftarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminPendaftarBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    private val viewModel: AdminPendaftarViewModel by viewModels()
    @Inject
    lateinit var loading: LoadingAlertDialog
    @Inject
    lateinit var tanggalDanWaktu: TanggalDanWaktu
    private lateinit var adapter: AdminPendaftarAdapter
    private var tempView: AlertDialogPendaftarBinding? = null
    private var listIdUser: ArrayList<Int>? = arrayListOf()
    private var listUser: ArrayList<String>? = arrayListOf()
    private var listIdDaftarPelatihan: ArrayList<Int>? = arrayListOf()
    private var listDaftarPelatihan: ArrayList<String>? = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminPendaftarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        setButton()
        setSharedPreferencesLogin()
        fetchUser()
        getUser()
        fetchPendaftar()
        getPendaftar()
        fetchDaftarPelatihan()
        getDaftarPelatihan()
        getPostTambahData()
        getPostUpdatePendaftar()
        getPostDeletePendaftar()
    }

    private fun setSharedPreferencesLogin() {
        sharedPreferencesLogin = SharedPreferencesLogin((this@AdminPendaftarActivity))
    }

    @SuppressLint("SetTextI18n")
    private fun setKontrolNavigationDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminPendaftarActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, myAppBar.ivNavDrawer, this@AdminPendaftarActivity)

            myAppBar.tvTitle.text =  "Pendaftar"
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
        val view = AlertDialogPendaftarBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminPendaftarActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tempView = view
            var selectedPendaftar = 0
            tVTitle.text = "Tambah Pendaftar"

            val arrayAdapterPendaftar = ArrayAdapter(
                this@AdminPendaftarActivity,
                android.R.layout.simple_spinner_item,
                listUser!!
            )

            arrayAdapterPendaftar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spPendaftar.adapter = arrayAdapterPendaftar

            spPendaftar.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedPendaftar = listIdUser!![position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            var selectedDaftarPelatihan = 0
            val arrayAdapterPelatihan = ArrayAdapter(
                this@AdminPendaftarActivity,
                android.R.layout.simple_spinner_item,
                listDaftarPelatihan!!
            )
            arrayAdapterPelatihan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spDaftarPelatihan.adapter = arrayAdapterPelatihan

            spDaftarPelatihan.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedDaftarPelatihan = listIdDaftarPelatihan!![position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            var selectedItemKet = ""
            val arrayAdapterKet = ArrayAdapter.createFromResource(
                this@AdminPendaftarActivity,
                R.array.keterangan,
                android.R.layout.simple_spinner_item
            )
            arrayAdapterKet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spKeterangan.adapter = arrayAdapterKet

            spKeterangan.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedItemKet = spKeterangan.selectedItem.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            etEditTanggalPendaftaran.setOnClickListener {
                tanggalDanWaktu.selectedDateTime(
                    tanggalDanWaktu.tanggalDanWaktuZonaMakassar(),
                    etEditTanggalPendaftaran,
                    this@AdminPendaftarActivity
                )
            }

            btnSimpan.setOnClickListener {
                postTambahDataPendaftar(
                    selectedPendaftar,
                    selectedDaftarPelatihan,
                    selectedItemKet
                )
                dialogInputan.dismiss()
            }

            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postTambahDataPendaftar(
        idUser: Int, idDaftarPelatihan: Int, ket: String
    ){
        viewModel.postTambahPendaftar(
            idUser, idDaftarPelatihan, ket
        )
    }

    private fun getPostTambahData(){
        viewModel.getTambahPendaftar().observe(this@AdminPendaftarActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminPendaftarActivity)
                is UIState.Success-> setSuccessTambahData(result.data)
                is UIState.Failure-> setFailureTambahData(result.message)
            }
        }
    }

    private fun setFailureTambahData(message: String) {
        Toast.makeText(this@AdminPendaftarActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessTambahData(data: ResponseModel?) {
        loading.alertDialogCancel()
        if(data != null){
            if(data.status == "0"){
                fetchPendaftar()
            } else{
                Toast.makeText(this@AdminPendaftarActivity, data.message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminPendaftarActivity, "Gagal: Ada kesalahan di API", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchUser() {
        viewModel.fetchUser()
    }

    private fun getUser(){
        viewModel.getUser().observe(this@AdminPendaftarActivity){result->
            when(result){
                is UIState.Loading-> {}
                is UIState.Failure-> setFailureFetchUser(result.message)
                is UIState.Success-> setSuccessFetchUser(result.data)
            }
        }
    }

    private fun setFailureFetchUser(message: String) {
        Toast.makeText(this@AdminPendaftarActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessFetchUser(data: ArrayList<UsersModel>) {
        if(data.isNotEmpty()){
            for(value in data){
                listIdUser!!.add(value.idUser!!)
                listUser!!.add(value.nama!!)
            }
        } else{
            Toast.makeText(this@AdminPendaftarActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchDaftarPelatihan() {
        viewModel.fetchDaftarPelatihan()
    }

    private fun getDaftarPelatihan(){
        viewModel.getDaftarPelatihan().observe(this@AdminPendaftarActivity){result->
            when(result){
                is UIState.Loading-> {}
                is UIState.Failure-> setFailureFetchDaftarPelatihan(result.message)
                is UIState.Success-> setSuccessFetchDaftarPelatihan(result.data)
            }
        }
    }

    private fun setFailureFetchDaftarPelatihan(message: String) {
        Toast.makeText(this@AdminPendaftarActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessFetchDaftarPelatihan(data: ArrayList<DaftarPelatihanModel>) {
        if(data.isNotEmpty()){
            for(value in data){
                val namaPelatihan = value.pelatihanModel!!.namaPelatihan!!
                val batch = value.batch
                val daftarPelatihan = "$namaPelatihan - Batch $batch"

                listIdDaftarPelatihan!!.add(value.idDaftarPelatihan!!)
                listDaftarPelatihan!!.add(daftarPelatihan)
            }
        } else{
            Toast.makeText(this@AdminPendaftarActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchPendaftar() {
        viewModel.fetchPendaftar()
    }

    private fun getPendaftar(){
        viewModel.getPendaftar().observe(this@AdminPendaftarActivity){result->
            when(result){
                is UIState.Loading-> setStartShimmer()
                is UIState.Failure-> setFailureFetchPendaftar(result.message)
                is UIState.Success-> setSuccessFetchPendaftar(result.data)
            }
        }
    }

    private fun setFailureFetchPendaftar(message: String) {
        setStopShimmer()
        Toast.makeText(this@AdminPendaftarActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessFetchPendaftar(data: ArrayList<PendaftarModel>) {
        setStopShimmer()
        if(data.isNotEmpty()){
            setAdapterPendaftar(data)
//            for(value in data){
//                listIdUser!!.add(value.userModel!!.idUser!!)
//                listUser!!.add(value.userModel!!.nama!!)
//            }
        } else{
            Toast.makeText(this@AdminPendaftarActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapterPendaftar(data: ArrayList<PendaftarModel>) {
        adapter = AdminPendaftarAdapter(data, object : OnClickItem.AdminClickPendaftar{

            override fun clickPendaftar(pendaftar: String, title: String) {
                showKeterangan(title, pendaftar)
            }

            override fun clickPelatihan(namaPelatihan: String, title: String) {
                showKeterangan(title, namaPelatihan)
            }

            override fun clickItemSetting(pendaftar: PendaftarModel, it: View) {
                val popupMenu = PopupMenu(this@AdminPendaftarActivity, it)
                popupMenu.inflate(R.menu.popup_edit_hapus)
                popupMenu.setOnMenuItemClickListener(object :
                    PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                        when (menuItem!!.itemId) {
                            R.id.edit -> {
                                setShowDialogEdit(pendaftar)
                                return true
                            }
                            R.id.hapus -> {
                                setShowDialogHapus(pendaftar)
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
            rvPendaftar.layoutManager = LinearLayoutManager(this@AdminPendaftarActivity, LinearLayoutManager.VERTICAL, false)
            rvPendaftar.adapter = adapter
        }
    }

    private fun showKeterangan(title:String, keterangan: String) {
        val view = AlertDialogKeteranganBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminPendaftarActivity)
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


    private fun setShowDialogEdit(pendaftar: PendaftarModel) {
        val view = AlertDialogPendaftarBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminPendaftarActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tempView = view
            tVTitle.text = "Edit Pendaftar"

            var selectedPendaftar = 0
            val arrayAdapterPendaftar = ArrayAdapter(
                this@AdminPendaftarActivity,
                android.R.layout.simple_spinner_item,
                listUser!!
            )

            arrayAdapterPendaftar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spPendaftar.adapter = arrayAdapterPendaftar

            spPendaftar.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedPendaftar = listIdUser!![position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
            val searchUser = listIdUser!!.indexOf(pendaftar.userModel!!.idUser)
            if(searchUser != -1){
                spPendaftar.setSelection(searchUser)
            }

            var selectedDaftarPelatihan = 0
            val arrayAdapterPelatihan = ArrayAdapter(
                this@AdminPendaftarActivity,
                android.R.layout.simple_spinner_item,
                listDaftarPelatihan!!
            )
            arrayAdapterPelatihan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spDaftarPelatihan.adapter = arrayAdapterPelatihan

            spDaftarPelatihan.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedDaftarPelatihan = listIdDaftarPelatihan!![position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
            val searchDaftarPelatihan = listIdDaftarPelatihan!!.indexOf(pendaftar.idDaftarPelatihan!!.toInt())
            if(searchDaftarPelatihan != -1){
                spDaftarPelatihan.setSelection(searchDaftarPelatihan)
            }

            var selectedItemKet = ""
            val arrayAdapterKet = ArrayAdapter.createFromResource(
                this@AdminPendaftarActivity,
                R.array.keterangan,
                android.R.layout.simple_spinner_item
            )
            arrayAdapterKet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spKeterangan.adapter = arrayAdapterKet

            spKeterangan.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedItemKet = spKeterangan.selectedItem.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
            for((no, value) in resources.getResourceName(R.array.keterangan).withIndex()){
                Log.d("DetaillTAG", "setShowDialogEdit: $no, $value")
                if(value.toString() == pendaftar.ket!!){
                    spKeterangan.setSelection(no)
                }
            }

            etEditTanggalPendaftaran.text = pendaftar.tglDaftar

            etEditTanggalPendaftaran.setOnClickListener {
                tanggalDanWaktu.selectedDateTime(
                    etEditTanggalPendaftaran.text.toString(),
                    etEditTanggalPendaftaran,
                    this@AdminPendaftarActivity
                )
            }

            btnSimpan.setOnClickListener {
                postUpdatePendaftar(
                    pendaftar.idPendaftar!!,
                    selectedPendaftar,
                    selectedDaftarPelatihan,
                    selectedItemKet
                )

                dialogInputan.dismiss()
            }

            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postUpdatePendaftar(
        idPendaftar: Int, idUser: Int, idDaftarPelatihan: Int, ket: String
    ){
        viewModel.postUpdatePendaftar(
            idPendaftar, idUser, idDaftarPelatihan, ket
        )
    }

    private fun getPostUpdatePendaftar(){
        viewModel.getUpdatePendaftar().observe(this@AdminPendaftarActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminPendaftarActivity)
                is UIState.Success-> setSuccessUpdatePendaftar(result.data)
                is UIState.Failure-> setFailureUpdatePendaftar(result.message)
            }
        }
    }

    private fun setFailureUpdatePendaftar(message: String) {
        Toast.makeText(this@AdminPendaftarActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessUpdatePendaftar(data: ResponseModel?) {
        loading.alertDialogCancel()
        if(data != null){
            if(data.status == "0"){
                fetchPendaftar()
            } else{
                Toast.makeText(this@AdminPendaftarActivity, data.message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminPendaftarActivity, "Gagal: Ada kesalahan di API", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setShowDialogHapus(pendaftar: PendaftarModel) {
        val view = AlertDialogKonfirmasiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminPendaftarActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKonfirmasi.text = "Yakin Hapus Data Ini ?"
            tvBodyKonfirmasi.text = "Data akan terhapus dan tidak dapat dikembalikan"

            btnKonfirmasi.setOnClickListener {
                postDeletePendaftar(pendaftar.idPendaftar!!)
                dialogInputan.dismiss()
            }

            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postDeletePendaftar(
        idPendaftar: Int
    ){
        viewModel.postDeletePendaftar(
            idPendaftar
        )
    }

    private fun getPostDeletePendaftar(){
        viewModel.getDeletePendaftar().observe(this@AdminPendaftarActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminPendaftarActivity)
                is UIState.Success-> setSuccessDeletePendaftar(result.data)
                is UIState.Failure-> setFailureDeletePendaftar(result.message)
            }
        }
    }

    private fun setFailureDeletePendaftar(message: String) {
        Toast.makeText(this@AdminPendaftarActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessDeletePendaftar(data: ResponseModel?) {
        loading.alertDialogCancel()
        if(data != null){
            if(data.status == "0"){
                fetchPendaftar()
            } else{
                Toast.makeText(this@AdminPendaftarActivity, data.message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminPendaftarActivity, "Gagal: Ada kesalahan di API", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setStartShimmer(){
        binding.apply {
            smPendaftar.startShimmer()
            smPendaftar.visibility = View.VISIBLE
            hsPendaftar.visibility = View.GONE
        }
    }

    private fun setStopShimmer(){
        binding.apply {
            smPendaftar.stopShimmer()
            smPendaftar.visibility = View.GONE
            hsPendaftar.visibility = View.VISIBLE
        }
    }

}