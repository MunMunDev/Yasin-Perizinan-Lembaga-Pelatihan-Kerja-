package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.user.permohonan

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
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.mismundev.yasin_perizinanlembagapelatihankerja.R
import com.mismundev.yasin_perizinanlembagapelatihankerja.adapter.user.DokumenPermohonanAdapter
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.DokumenModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.ResponseModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ActivityDetailPermohonanBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.AlertDialogPermohonanDokumenBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.AlertDialogShowImageBinding
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

@AndroidEntryPoint
class DetailPermohonanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPermohonanBinding
    val viewModel: DetailPermohonanViewModel by viewModels()
    private var file: MultipartBody.Part? = null
    private var tempView: AlertDialogPermohonanDokumenBinding? = null
    private lateinit var sharedPreferences: SharedPreferencesLogin
    private var idPermohonan = 0
    private var ket = ""
    private var loading = LoadingAlertDialog()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPermohonanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSharedPreferences()
        setDataSebelumnya()
        setButton()
        getDokumenPermohonan()
        getPostUpdateData()
    }
    private fun setSharedPreferences() {
        sharedPreferences = SharedPreferencesLogin(this@DetailPermohonanActivity)
    }

    private fun setDataSebelumnya() {
        val i = intent.extras
        if(i != null){
            idPermohonan = i.getInt("id_permohonan", 0)
            ket = i.getString("ket", "")
            fetchDokumenPermohonan(idPermohonan, sharedPreferences.getIdUser())
        }
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

        }
    }

    private fun fetchDokumenPermohonan(idPermohonan: Int, idUser:Int) {
        viewModel.fetchDokumenPermohonan(idPermohonan, idUser)
    }

    private fun getDokumenPermohonan(){
        viewModel.getDokumenPermohonan().observe(this@DetailPermohonanActivity){result->
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
        Toast.makeText(this@DetailPermohonanActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessFetchDokumenPermohonan(data: ArrayList<DokumenModel>) {
        setStopShimmer()
        if(data.isNotEmpty()){
            setAdapterPermohonan(data)
        } else{
            Toast.makeText(this@DetailPermohonanActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapterPermohonan(data: ArrayList<DokumenModel>) {
        val adapter = DokumenPermohonanAdapter(data, object : OnClickItem.ClickDokumenPermohonan{

            override fun clickFile(file: String, jenisDokumen: String, ekstensi: String) {
                if(ekstensi == "pdf"){
                    val i = Intent(this@DetailPermohonanActivity, PdfActivity::class.java)
                    i.putExtra("file", file)
                    startActivity(i)
                } else{
                    setShowImage("", file)
                }
            }

            override fun clickUpdate(dokumen: DokumenModel) {
                setShowDialogEdit(dokumen)
            }
        }, ket)
        binding.apply {
            rvDokumen.layoutManager = GridLayoutManager(this@DetailPermohonanActivity, 2)
            rvDokumen.adapter = adapter
        }
    }

    private fun setShowDialogEdit(dokumen: DokumenModel) {
        val view = AlertDialogPermohonanDokumenBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@DetailPermohonanActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        tempView = view
        view.apply {
            etEditFile.setOnClickListener {
                if(checkPermission()){
                    pickFile(dokumen.ekstensi!!)
                } else{
                    requestPermission()
                }
            }
            etEditJenisDokumen.setText(dokumen.jenis_dokumen)
            etEditJenisDokumen.isEnabled = false
            btnSimpan.setOnClickListener {
                var cek = false
                if (etEditFile.text.toString() == resources.getString(R.string.ket_klik_file)) {
                    etEditFile.error = "Tidak Boleh Kosong"
                    cek = true
                }

                if (!cek) {
                    val idDokumen = dokumen.id_dokumen!!
                    postUpdateDataPermohonan(
                        convertStringToMultipartBody("post"),
                        convertStringToMultipartBody(idDokumen.toString()),
                        file!!
                    )
                }
                dialogInputan.dismiss()
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postUpdateDataPermohonan(
        post: RequestBody,
        idDokumen: RequestBody,
        file: MultipartBody.Part,
    ){
        viewModel.postUpdateDokumenPermohonanFile(
            post, idDokumen, file
        )
    }

    private fun getPostUpdateData(){
        viewModel.getUpdatePermohonan().observe(this@DetailPermohonanActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@DetailPermohonanActivity)
                is UIState.Success-> setSuccessUpdateData(result.data)
                is UIState.Failure-> setFailureUpdateData(result.message)
                else -> {}
            }
        }
    }

    private fun setFailureUpdateData(message: String) {
        Toast.makeText(this@DetailPermohonanActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessUpdateData(data: ResponseModel?) {
        if(data != null){
            if(data.status == "0"){
                Toast.makeText(this@DetailPermohonanActivity, "Berhasil Update", Toast.LENGTH_SHORT).show()
                viewModel.fetchDokumenPermohonan(idPermohonan, sharedPreferences.getIdUser())
            } else{
                Toast.makeText(this@DetailPermohonanActivity, data.message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@DetailPermohonanActivity, "Gagal: Ada kesalahan di API", Toast.LENGTH_SHORT).show()
        }
        loading.alertDialogCancel()
    }

    private fun setStartShimmer(){
        binding.apply {
            smDokumen.startShimmer()
            smDokumen.visibility = View.VISIBLE
            rvDokumen.visibility = View.GONE
        }
    }

    private fun setStopShimmer(){
        binding.apply {
            smDokumen.stopShimmer()
            smDokumen.visibility = View.GONE
            rvDokumen.visibility = View.VISIBLE
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
                startActivity(Intent(this@DetailPermohonanActivity, DetailPermohonanActivity::class.java))
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

    private fun pickFile(ekstensi: String) {
        if(ekstensi.trim().lowercase() == "image"){
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "image/*"
            }

            startActivityForResult(intent, Constant.STORAGE_PERMISSION_CODE)
        } else{
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "application/pdf"
            }

            startActivityForResult(intent, Constant.STORAGE_PERMISSION_CODE)
        }
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

        val alertDialog = AlertDialog.Builder(this@DetailPermohonanActivity)
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

        Glide.with(this@DetailPermohonanActivity)
            .load("${Constant.LOCATION_DOKUMEN}$gambar") // URL Gambar
            .placeholder(R.drawable.loading)
            .error(R.drawable.img_pelatihan)
            .into(view.ivShowImage) // imageView mana yang akan diterapkan

    }
}