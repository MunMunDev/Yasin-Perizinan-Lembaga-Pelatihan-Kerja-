package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.pelatihan

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
import com.mismundev.yasin_perizinanlembagapelatihankerja.adapter.admin.AdminPelatihanAdapter
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.JenisPelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.PelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.ResponseModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ActivityAdminPelatihanBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.AlertDialogPelatihanBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.AlertDialogKeteranganBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.AlertDialogShowImageBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.main.AdminMainActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.Constant
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.KontrolNavigationDrawer
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.LoadingAlertDialog
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.OnClickItem
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.SharedPreferencesLogin
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.ArrayList
import javax.inject.Inject

@AndroidEntryPoint
class AdminPelatihanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminPelatihanBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    private val viewModel: AdminPelatihanViewModel by viewModels()
    @Inject
    lateinit var loading: LoadingAlertDialog
    private lateinit var adapter: AdminPelatihanAdapter
    private var fileImage: MultipartBody.Part? = null
    private var tempView: AlertDialogPelatihanBinding? = null
    private var arrayJenisPelatihan: ArrayList<JenisPelatihanModel>? = arrayListOf()
    private var listJenisPelatihan: ArrayList<String>? = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminPelatihanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        setButton()
        setSharedPreferencesLogin()
        fetchJenisPelatihan()
        getJenisPelatihan()
        fetchPelatihan()
        getPelatihan()
        getPostTambahData()
        getPostUpdatePelatihan()
    }

    private fun setSharedPreferencesLogin() {
        sharedPreferencesLogin = SharedPreferencesLogin((this@AdminPelatihanActivity))
    }

    @SuppressLint("SetTextI18n")
    private fun setKontrolNavigationDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminPelatihanActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, myAppBar.ivNavDrawer, this@AdminPelatihanActivity)

            myAppBar.tvTitle.text =  "Pelatiihan"
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
        val view = AlertDialogPelatihanBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminPelatihanActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tempView = view
            var selectedItem = 0
            tVTitle.text = "Tambah Pelatihan"
            etEditGambar.setOnClickListener {
                if(checkPermission()){
                    pickImageFile()
                } else{
                    requestPermission()
                }
            }
            val arrayAdapterKotaKab = ArrayAdapter(
                this@AdminPelatihanActivity,
                android.R.layout.simple_spinner_item,
                listJenisPelatihan!!
            )

            arrayAdapterKotaKab.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spJenisPelatihan.adapter = arrayAdapterKotaKab

            spJenisPelatihan.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
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
            btnSimpan.setOnClickListener {
                var cek = false
                if (etEditNamaPelatihan.toString().isEmpty()) {
                    etEditNamaPelatihan.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if (etEditDeskripsi.toString().isEmpty()) {
                    etEditDeskripsi.error = "Tidak Boleh Kosong"
                    cek = true
                }

                if (!cek) {
                    if (etEditGambar.text.toString() == resources.getString(R.string.ket_klik_file)) {
                        // ketika tidak ada gambar masuk
                        Toast.makeText(this@AdminPelatihanActivity, "Masukkan gambar", Toast.LENGTH_SHORT).show()
                    } else{
                        // ada gambar
                        postTambahDataPelatihan(
                            arrayJenisPelatihan!![selectedItem].idJenisPelatihan!!,
                            etEditNamaPelatihan.text.toString(),
                            etEditDeskripsi.text.toString(),
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

    private fun postTambahDataPelatihan(
        idJenisPelatihan: Int, namaPelatihan: String, deskripsi: String
    ){
        viewModel.postTambahPelatihan(
            convertStringToMultipartBody("post_add_pelatihan"),
            convertStringToMultipartBody("$idJenisPelatihan"),
            convertStringToMultipartBody(namaPelatihan),
            convertStringToMultipartBody(deskripsi),
            fileImage!!
        )
    }

    private fun getPostTambahData(){
        viewModel.getTambahPelatihan().observe(this@AdminPelatihanActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminPelatihanActivity)
                is UIState.Success-> setSuccessTambahData(result.data)
                is UIState.Failure-> setFailureTambahData(result.message)
            }
        }
    }

    private fun setFailureTambahData(message: String) {
        Toast.makeText(this@AdminPelatihanActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessTambahData(data: ResponseModel?) {
        loading.alertDialogCancel()
        if(data != null){
            if(data.status == "0"){
                fetchPelatihan()
            } else{
                Toast.makeText(this@AdminPelatihanActivity, data.message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminPelatihanActivity, "Gagal: Ada kesalahan di API", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchJenisPelatihan() {
        viewModel.fetchJenisPelatihan()
    }

    private fun getJenisPelatihan(){
        viewModel.getJenisPelatihan().observe(this@AdminPelatihanActivity){result->
            when(result){
                is UIState.Loading-> {}
                is UIState.Failure-> setFailureFetchJenisPelatihan(result.message)
                is UIState.Success-> setSuccessFetchJenisPelatihan(result.data)
            }
        }
    }

    private fun setFailureFetchJenisPelatihan(message: String) {
//        setStopShimmer()
//        Toast.makeText(this@AdminPelatihanActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessFetchJenisPelatihan(data: ArrayList<JenisPelatihanModel>) {
//        setStopShimmer()
        if(data.isNotEmpty()){
            arrayJenisPelatihan = data
            for(value in data){
                listJenisPelatihan!!.add(value.jenisPelatihan!!)
            }
        } else{
            Toast.makeText(this@AdminPelatihanActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchPelatihan() {
        viewModel.fetchPelatihan()
    }

    private fun getPelatihan(){
        viewModel.getPelatihan().observe(this@AdminPelatihanActivity){result->
            when(result){
                is UIState.Loading-> setStartShimmer()
                is UIState.Failure-> setFailureFetchPelatihan(result.message)
                is UIState.Success-> setSuccessFetchPelatihan(result.data)
            }
        }
    }

    private fun setFailureFetchPelatihan(message: String) {
        setStopShimmer()
        Toast.makeText(this@AdminPelatihanActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessFetchPelatihan(data: ArrayList<PelatihanModel>) {
        setStopShimmer()
        if(data.isNotEmpty()){
            setAdapterPelatihan(data)
        } else{
            Toast.makeText(this@AdminPelatihanActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapterPelatihan(data: ArrayList<PelatihanModel>) {
        adapter = AdminPelatihanAdapter(data, object : OnClickItem.AdminClickPelatihan{

            override fun clickJenisPelatihan(jenisPelatihan: String, title: String) {
                showKeterangan(title, jenisPelatihan)
            }

            override fun clickNamaPelatihan(namaPelatihan: String, title: String) {
                showKeterangan(title, namaPelatihan)
            }

            override fun clickDeskripsi(deskripsi: String, title: String) {
                showKeterangan(title, deskripsi)
            }

            override fun clickGambar(gambar: String, title: String) {
                setShowImage(title, gambar)
            }

            override fun clickItemSetting(pelatihan: PelatihanModel, it: View) {
                val popupMenu = PopupMenu(this@AdminPelatihanActivity, it)
                popupMenu.inflate(R.menu.popup_edit)
                popupMenu.setOnMenuItemClickListener(object :
                    PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                        when (menuItem!!.itemId) {
                            R.id.edit -> {
                                setShowDialogEdit(pelatihan)
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
            rvPelatihan.layoutManager = LinearLayoutManager(this@AdminPelatihanActivity, LinearLayoutManager.VERTICAL, false)
            rvPelatihan.adapter = adapter
        }
    }

    private fun showKeterangan(title:String, keterangan: String) {
        val view = AlertDialogKeteranganBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminPelatihanActivity)
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

        val alertDialog = AlertDialog.Builder(this@AdminPelatihanActivity)
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

        Glide.with(this@AdminPelatihanActivity)
            .load("${Constant.LOCATION_GAMBAR}$gambar") // URL Gambar
            .placeholder(R.drawable.loading)
            .error(R.drawable.img_pelatihan)
            .into(view.ivShowImage) // imageView mana yang akan diterapkan

    }


    private fun setShowDialogEdit(pelatihan: PelatihanModel) {
        val view = AlertDialogPelatihanBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminPelatihanActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tempView = view
            tVTitle.text = "Edit Pelatihan"
            etEditNamaPelatihan.setText(pelatihan.namaPelatihan)
            etEditDeskripsi.setText(pelatihan.deskripsi)

            etEditGambar.setOnClickListener {
                if(checkPermission()){
                    pickImageFile()
                } else{
                    requestPermission()
                }
            }

            var selectedItem = 0
            val arrayAdapterKotaKab = ArrayAdapter(
                this@AdminPelatihanActivity,
                android.R.layout.simple_spinner_item,
                listJenisPelatihan!!
            )

            arrayAdapterKotaKab.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spJenisPelatihan.adapter = arrayAdapterKotaKab

            spJenisPelatihan.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
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

            for((no, value) in arrayJenisPelatihan!!.withIndex()){
                if(value.idJenisPelatihan!! == pelatihan.jenisPelatihanModel!!.idJenisPelatihan){
                    spJenisPelatihan.setSelection(no)
                    selectedItem = no
                }
            }

            btnSimpan.setOnClickListener {
                var cek = false
                if (etEditNamaPelatihan.toString().isEmpty()) {
                    etEditNamaPelatihan.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if (etEditDeskripsi.toString().isEmpty()) {
                    etEditDeskripsi.error = "Tidak Boleh Kosong"
                    cek = true
                }

                if (!cek) {
                    if (etEditGambar.text.toString() == resources.getString(R.string.ket_klik_file)) {
                        // ketika tidak ada gambar masuk
                        postUpdatePelatihan(
                            pelatihan.idPelatihan!!,
                            arrayJenisPelatihan!![selectedItem].idJenisPelatihan!!,
                            etEditNamaPelatihan.text.toString(),
                            etEditDeskripsi.text.toString()
                        )
                    } else{
                        // ada gambar
                        postUpdatePelatihanAddImage(
                            pelatihan.idPelatihan!!,
                            arrayJenisPelatihan!![selectedItem].idJenisPelatihan!!,
                            etEditNamaPelatihan.text.toString(),
                            etEditDeskripsi.text.toString()
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

    private fun postUpdatePelatihanAddImage(
        idPelatihan: Int, idJenisPelatihan: Int, namaPelatihan: String, deskripsi: String
    ){
        viewModel.postUpdatePelatihanAddImage(
            convertStringToMultipartBody(""),
            convertStringToMultipartBody(idPelatihan.toString()),
            convertStringToMultipartBody(idJenisPelatihan.toString()),
            convertStringToMultipartBody(namaPelatihan),
            convertStringToMultipartBody(deskripsi),
            fileImage!!
        )
    }

    private fun postUpdatePelatihan(
        idPelatihan: Int, idJenisPelatihan: Int, namaPelatihan: String, deskripsi: String
    ){
        viewModel.postUpdatePelatihan(
            idPelatihan, idJenisPelatihan, namaPelatihan, deskripsi
        )
    }

    private fun getPostUpdatePelatihan(){
        viewModel.getUpdatePelatihan().observe(this@AdminPelatihanActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminPelatihanActivity)
                is UIState.Success-> setSuccessUpdatePelatihan(result.data)
                is UIState.Failure-> setFailureUpdatePelatihan(result.message)
            }
        }
    }

    private fun setFailureUpdatePelatihan(message: String) {
        Toast.makeText(this@AdminPelatihanActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessUpdatePelatihan(data: ResponseModel?) {
        loading.alertDialogCancel()
        if(data != null){
            if(data.status == "0"){
                fetchPelatihan()
            } else{
                Toast.makeText(this@AdminPelatihanActivity, data.message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminPelatihanActivity, "Gagal: Ada kesalahan di API", Toast.LENGTH_SHORT).show()
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

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constant.STORAGE_PERMISSION_CODE && resultCode == Activity.RESULT_OK && data != null) {
            // Mendapatkan URI file PDF yang dipilih
            val fileUri = data.data!!

            val nameImage = getNameFile(fileUri)

            tempView?.let {
                it.etEditGambar.text = nameImage
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
        startActivity(Intent(this@AdminPelatihanActivity, AdminMainActivity::class.java))
        finish()
    }
}