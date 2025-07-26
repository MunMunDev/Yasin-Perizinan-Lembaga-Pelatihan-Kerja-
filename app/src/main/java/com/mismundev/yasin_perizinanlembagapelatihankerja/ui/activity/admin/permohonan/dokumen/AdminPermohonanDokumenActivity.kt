package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.permohonan.dokumen

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
import com.mismundev.yasin_perizinanlembagapelatihankerja.adapter.admin.AdminDokumenPermohonanAdapter
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.ResponseModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.DokumenModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.PendaftarModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ActivityAdminPermohonanDokumenBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.AlertDialogKeteranganBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.AlertDialogKonfirmasiBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.AlertDialogPermohonanDokumenBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.AlertDialogShowImageBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.main.AdminMainActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.pdf.PdfActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.Constant
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.LoadingAlertDialog
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.OnClickItem
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.SharedPreferencesLogin
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class AdminPermohonanDokumenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminPermohonanDokumenBinding
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    private val viewModel: AdminPermohonanDokumenViewModel by viewModels()
    @Inject
    lateinit var loading: LoadingAlertDialog
    private lateinit var adapter: AdminDokumenPermohonanAdapter
    private var idPermohonan = 0
    private var idDaftarPelatihan = "0"
    private var file: MultipartBody.Part? = null
    private var tempView: AlertDialogPermohonanDokumenBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminPermohonanDokumenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDataSebelumnya()
        setButton()
        setSharedPreferencesLogin()
        getDokumenPermohonan()
        getPostTambahData()
        getPostUpdateData()
        getPostDeleteData()
    }

    private fun setDataSebelumnya() {
        val i = intent.extras
        if(i != null){
            idPermohonan = i.getInt("id_permohonan", 0)
            idDaftarPelatihan = i.getString("id_daftar_pelatihan")!!
            fetchDokumenPermohonan(idPermohonan)
        }
    }

    private fun setSharedPreferencesLogin() {
        sharedPreferencesLogin = SharedPreferencesLogin((this@AdminPermohonanDokumenActivity))
    }

    private fun setButton() {
        binding.apply {
            myAppBar.apply {
                ivNavDrawer.visibility = View.GONE
                ivBack.visibility = View.VISIBLE
                tvTitle.text = "Dokumen Permohonan"

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
        val view = AlertDialogPermohonanDokumenBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminPermohonanDokumenActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        tempView = view
        view.apply {
            tVTitle.text = "Tambah Dokumen"
            etEditFile.setOnClickListener {
                if(checkPermission()){
                    pickFile()
                } else{
                    requestPermission()
                }
            }
            btnSimpan.setOnClickListener {
                var cek = false
                if (etEditJenisDokumen.text.toString().isEmpty()) {
                    etEditJenisDokumen.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if (etEditFile.text.toString() == resources.getString(R.string.ket_klik_file)) {
                    etEditFile.error = "Tidak Boleh Kosong"
                    cek = true
                }

                if (!cek) {
                    postTambahDataDokumenPermohonan(
                        convertStringToMultipartBody("post"),
                        convertStringToMultipartBody(idPermohonan.toString()),
                        convertStringToMultipartBody(idDaftarPelatihan),
                        convertStringToMultipartBody(etEditJenisDokumen.text.toString()),
                        file!!
                    )

                    dialogInputan.dismiss()
                }
            }

            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postTambahDataDokumenPermohonan(
        post: RequestBody,
        idPermohonan: RequestBody,
        idDaftarPelatihan: RequestBody,
        jenisDokumen: RequestBody,
        file: MultipartBody.Part,
    ){
        viewModel.postTambahDokumenPermohonan(
            post, idPermohonan, idDaftarPelatihan, jenisDokumen, file
        )
    }

    private fun getPostTambahData(){
        viewModel.getTambahPermohonan().observe(this@AdminPermohonanDokumenActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminPermohonanDokumenActivity)
                is UIState.Success-> setSuccessTambahData(result.data)
                is UIState.Failure-> setFailureTambahData(result.message)
                else -> {}
            }
        }
    }

    private fun setFailureTambahData(message: String) {
        Toast.makeText(this@AdminPermohonanDokumenActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessTambahData(data: ResponseModel?) {
        if(data != null){
            if(data.status == "0"){
                fetchDokumenPermohonan(idPermohonan)
            } else{
                Toast.makeText(this@AdminPermohonanDokumenActivity, data.message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminPermohonanDokumenActivity, "Gagal: Ada kesalahan di API", Toast.LENGTH_SHORT).show()
        }
        loading.alertDialogCancel()
    }

    private fun fetchDokumenPermohonan(idPermohonan: Int) {
        viewModel.fetchDokumenPermohonan(idPermohonan)
    }

    private fun getDokumenPermohonan(){
        viewModel.getDokumenPermohonan().observe(this@AdminPermohonanDokumenActivity){result->
            when(result){
                is UIState.Loading-> setStartShimmer()
                is UIState.Failure-> setFailureFetchDokumenPermohonan(result.message)
                is UIState.Success-> setSuccessFetchDokumenPermohonan(result.data)
                else -> {}
            }
        }
    }

    private fun setFailureFetchDokumenPermohonan(message: String) {
        setStopShimmer()
        Toast.makeText(this@AdminPermohonanDokumenActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessFetchDokumenPermohonan(data: ArrayList<DokumenModel>) {
        setStopShimmer()
        if(data.isNotEmpty()){
            setAdapterPermohonan(data)
        } else{
            Toast.makeText(this@AdminPermohonanDokumenActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapterPermohonan(data: ArrayList<DokumenModel>) {
        adapter = AdminDokumenPermohonanAdapter(data, object : OnClickItem.AdminClickDokumenPermohonan{
            override fun clickJenisDokumen(keterangan: String, title: String) {
                showKeterangan(title, keterangan)
            }

            override fun clickFile(file: String, ekstensi: String, title: String) {
                if(ekstensi=="pdf"){
                    val i = Intent(this@AdminPermohonanDokumenActivity, PdfActivity::class.java)
                    i.putExtra("file", file)
                    startActivity(i)
                } else{
                    setShowImage(title, file)
                }
            }

            override fun clickItemSetting(dokumen: DokumenModel, it: View) {
                val popupMenu = PopupMenu(this@AdminPermohonanDokumenActivity, it)
                popupMenu.inflate(R.menu.popup_edit_hapus)
                popupMenu.setOnMenuItemClickListener(object :
                    PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                        when (menuItem!!.itemId) {
                            R.id.edit -> {
                                setShowDialogEdit(dokumen)
                                return true
                            }
                            R.id.hapus -> {
                                setShowDialogHapus(dokumen)
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
            rvPermohonan.layoutManager = LinearLayoutManager(this@AdminPermohonanDokumenActivity, LinearLayoutManager.VERTICAL, false)
            rvPermohonan.adapter = adapter
        }
    }

    private fun showKeterangan(title:String, keterangan: String) {
        val view = AlertDialogKeteranganBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminPermohonanDokumenActivity)
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

    private fun setShowDialogEdit(dokumen: DokumenModel) {
        val view = AlertDialogPermohonanDokumenBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminPermohonanDokumenActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        tempView = view
        view.apply {
            etEditFile.setOnClickListener {
                if(checkPermission()){
                    pickFile()
                } else{
                    requestPermission()
                }
            }
            etEditJenisDokumen.setText(dokumen.jenis_dokumen)
            btnSimpan.setOnClickListener {
                var cek = false
                if (etEditJenisDokumen.text.toString().isEmpty()) {
                    etEditJenisDokumen.error = "Tidak Boleh Kosong"
                    cek = true
                }

                if (!cek) {
                    val idPermohonan = dokumen.id_dokumen!!
                    val jenisDokumen = etEditJenisDokumen.text.toString()
                    if(etEditFile.text.toString() != resources.getString(R.string.ket_klik_file)){
                        postUpdateDataPermohonanImage(
                            convertStringToMultipartBody("post"),
                            convertStringToMultipartBody(idPermohonan.toString()),
                            convertStringToMultipartBody(jenisDokumen),
                            file!!
                        )
                    } else{
                        postUpdateDataPermohonan(
                            idPermohonan, jenisDokumen
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
        jenisDokumen: String,
    ){
        viewModel.postUpdateDokumenPermohonan(
            idPermohonan, jenisDokumen
        )
    }

    private fun postUpdateDataPermohonanImage(
        post: RequestBody,
        idPermohonan: RequestBody,
        jenisDokumen: RequestBody,
        file: MultipartBody.Part,
    ){
        viewModel.postUpdateDokumenPermohonanImage(
            post, idPermohonan, jenisDokumen, file
        )
    }

    private fun getPostUpdateData(){
        viewModel.getUpdatePermohonan().observe(this@AdminPermohonanDokumenActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminPermohonanDokumenActivity)
                is UIState.Success-> setSuccessUpdateData(result.data)
                is UIState.Failure-> setFailureUpdateData(result.message)
                else -> {}
            }
        }
    }

    private fun setFailureUpdateData(message: String) {
        Toast.makeText(this@AdminPermohonanDokumenActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessUpdateData(data: ResponseModel?) {
        if(data != null){
            if(data.status == "0"){
                viewModel.fetchDokumenPermohonan(idPermohonan)
            } else{
                Toast.makeText(this@AdminPermohonanDokumenActivity, data.message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminPermohonanDokumenActivity, "Gagal: Ada kesalahan di API", Toast.LENGTH_SHORT).show()
        }
        loading.alertDialogCancel()
    }

    private fun setShowDialogHapus(dokumen: DokumenModel) {
        val view = AlertDialogKonfirmasiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminPermohonanDokumenActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKonfirmasi.text = "Yakin Hapus Data Ini ?"
            tvBodyKonfirmasi.text = "Data akan terhapus dan tidak dapat dikembalikan"

            btnKonfirmasi.setOnClickListener {
                postDeleteData(dokumen.id_dokumen!!)
                dialogInputan.dismiss()
            }

            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postDeleteData(idDokumen: Int){
        viewModel.postDeleteDokumenPermohonan(idDokumen)
    }

    private fun getPostDeleteData(){
        viewModel.getDeletePermohonan().observe(this@AdminPermohonanDokumenActivity){result->
            when(result){
                is UIState.Loading-> setStartShimmer()
                is UIState.Success-> setSuccessDeleteData(result.data)
                is UIState.Failure-> setFailureDeleteData(result.message)
                else -> {}
            }
        }
    }

    private fun setFailureDeleteData(message: String) {
        Toast.makeText(this@AdminPermohonanDokumenActivity, message, Toast.LENGTH_SHORT).show()
        setStopShimmer()
    }

    private fun setSuccessDeleteData(data: ResponseModel?) {
        if(data != null){
            if(data.status == "0"){
                fetchDokumenPermohonan(idPermohonan)
            } else{
                Toast.makeText(this@AdminPermohonanDokumenActivity, data.message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminPermohonanDokumenActivity, "Gagal: Ada kesalahan di API", Toast.LENGTH_SHORT).show()
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
                startActivity(Intent(this, AdminPermohonanDokumenActivity::class.java))
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
                it.etEditFile.text = nameImage
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

        val alertDialog = AlertDialog.Builder(this@AdminPermohonanDokumenActivity)
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

        Glide.with(this@AdminPermohonanDokumenActivity)
            .load("${Constant.LOCATION_DOKUMEN}$gambar") // URL Gambar
            .placeholder(R.drawable.loading)
            .error(R.drawable.img_pelatihan)
            .into(view.ivShowImage) // imageView mana yang akan diterapkan

    }

}