package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.daftar_pelatihan

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
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.mismundev.yasin_perizinanlembagapelatihankerja.R
import com.mismundev.yasin_perizinanlembagapelatihankerja.adapter.admin.AdminDaftarPelatihanAdapter
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.DaftarPelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.PelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.ResponseModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ActivityAdminDaftarPelatihanBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.AlertDialogKeteranganBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.AlertDialogDaftarPelatihanBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.AlertDialogShowImageBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.daftar_daftarDaftarPelatihan.AdminDaftarPelatihanViewModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.main.AdminMainActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.pelatihan.AdminPelatihanActivity
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
class AdminDaftarPelatihanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminDaftarPelatihanBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    private val viewModel: AdminDaftarPelatihanViewModel by viewModels()
    @Inject
    lateinit var loading: LoadingAlertDialog
    @Inject
    lateinit var tanggalDanWaktu: TanggalDanWaktu
    private lateinit var adapter: AdminDaftarPelatihanAdapter
    private var fileImage: MultipartBody.Part? = null
    private var tempView: AlertDialogDaftarPelatihanBinding? = null
    private var arrayPelatihan: ArrayList<PelatihanModel>? = arrayListOf()
    private var listJenisPelatihan: ArrayList<String>? = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDaftarPelatihanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        setButton()
        setSharedPreferencesLogin()
        fetchPelatihan()
        getPelatihan()
        fetchDaftarPelatihan()
        getDaftarPelatihan()
        getPostTambahData()
        getPostUpdateDaftarPelatihan()
    }

    private fun setSharedPreferencesLogin() {
        sharedPreferencesLogin = SharedPreferencesLogin((this@AdminDaftarPelatihanActivity))
    }

    @SuppressLint("SetTextI18n")
    private fun setKontrolNavigationDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminDaftarPelatihanActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, myAppBar.ivNavDrawer, this@AdminDaftarPelatihanActivity)

            myAppBar.tvTitle.text =  "Daftar Pelatiihan"
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
        val view = AlertDialogDaftarPelatihanBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminDaftarPelatihanActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            etEditTanggalPelaksanaan.text = tanggalDanWaktu.tanggalDanWaktuZonaMakassar()
            etEditTanggalMulaiDaftar.text = tanggalDanWaktu.tanggalSekarangZonaMakassar()
            etEditTanggalBerakhirDaftar.text = tanggalDanWaktu.tanggalSekarangZonaMakassar()

            tempView = view
            var selectedItem = 0
            tVTitle.text = "Tambah Pelatihan"
            etEditSertifikat.setOnClickListener {
                if(checkPermission()){
                    pickImageFile()
                } else{
                    requestPermission()
                }
            }
            val arrayAdapterKotaKab = ArrayAdapter(
                this@AdminDaftarPelatihanActivity,
                android.R.layout.simple_spinner_item,
                listJenisPelatihan!!
            )

            arrayAdapterKotaKab.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spPelatihan.adapter = arrayAdapterKotaKab

            spPelatihan.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedItem = position
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            etEditTanggalPelaksanaan.setOnClickListener{
                tanggalDanWaktu.selectedDateTime(
                    etEditTanggalPelaksanaan.text.toString(),
                    etEditTanggalPelaksanaan,
                    this@AdminDaftarPelatihanActivity
                )
            }
            etEditTanggalMulaiDaftar.setOnClickListener {
                tanggalDanWaktu.selectedDate(
                    etEditTanggalMulaiDaftar.text.toString(),
                    etEditTanggalMulaiDaftar,
                    this@AdminDaftarPelatihanActivity
                )
            }
            etEditTanggalBerakhirDaftar.setOnClickListener {
                tanggalDanWaktu.selectedDate(
                    etEditTanggalBerakhirDaftar.text.toString(),
                    etEditTanggalBerakhirDaftar,
                    this@AdminDaftarPelatihanActivity
                )
            }

            btnSimpan.setOnClickListener {
                var cek = false
                if (etEditKuota.toString().isEmpty()) {
                    etEditKuota.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if (etEditBiaya.toString().isEmpty()) {
                    etEditBiaya.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if (etEditLokasi.toString().isEmpty()) {
                    etEditLokasi.error = "Tidak Boleh Kosong"
                    cek = true
                }

                if (!cek) {
                    if (etEditSertifikat.text.toString() == resources.getString(R.string.ket_klik_file)) {
                        // ketika tidak ada gambar masuk
                        Toast.makeText(this@AdminDaftarPelatihanActivity, "Masukkan gambar", Toast.LENGTH_SHORT).show()
                    } else{
                        // ada gambar
                        postTambahDataDaftarPelatihan(
                            arrayPelatihan!![selectedItem].idPelatihan!!,
                            etEditKuota.text.toString().toInt(),
                            etEditBiaya.text.toString().toInt(),
                            etEditTanggalPelaksanaan.text.toString(),
                            etEditTanggalMulaiDaftar.text.toString(),
                            etEditTanggalBerakhirDaftar.text.toString(),
                            etEditLokasi.text.toString(),
                        )
                        dialogInputan.dismiss()
                    }
                }
            }

            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postTambahDataDaftarPelatihan(
        idPelatihan: Int, kuota: Int,
        biaya: Int, tglMulai: String, tglBerakhir: String,
        tglPelaksanaan: String, lokasi: String
    ){
        viewModel.postTambahDaftarPelatihan(
            convertStringToMultipartBody("post_add_daftar_pelatihan"),
            convertStringToMultipartBody("$idPelatihan"),
            convertStringToMultipartBody("$kuota"),
            convertStringToMultipartBody("$biaya"),
            convertStringToMultipartBody(tglMulai),
            convertStringToMultipartBody(tglBerakhir),
            convertStringToMultipartBody(tglPelaksanaan),
            convertStringToMultipartBody(lokasi),
            fileImage!!
        )
    }

    private fun getPostTambahData(){
        viewModel.getTambahDaftarPelatihan().observe(this@AdminDaftarPelatihanActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminDaftarPelatihanActivity)
                is UIState.Success-> setSuccessTambahDataDaftarPelatihan(result.data)
                is UIState.Failure-> setFailureTambahDataDaftarPelatihan(result.message)
            }
        }
    }

    private fun setFailureTambahDataDaftarPelatihan(message: String) {
        Toast.makeText(this@AdminDaftarPelatihanActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessTambahDataDaftarPelatihan(data: ResponseModel?) {
        loading.alertDialogCancel()
        if(data != null){
            if(data.status == "0"){
                Toast.makeText(this@AdminDaftarPelatihanActivity, "Berhasil Tambah Data", Toast.LENGTH_SHORT).show()
                fetchDaftarPelatihan()
            } else{
                Toast.makeText(this@AdminDaftarPelatihanActivity, data.message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminDaftarPelatihanActivity, "Gagal: Ada kesalahan di API", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchPelatihan() {
        viewModel.fetchPelatihan()
    }

    private fun getPelatihan(){
        viewModel.getPelatihan().observe(this@AdminDaftarPelatihanActivity){result->
            when(result){
                is UIState.Loading-> {}
                is UIState.Failure-> setFailureFetchPelatihan(result.message)
                is UIState.Success-> setSuccessFetchPelatihan(result.data)
            }
        }
    }

    private fun setFailureFetchPelatihan(message: String) {
//        setStopShimmer()
//        Toast.makeText(this@AdminDaftarPelatihanActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessFetchPelatihan(data: ArrayList<PelatihanModel>) {
//        setStopShimmer()
        if(data.isNotEmpty()){
            arrayPelatihan = data
            for(value in data){
                listJenisPelatihan!!.add(value.namaPelatihan!!)
            }
        } else{
            Toast.makeText(this@AdminDaftarPelatihanActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchDaftarPelatihan() {
        viewModel.fetchDaftarPelatihan()
    }

    private fun getDaftarPelatihan(){
        Log.d("DetailTAG", "getDaftarPelatihan: getDaftarPelatihan")
        viewModel.getDaftarPelatihan().observe(this@AdminDaftarPelatihanActivity){result->
            when(result){
                is UIState.Loading-> setStartShimmer()
                is UIState.Failure-> setFailureFetchDaftarPelatihan(result.message)
                is UIState.Success-> setSuccessFetchDaftarPelatihan(result.data)
            }
        }
    }

    private fun setFailureFetchDaftarPelatihan(message: String) {
        setStopShimmer()
        Log.d("DetailTAG", "getDaftarPelatihan: gagal")
        Toast.makeText(this@AdminDaftarPelatihanActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessFetchDaftarPelatihan(data: ArrayList<DaftarPelatihanModel>) {
        Log.d("DetailTAG", "getDaftarPelatihan: berhasil")
        setStopShimmer()
        if(data.isNotEmpty()){
            setAdapterPelatihan(data)
        } else{
            Toast.makeText(this@AdminDaftarPelatihanActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapterPelatihan(data: ArrayList<DaftarPelatihanModel>) {
        adapter = AdminDaftarPelatihanAdapter(data, object : OnClickItem.AdminClickDaftarPelatihan{
            override fun clickPenjelasan(deskripsi: String, title: String) {
                showKeterangan(title, deskripsi)
            }

            override fun clickGambar(gambar: String, title: String) {
                setShowImage(title, gambar)
            }

            override fun clickItemSetting(daftarPelatihan: DaftarPelatihanModel, it: View) {
                val popupMenu = PopupMenu(this@AdminDaftarPelatihanActivity, it)
                popupMenu.inflate(R.menu.popup_edit)
                popupMenu.setOnMenuItemClickListener(object :
                    PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                        when (menuItem!!.itemId) {
                            R.id.edit -> {
                                setShowDialogEdit(daftarPelatihan)
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
            rvPelatihan.layoutManager = LinearLayoutManager(this@AdminDaftarPelatihanActivity, LinearLayoutManager.VERTICAL, false)
            rvPelatihan.adapter = adapter
        }
    }

    private fun showKeterangan(title:String, keterangan: String) {
        val view = AlertDialogKeteranganBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminDaftarPelatihanActivity)
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


    private fun setShowImage(title:String, gambar: String) {
        val view = AlertDialogShowImageBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminDaftarPelatihanActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitle.text = title
            btnClose.setOnClickListener {
                dialogInputan.dismiss()
            }
        }

        Glide.with(this@AdminDaftarPelatihanActivity)
            .load("${Constant.LOCATION_GAMBAR}$gambar") // URL Gambar
            .placeholder(R.drawable.loading)
            .error(R.drawable.image_error)
            .into(view.ivShowImage) // imageView mana yang akan diterapkan

    }


    private fun setShowDialogEdit(daftarPelatihan: DaftarPelatihanModel) {
        val view = AlertDialogDaftarPelatihanBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminDaftarPelatihanActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            etEditKuota.setText(daftarPelatihan.kuota.toString())
            etEditBiaya.setText(daftarPelatihan.biaya.toString())
            etEditLokasi.setText(daftarPelatihan.lokasi.toString())

            etEditTanggalPelaksanaan.text = daftarPelatihan.tglPelaksanaan
            etEditTanggalMulaiDaftar.text = daftarPelatihan.tglMulaiDaftar
            etEditTanggalBerakhirDaftar.text = daftarPelatihan.tglBerakhirDaftar

            tempView = view
            var selectedItem = 0
            tVTitle.text = "Edit Daftar Pelatihan"
            etEditSertifikat.setOnClickListener {
                if(checkPermission()){
                    pickImageFile()
                } else{
                    requestPermission()
                }
            }
            val arrayAdapterKotaKab = ArrayAdapter(
                this@AdminDaftarPelatihanActivity,
                android.R.layout.simple_spinner_item,
                listJenisPelatihan!!
            )

            arrayAdapterKotaKab.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spPelatihan.adapter = arrayAdapterKotaKab

            spPelatihan.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedItem = position
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            for((no, value) in arrayPelatihan!!.withIndex()){
                if(value.idPelatihan!! == daftarPelatihan.idPelatihan){
                    spPelatihan.setSelection(no)
                    selectedItem = no
                }
            }

            etEditTanggalPelaksanaan.setOnClickListener{
                tanggalDanWaktu.selectedDateTime(
                    etEditTanggalPelaksanaan.text.toString(),
                    etEditTanggalPelaksanaan,
                    this@AdminDaftarPelatihanActivity
                )
            }
            etEditTanggalMulaiDaftar.setOnClickListener {
                tanggalDanWaktu.selectedDate(
                    etEditTanggalMulaiDaftar.text.toString(),
                    etEditTanggalPelaksanaan,
                    this@AdminDaftarPelatihanActivity
                )
            }
            etEditTanggalBerakhirDaftar.setOnClickListener {
                tanggalDanWaktu.selectedDate(
                    etEditTanggalBerakhirDaftar.text.toString(),
                    etEditTanggalBerakhirDaftar,
                    this@AdminDaftarPelatihanActivity
                )
            }

            btnSimpan.setOnClickListener {
                var cek = false
                if (etEditKuota.toString().isEmpty()) {
                    etEditKuota.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if (etEditBiaya.toString().isEmpty()) {
                    etEditBiaya.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if (etEditLokasi.toString().isEmpty()) {
                    etEditLokasi.error = "Tidak Boleh Kosong"
                    cek = true
                }

                if (!cek) {
                    if (etEditSertifikat.text.toString() == resources.getString(R.string.ket_klik_file)) {
                        // ketika tidak ada gambar masuk
                        postUpdateDaftarPelatihanAddImage(
                            daftarPelatihan.idDaftarPelatihan!!, selectedItem,
                            etEditKuota.text.toString().toInt(), etEditBiaya.text.toString().toInt(),
                            etEditTanggalPelaksanaan.text.toString(), etEditTanggalMulaiDaftar.text.toString(),
                            etEditTanggalBerakhirDaftar.text.toString(), etEditLokasi.text.toString()
                        )
                        dialogInputan.dismiss()
                    } else{
                        // ada gambar
                        postUpdateDaftarPelatihan(
                            daftarPelatihan.idDaftarPelatihan!!, selectedItem,
                            etEditKuota.text.toString().toInt(), etEditBiaya.text.toString().toInt(),
                            etEditTanggalPelaksanaan.text.toString(), etEditTanggalMulaiDaftar.text.toString(),
                            etEditTanggalBerakhirDaftar.text.toString(), etEditLokasi.text.toString()
                        )
                        dialogInputan.dismiss()
                        dialogInputan.dismiss()
                    }
                }
            }

            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }

    }

    private fun postUpdateDaftarPelatihanAddImage(
        idDaftarPelatihan: Int,  idPelatihan: Int, kuota: Int,
        biaya: Int, tglMulai: String, tglBerakhir: String,
        tglPelaksanaan: String, lokasi: String
    ){
        viewModel.postUpdateDaftarPelatihanAddImage(
            convertStringToMultipartBody("post_update_daftar_pelatihan_add_image"),
            convertStringToMultipartBody("$idDaftarPelatihan"),
            convertStringToMultipartBody("$idPelatihan"),
            convertStringToMultipartBody("$kuota"),
            convertStringToMultipartBody("$biaya"),
            convertStringToMultipartBody(tglMulai),
            convertStringToMultipartBody(tglBerakhir),
            convertStringToMultipartBody(tglPelaksanaan),
            convertStringToMultipartBody(lokasi),
            fileImage!!
        )
    }

    private fun postUpdateDaftarPelatihan(
        idDaftarPelatihan: Int, idPelatihan: Int, kuota: Int,
        biaya: Int, tglMulai: String, tglBerakhir: String,
        tglPelaksanaan: String, lokasi: String
    ){
        viewModel.postUpdateDaftarPelatihan(
            idDaftarPelatihan, idPelatihan, kuota, biaya, tglMulai, tglBerakhir,
            tglPelaksanaan, lokasi
        )
    }

    private fun getPostUpdateDaftarPelatihan(){
        viewModel.getUpdateDaftarPelatihan().observe(this@AdminDaftarPelatihanActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminDaftarPelatihanActivity)
                is UIState.Success-> setSuccessUpdateDaftarPelatihan(result.data)
                is UIState.Failure-> setFailureUpdateDaftarPelatihan(result.message)
            }
        }
    }

    private fun setFailureUpdateDaftarPelatihan(message: String) {
        Toast.makeText(this@AdminDaftarPelatihanActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessUpdateDaftarPelatihan(data: ResponseModel?) {
        loading.alertDialogCancel()
        if(data != null){
            if(data.status == "0"){
                fetchPelatihan()
            } else{
                Toast.makeText(this@AdminDaftarPelatihanActivity, data.message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminDaftarPelatihanActivity, "Gagal: Ada kesalahan di API", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setStartShimmer(){
        binding.apply {
            smPelatihan.startShimmer()
            smPelatihan.visibility = View.VISIBLE
            hsPelatihan.visibility = View.GONE
        }
    }

    private fun setStopShimmer(){
        binding.apply {
            smPelatihan.stopShimmer()
            smPelatihan.visibility = View.GONE
            hsPelatihan.visibility = View.VISIBLE
        }
    }

    //Permission
    private fun checkPermission(): Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            //Android is 11(R) or above
            Environment.isExternalStorageManager()
        }
        else{
            //Android is below 11(R)
            val write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun getNameFile(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        val nameIndex = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor?.moveToFirst()
        val name = cursor?.getString(nameIndex!!)
        cursor?.close()
        return name!!
    }

    @SuppressLint("Recycle")
    private fun uploadImageToStorage(pdfUri: Uri?, pdfFileName: String, nameAPI:String): MultipartBody.Part? {
        var pdfPart : MultipartBody.Part? = null
        pdfUri?.let {
            val file = contentResolver.openInputStream(pdfUri)?.readBytes()

            if (file != null) {
//                // Membuat objek RequestBody dari file PDF
//                val requestFile = file.toRequestBody("application/pdf".toMediaTypeOrNull())
//                // Membuat objek MultipartBody.Part untuk file PDF
//                pdfPart = MultipartBody.Part.createFormData("materi_pdf", pdfFileName, requestFile)

                pdfPart = convertFileToMultipartBody(file, pdfFileName, nameAPI)
            }
        }
        return pdfPart
    }

    private fun convertFileToMultipartBody(file: ByteArray, pdfFileName: String, nameAPI:String): MultipartBody.Part?{
        val requestFile = file.toRequestBody("application/pdf".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData(nameAPI, pdfFileName, requestFile)

        return filePart
    }

    private fun requestPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            if (Environment.isExternalStorageManager()) {
                startActivity(Intent(this, AdminPelatihanActivity::class.java))
            } else { //request for the permission
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        } else{
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                Constant.STORAGE_PERMISSION_CODE
            )
        }
    }

    private fun pickImageFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/*"
        }

        startActivityForResult(intent, Constant.STORAGE_PERMISSION_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constant.STORAGE_PERMISSION_CODE && resultCode == Activity.RESULT_OK && data != null) {
            // Mendapatkan URI file PDF yang dipilih
            val fileUri = data.data!!

            val nameImage = getNameFile(fileUri)

            tempView?.let {
                it.etEditSertifikat.text = nameImage
            }

            // Mengirim file PDF ke website menggunakan Retrofit
            fileImage = uploadImageToStorage(fileUri, nameImage, "gambar")
        }
    }

    private fun convertStringToMultipartBody(data: String): RequestBody {
        return RequestBody.create("multipart/form-data".toMediaTypeOrNull(), data)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@AdminDaftarPelatihanActivity, AdminMainActivity::class.java))
        finish()
    }
}