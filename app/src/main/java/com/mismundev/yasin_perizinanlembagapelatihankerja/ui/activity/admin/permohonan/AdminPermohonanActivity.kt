package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.permohonan

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
import com.mismundev.yasin_perizinanlembagapelatihankerja.adapter.admin.AdminPermohonanAdapter
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.DaftarPelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.PermohonanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.ResponseModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.UsersModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ActivityAdminPermohonanBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.AlertDialogKeteranganBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.AlertDialogPermohonanBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.AlertDialogShowImageBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.main.AdminMainActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.pdf.PdfActivity
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
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class AdminPermohonanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminPermohonanBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    private val viewModel: AdminPermohonanViewModel by viewModels()
    @Inject
    lateinit var loading: LoadingAlertDialog
    private lateinit var adapter: AdminPermohonanAdapter

    // arrayList
    private var arrayUser : ArrayList<String> = arrayListOf()
    private var arrayIdUser : ArrayList<Int> = arrayListOf()
    private var arrayDaftarPelatihan : ArrayList<String> = arrayListOf()
    private var arrayIdDaftarPelatihan : ArrayList<Int> = arrayListOf()

    private var file: MultipartBody.Part? = null
    private var tempView: AlertDialogPermohonanBinding? = null

    private val tanggalDanWaktu = TanggalDanWaktu()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminPermohonanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        setButton()
        setSharedPreferencesLogin()
        fetchAllData()
        getPermohonan()
        getUser()
        getDaftarPelatihan()
        getPostTambahData()
        getPostUpdateData()
    }

    private fun setSharedPreferencesLogin() {
        sharedPreferencesLogin = SharedPreferencesLogin((this@AdminPermohonanActivity))
    }

    @SuppressLint("SetTextI18n")
    private fun setKontrolNavigationDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminPermohonanActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, myAppBar.ivNavDrawer, this@AdminPermohonanActivity)

            myAppBar.tvTitle.text = "Permohonan Pelatihan"
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
//        val view = AlertDialogPermohonanBinding.inflate(layoutInflater)
//
//        val alertDialog = AlertDialog.Builder(this@AdminPermohonanActivity)
//        alertDialog.setView(view.root)
//            .setCancelable(true)
//        val dialogInputan = alertDialog.create()
//        dialogInputan.show()
//
//        view.apply {
//            tVTitle.text = "Tambah Jenis Pelatihan"
//            btnSimpan.setOnClickListener {
//                var cek = false
//                if (etEditPermohonan.toString().isEmpty()) {
//                    etEditPermohonan.error = "Tidak Boleh Kosong"
//                    cek = true
//                }
//
//                if (!cek) {
//                    postTambahDataPermohonan(
//                        etEditPermohonan.text.toString()
//                    )
//
//                    dialogInputan.dismiss()
//                }
//            }
//
//            btnBatal.setOnClickListener {
//                dialogInputan.dismiss()
//            }
//        }
    }

    private fun postTambahDataPermohonan(jenisPelatihan: String){
//        viewModel.postTambahPermohonan(
//            jenisPelatihan
//        )
    }

    private fun getPostTambahData(){
        viewModel.getTambahPermohonan().observe(this@AdminPermohonanActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminPermohonanActivity)
                is UIState.Success-> setSuccessTambahData(result.data)
                is UIState.Failure-> setFailureTambahData(result.message)
            }
        }
    }

    private fun setFailureTambahData(message: String) {
        Toast.makeText(this@AdminPermohonanActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessTambahData(data: ResponseModel?) {
        if(data != null){
            if(data.status == "0"){
                viewModel.fetchPermohonan()
            } else{
                Toast.makeText(this@AdminPermohonanActivity, data.message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminPermohonanActivity, "Gagal: Ada kesalahan di API", Toast.LENGTH_SHORT).show()
        }
        loading.alertDialogCancel()
    }

    private fun fetchAllData() {
        viewModel.allData()
    }

    private fun getUser(){
        viewModel.getUser().observe(this@AdminPermohonanActivity){result->
            when(result){
                is UIState.Loading-> setStartShimmer()
                is UIState.Failure-> setFailureFetchUser(result.message)
                is UIState.Success-> setSuccessFetchUser(result.data)
            }
        }
    }

    private fun setFailureFetchUser(message: String) {
        setStopShimmer()
        Toast.makeText(this@AdminPermohonanActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessFetchUser(data: ArrayList<UsersModel>) {
        setStopShimmer()
        if(data.isNotEmpty()){
            data.forEach{ user->
                user.nama?.let { arrayUser.add(it) }
                user.idUser?.let { arrayIdUser.add(it) }
            }
        } else{
            Toast.makeText(this@AdminPermohonanActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getDaftarPelatihan(){
        viewModel.getDaftarPelatihan().observe(this@AdminPermohonanActivity){result->
            when(result){
                is UIState.Loading-> setStartShimmer()
                is UIState.Failure-> setFailureFetchDaftarPelatihan(result.message)
                is UIState.Success-> setSuccessFetchDaftarPelatihan(result.data)
            }
        }
    }

    private fun setFailureFetchDaftarPelatihan(message: String) {
        setStopShimmer()
        Toast.makeText(this@AdminPermohonanActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessFetchDaftarPelatihan(data: ArrayList<DaftarPelatihanModel>) {
        setStopShimmer()
        if(data.isNotEmpty()){
            data.forEach{ daftarPelatihan ->
                daftarPelatihan.pelatihanModel?.let { pelatihan->
                    pelatihan.namaPelatihan?.let {
                        val daftar = it+" - "+daftarPelatihan.batch
                        arrayDaftarPelatihan.add(daftar)
                    }
                }
                daftarPelatihan.idDaftarPelatihan?.let { arrayIdDaftarPelatihan.add(it) }
            }
        } else{
            Toast.makeText(this@AdminPermohonanActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getPermohonan(){
        viewModel.getPermohonan().observe(this@AdminPermohonanActivity){result->
            when(result){
                is UIState.Loading-> setStartShimmer()
                is UIState.Failure-> setFailureFetchPermohonan(result.message)
                is UIState.Success-> setSuccessFetchPermohonan(result.data)
            }
        }
    }

    private fun setFailureFetchPermohonan(message: String) {
        setStopShimmer()
        Toast.makeText(this@AdminPermohonanActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessFetchPermohonan(data: ArrayList<PermohonanModel>) {
        setStopShimmer()
        if(data.isNotEmpty()){
            setAdapterPermohonan(data)
        } else{
            Toast.makeText(this@AdminPermohonanActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapterPermohonan(data: ArrayList<PermohonanModel>) {
        adapter = AdminPermohonanAdapter(data, object : OnClickItem.AdminClickPermohonan{

            override fun clickNamaUser(keterangan: String, title: String) {
                showKeterangan(title, keterangan)
            }

            override fun clickNamaPelatihan(keterangan: String, title: String) {
                showKeterangan(title, keterangan)
            }

            override fun clickJenisDokumen(keterangan: String, title: String) {
                showKeterangan(title, keterangan)
            }

            override fun clickFile(file: String, ekstensi: String, title: String) {
                if(file.isEmpty()){
                    Toast.makeText(this@AdminPermohonanActivity, "Tidak ada file", Toast.LENGTH_SHORT).show()
                } else{
                    if(ekstensi=="pdf"){
                        val i = Intent(this@AdminPermohonanActivity, PdfActivity::class.java)
                        i.putExtra("file", file)
                        startActivity(i)
                    } else{
                        setShowImage(title, file)
                    }
                }
            }

            override fun clickItemSetting(permohonan: PermohonanModel, it: View) {
                val popupMenu = PopupMenu(this@AdminPermohonanActivity, it)
                popupMenu.inflate(R.menu.popup_edit)
                popupMenu.setOnMenuItemClickListener(object :
                    PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                        when (menuItem!!.itemId) {
                            R.id.edit -> {
                                setShowDialogEdit(permohonan)
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
            rvPermohonan.layoutManager = LinearLayoutManager(this@AdminPermohonanActivity, LinearLayoutManager.VERTICAL, false)
            rvPermohonan.adapter = adapter
        }
    }

    private fun showKeterangan(title:String, keterangan: String) {
        val view = AlertDialogKeteranganBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminPermohonanActivity)
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

    private fun setShowDialogEdit(permohonan: PermohonanModel) {
        val view = AlertDialogPermohonanBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminPermohonanActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        val positionUser = arrayIdUser.indexOf(permohonan.id_user!!.toInt())
        val positionDaftarPelatihan = arrayIdDaftarPelatihan.indexOf(permohonan.id_daftar_pelatihan!!.toInt())

        var idUser = 0
        var idDaftarPelatihan = 0
        var idKeterangan = 0

        val arrayAdapterUser = ArrayAdapter(
            this@AdminPermohonanActivity,
            android.R.layout.simple_spinner_item,
            arrayUser
        )
        arrayAdapterUser.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val arrayAdapterDaftarPelatihan = ArrayAdapter(
            this@AdminPermohonanActivity,
            android.R.layout.simple_spinner_item,
            arrayDaftarPelatihan
        )
        arrayAdapterDaftarPelatihan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val arrayAdapterKeterangan = ArrayAdapter.createFromResource(
            this@AdminPermohonanActivity,
            R.array.ket_selesai,
            android.R.layout.simple_spinner_item,
        )
        arrayAdapterKeterangan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        tempView = view
        view.apply {
            etEditTanggal.text = permohonan.tanggal
            etEditWaktu.text = permohonan.waktu
            etEditCatatan.setText(permohonan.catatan)

            spUser.adapter = arrayAdapterUser
            spDaftarPelatihan.adapter = arrayAdapterDaftarPelatihan
            spKeterangan.adapter = arrayAdapterKeterangan

            spUser.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    idUser = arrayIdUser[position]
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
            spUser.setSelection(positionUser)

            spDaftarPelatihan.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    idDaftarPelatihan = arrayIdDaftarPelatihan[position]
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
            spDaftarPelatihan.setSelection(positionDaftarPelatihan)

            spKeterangan.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    idKeterangan = position
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
            spKeterangan.setSelection(permohonan.ket!!.toInt())

            etEditTanggal.setOnClickListener {
                tanggalDanWaktu.selectedDate(
                    etEditTanggal.text.toString(),
                    etEditTanggal,
                    this@AdminPermohonanActivity
                )
            }
            etEditWaktu.setOnClickListener {
                tanggalDanWaktu.selectedTime(
                    etEditWaktu.text.toString(),
                    etEditWaktu,
                    this@AdminPermohonanActivity
                )
            }

            etEditSertifikat.setOnClickListener {
                if(checkPermission()){
                    pickFile()
                } else{
                    requestPermission()
                }
            }

            btnSimpan.setOnClickListener {
                var cek = false
                if (etEditTanggal.toString().isEmpty()) {
                    etEditTanggal.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if (etEditWaktu.toString().isEmpty()) {
                    etEditWaktu.error = "Tidak Boleh Kosong"
                    cek = true
                }

                if (!cek) {
                    val idPermohonan = permohonan.id_permohonan!!
                    val tanggal = etEditTanggal.text.toString()
                    val waktu = etEditWaktu.text.toString()
                    val catatan = etEditCatatan.text.toString()
                    if(etEditSertifikat.text == resources.getString(R.string.ket_klik_file)){
                        postUpdateDataPermohonan(
                            idPermohonan, idUser, idDaftarPelatihan, tanggal, waktu, idKeterangan, catatan
                        )
                    } else{
                        postUpdateDataPermohonanImage(
                            idPermohonan, idUser, idDaftarPelatihan, tanggal, waktu, idKeterangan, catatan
                        )
                    }

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
        idUser: Int,
        idDaftarPelatihan: Int,
        tanggal: String,
        waktu: String,
        idKeterangan: Int,
        catatan: String,
    ){
        viewModel.postUpdatePermohonan(
            idPermohonan, idUser, idDaftarPelatihan, tanggal, waktu, idKeterangan, catatan
        )
    }

    private fun postUpdateDataPermohonanImage(
        idPermohonan: Int,
        idUser: Int,
        idDaftarPelatihan: Int,
        tanggal: String,
        waktu: String,
        idKeterangan: Int,
        catatan: String,
    ){
        viewModel.postUpdatePermohonanImage(
            convertStringToMultipartBody(""),
            convertStringToMultipartBody(idPermohonan.toString()),
            convertStringToMultipartBody(idUser.toString()),
            convertStringToMultipartBody(idDaftarPelatihan.toString()),
            convertStringToMultipartBody(tanggal),
            convertStringToMultipartBody(waktu),
            convertStringToMultipartBody(idKeterangan.toString()),
            convertStringToMultipartBody(catatan),
            file!!
        )
    }

    private fun getPostUpdateData(){
        viewModel.getUpdatePermohonan().observe(this@AdminPermohonanActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminPermohonanActivity)
                is UIState.Success-> setSuccessUpdateData(result.data)
                is UIState.Failure-> setFailureUpdateData(result.message)
            }
        }
    }

    private fun setFailureUpdateData(message: String) {
        Toast.makeText(this@AdminPermohonanActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessUpdateData(data: ResponseModel?) {
        if(data != null){
            if(data.status == "0"){
                viewModel.fetchPermohonan()
            } else{
                Toast.makeText(this@AdminPermohonanActivity, data.message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminPermohonanActivity, "Gagal: Ada kesalahan di API", Toast.LENGTH_SHORT).show()
        }
        loading.alertDialogCancel()
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
                startActivity(Intent(this, AdminPermohonanActivity::class.java))
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

    private fun pickFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("application/pdf", "image/*"))
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
            file = uploadImageToStorage(fileUri, nameImage, "file")
        }
    }

    private fun convertStringToMultipartBody(data: String): RequestBody {
        return RequestBody.create("multipart/form-data".toMediaTypeOrNull(), data)
    }

    private fun setShowImage(title:String, gambar: String) {
        val view = AlertDialogShowImageBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminPermohonanActivity)
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

        Glide.with(this@AdminPermohonanActivity)
            .load("${Constant.LOCATION_DOKUMEN}$gambar") // URL Gambar
            .placeholder(R.drawable.loading)
            .error(R.drawable.img_pelatihan)
            .into(view.ivShowImage) // imageView mana yang akan diterapkan

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@AdminPermohonanActivity, AdminMainActivity::class.java))
        finish()
    }
}