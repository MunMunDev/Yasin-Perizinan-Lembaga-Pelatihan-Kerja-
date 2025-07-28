package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.user.pelatihan.detail_pelatihan

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.SearchManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.mismundev.yasin_perizinanlembagapelatihankerja.R
import com.mismundev.yasin_perizinanlembagapelatihankerja.adapter.admin.DokumenPostPermohonanAdapter
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.DaftarPelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.DokumenPostModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.PendaftarModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.PermohonanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.ResponseModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ActivityDetailPelatihanBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.AlertDialogDaftarBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.AlertDialogDokumenPostBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.pelatihan.AdminPelatihanActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.user.payment.PaymentActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.Constant
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.KonversiRupiah
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.OnClickItem
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.SharedPreferencesLogin
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.TanggalDanWaktu
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.Locale
import javax.inject.Inject


@AndroidEntryPoint
class DetailPelatihanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPelatihanBinding
    private val viewModel: DetailPelatihanViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferencesLogin
    private var idPendaftar = 0
    private var idPelatihan = 0
    private var idDaftarPelatihan = 0
    private var namaPelatihan = ""
    private var kuota = 0
    private var tglMulaiPendaftaran = ""
    private var tglBerakhirPendaftaran = ""
    @Inject lateinit var rupiah: KonversiRupiah
    @Inject lateinit var tanggalDanWaktu: TanggalDanWaktu
    private var dataDaftarPelatihan : DaftarPelatihanModel? = null
    private val TAG = "DetailPelatihanActivity"
    private var harga = 0
    private var arrayJenisDokumen: ArrayList<DokumenPostModel> = arrayListOf()
    private var position = 0

    private var tempTvFile: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPelatihanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchDataPreviously()
        setSharedPreferences()
        setButton()
        setTopAppBar()
        fetchTelahDaftarPelatihan(idDaftarPelatihan, sharedPreferences.getIdUser())
        getTelahDaftarPelatihan()
        fetchPelatihan(idDaftarPelatihan)
        getPelatihan()
        getPermohonan()
        getDaftarGratis()
        getPostPermohonan()
        getPostDokumenPermohonan()
        setSwipeRefreshLayout()
    }

    private fun setSwipeRefreshLayout() {
        binding.swipeRefresh.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeRefresh.isRefreshing = false
                fetchTelahDaftarPelatihan(idDaftarPelatihan, sharedPreferences.getIdUser())
                fetchPelatihan(idDaftarPelatihan)
            }, 2000)
        }
    }

    private fun fetchDataPreviously() {
        val extras: Bundle? = intent.extras
        if(extras != null){
            idDaftarPelatihan = extras.getInt("id_daftar_pelatihan", 0)
            namaPelatihan = extras.getString("nama_pelatihan")!!
        }
    }

    private fun setSharedPreferences() {
        sharedPreferences = SharedPreferencesLogin(this@DetailPelatihanActivity)
    }

    private fun setTopAppBar() {
        binding.appNavbarDrawer.apply {
            tvTitle.text = namaPelatihan
            ivNavDrawer.visibility = View.GONE
            ivBack.visibility = View.VISIBLE
        }
    }

    private fun setButton(){
        binding.apply {
            appNavbarDrawer.ivBack.setOnClickListener {
                finish()
            }
            btnDaftar.setOnClickListener {
                Log.d(TAG, "setButton: $tglMulaiPendaftaran, $tglBerakhirPendaftaran")
                val cekMulaiPendaftaran = tanggalDanWaktu.cekTanggalMulaiPendaftaran(tglMulaiPendaftaran)
                val cekBerakhirPendaftaran = tanggalDanWaktu.cekTanggalBerakhirPendaftaran(tglBerakhirPendaftaran)
                Log.d(TAG, "setButton: $cekMulaiPendaftaran, $cekBerakhirPendaftaran")

                if(cekMulaiPendaftaran && cekBerakhirPendaftaran){
                    if(kuota > 0) daftarPelatihan()
                    else if(kuota == 0) Toast.makeText(this@DetailPelatihanActivity, "Kuota Telah Habis", Toast.LENGTH_SHORT).show()
                }
                else if(!cekMulaiPendaftaran) Toast.makeText(this@DetailPelatihanActivity, "Waktu Pendaftaran Belum Dimulai", Toast.LENGTH_SHORT).show()
                else if(!cekBerakhirPendaftaran) Toast.makeText(this@DetailPelatihanActivity, "Waktu Pendaftaran Telah Lewat", Toast.LENGTH_SHORT).show()
            }
            btnCetakSertifikat.setOnClickListener {
                val intent = Intent(Intent.ACTION_WEB_SEARCH)
                intent.putExtra(
                    SearchManager.QUERY,
                    "https://e-portofolio.web.id/pelatihan-kerja/print.php?id_pendaftar=$idPendaftar"
                )
                startActivity(intent)
            }
            btnProsesPermohonan.setOnClickListener {
                Toast.makeText(this@DetailPelatihanActivity, "Silahkan Lihat Status Permohonan Pada menu Permohonan", Toast.LENGTH_SHORT).show()
            }
            btnPermohonan.setOnClickListener {
                setShowBerkasPermohonan(arrayJenisDokumen)
            }
        }
    }

    private fun setShowBerkasPermohonan(dokumen: ArrayList<DokumenPostModel>) {
        val view = AlertDialogDokumenPostBinding.inflate(layoutInflater)
        val alertDialog = AlertDialog.Builder(this@DetailPelatihanActivity)
        alertDialog.apply {
            setView(view.root)
            setCancelable(false)
        }
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            val adapterPermohonan = DokumenPostPermohonanAdapter(dokumen, object: OnClickItem.ClickDokumenPostPermohonan{

                override fun clickFile(tvFile: TextView, position: Int, ekstensi: String) {
                    this@DetailPelatihanActivity.position = position
                    tempTvFile = tvFile

                    if(checkPermission()){
                        if(ekstensi.trim().lowercase(Locale.ROOT) =="pdf"){
                            pickFile()
                        } else{
                            pickImage()
                        }
                    } else{
                        requestPermission()
                    }
                }
            })
            rvDokumen.layoutManager = LinearLayoutManager(this@DetailPelatihanActivity, LinearLayoutManager.VERTICAL, false)
            rvDokumen.adapter = adapterPermohonan

            // Button
            btnSimpan.setOnClickListener {
                var check = false
                var keterangan = "Tolong masukkan file"
                for(value in dokumen){
                    if(value.file==null){
                        check = true
                        keterangan+=value.jenis_dokumen+", "
                    }
                }

                if(check){
                    Toast.makeText(this@DetailPelatihanActivity, keterangan, Toast.LENGTH_SHORT).show()
                } else{
                    dialogInputan.dismiss()
                    postPermohonan(
                        sharedPreferences.getIdUser(),
                        idPelatihan,
                        idDaftarPelatihan
                    )
//                    // post data
//                    val post = convertStringToMultipartBody("")
//                    for(value in arrayJenisDokumen){
//                        // post data satu per satu
//                        val idDaftarPelatihan = convertStringToMultipartBody(idDaftarPelatihan.toString())
//                        val jenisDokumen = convertStringToMultipartBody(value.jenis_dokumen!!)
//                        val ekstensi = convertStringToMultipartBody(value.ekstensi!!)
//                        val file = value.file!!
//                        postDokumenPermohonan(post, idDaftarPelatihan, jenisDokumen, ekstensi, file)
//                    }
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postPermohonan(
        idUser: Int,
        idPelatihan: Int,
        idDaftarPelatihan: Int,
    ) {
        viewModel.postPermohonan(idUser, idPelatihan, idDaftarPelatihan)
    }

    private fun getPostPermohonan(){
        viewModel.getPostPermohonan().observe(this@DetailPelatihanActivity){result->
            when(result){
                is UIState.Loading-> {}
                is UIState.Success-> setSuccessPostPermohonan(result.data)
                is UIState.Failure-> setFailurePostPermohonan(result.message)
            }
        }
    }

    private fun setSuccessPostPermohonan(data: ResponseModel) {
        if(data.status == "0"){
            binding.apply {
                btnTerdaftar.visibility = View.GONE
                btnDaftar.visibility = View.GONE
                btnCetakSertifikat.visibility = View.GONE
                btnPermohonan.visibility = View.GONE
                btnProsesPermohonan.visibility = View.VISIBLE
            }
            val vIdPermohonan = data.message_response!!
            val post = convertStringToMultipartBody("")
            for(value in arrayJenisDokumen){
                // post data satu per satu
                val idPermohonan = convertStringToMultipartBody(vIdPermohonan)
                val idDaftarPelatihan = convertStringToMultipartBody(idDaftarPelatihan.toString())
                val jenisDokumen = convertStringToMultipartBody(value.jenis_dokumen!!)
                val ekstensi = convertStringToMultipartBody(value.ekstensi!!)
                val file = value.file!!
                postDokumenPermohonan(post, idPermohonan, idDaftarPelatihan, jenisDokumen, ekstensi, file)
            }
        } else{
            Toast.makeText(this@DetailPelatihanActivity, data.message_response, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFailurePostPermohonan(message: String) {
        Toast.makeText(this@DetailPelatihanActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun postDokumenPermohonan(
        post: RequestBody,
        idPermohonan: RequestBody,
        idDaftarPelatihan: RequestBody,
        jenisDokumen: RequestBody,
        ekstensi: RequestBody,
        file: MultipartBody.Part,
    ) {
        viewModel.postDokumenPermohonan(post, idPermohonan, idDaftarPelatihan, jenisDokumen, ekstensi, file)
    }

    private fun getPostDokumenPermohonan(){
        viewModel.getDokumenPermohonan().observe(this@DetailPelatihanActivity){result->
            when(result){
                is UIState.Loading-> setStartShimmerDetailPelatihan()
                is UIState.Success-> setSuccessPostDokumenPermohonan(result.data)
                is UIState.Failure-> setFailurePostDokumenPermohonan(result.message)
            }
        }
    }

    private fun setSuccessPostDokumenPermohonan(data: ResponseModel) {
        setStopShimmerDetailPelatihan()
        if(data.status == "0"){
            Toast.makeText(this@DetailPelatihanActivity, "Berhasil", Toast.LENGTH_SHORT).show()
        } else{
            Toast.makeText(this@DetailPelatihanActivity, data.message_response, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFailurePostDokumenPermohonan(message: String) {
        Toast.makeText(this@DetailPelatihanActivity, message, Toast.LENGTH_SHORT).show()
        Log.d(TAG, "setSuccessPostPermohonan: gagal")

    }

    private fun daftarPelatihan() {
        if(harga == 0) showDialogDaftarPelatihanGratis() else showDialogDaftarPelatihan()
    }

    @SuppressLint("SetTextI18n")
    private fun showDialogDaftarPelatihanGratis(){
        val view = AlertDialogDaftarBinding.inflate(layoutInflater)
        val alertDialog = AlertDialog.Builder(this@DetailPelatihanActivity)
        alertDialog.apply {
            setView(view.root)
            setCancelable(false)
        }
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            // set information
            tvTitleKonfirmasi.text = namaPelatihan
            tvBodyKonfirmasi.text = """
                Apakah anda ingin daftar $namaPelatihan ? 
                Pelatihan ini Gratis.
                Klik konfirmasi untuk mendaftar
            """.trimIndent()

            // Button
            btnKonfirmasi.setOnClickListener {
                postDaftarGratis()
                dialogInputan.dismiss()
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDialogDaftarPelatihan(){
        val view = AlertDialogDaftarBinding.inflate(layoutInflater)
        val alertDialog = AlertDialog.Builder(this@DetailPelatihanActivity)
        alertDialog.apply {
            setView(view.root)
            setCancelable(false)
        }
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            // set information
            tvTitleKonfirmasi.text = namaPelatihan
            tvBodyKonfirmasi.text = """
                Apakah anda ingin daftar $namaPelatihan ? 
                Pelatihan akan dikenakan biaya seharga ${rupiah.rupiah(harga.toLong())}
                Klik konfirmasi untuk melakukan pembayaran
            """.trimIndent()

            // Button
            btnKonfirmasi.setOnClickListener {
                dataDaftarPelatihan?.let { result ->
                    binding.apply {
                        harga = result.biaya!!
                        val biaya = rupiah.rupiah(result.biaya!!.toLong())
                        val tglMulaiPendaftaran = tanggalDanWaktu.konversiBulanSingkatan(result.tglMulaiDaftar!!)
                        val tglBerakhirPendaftaran = tanggalDanWaktu.konversiBulanSingkatan(result.tglBerakhirDaftar!!)
                        val pelaksanaan = tanggalDanWaktu.konversiBulanDanWaktu(result.tglPelaksanaan!!)

                        // Pindah activity
                        val i = Intent(this@DetailPelatihanActivity, PaymentActivity::class.java)

                        i.putExtra("id_daftar_pelatihan", result.idDaftarPelatihan)
                        i.putExtra("jenis_pelatihan", result.pelatihanModel!!.jenisPelatihanModel!!.jenisPelatihan)
                        i.putExtra("nama_pelatihan", result.pelatihanModel!!.namaPelatihan)
                        i.putExtra("biaya", result.biaya)
                        i.putExtra("pendaftaran", "$tglMulaiPendaftaran - $tglBerakhirPendaftaran")
                        i.putExtra("pelaksanaan", pelaksanaan)
                        i.putExtra("lokasi", result.lokasi)

                        startActivity(i)
                        finish()
                    }
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postDaftarGratis(){
        viewModel.postDaftarPelatihan(idDaftarPelatihan, sharedPreferences.getIdUser())
    }

    private fun getDaftarGratis(){
        viewModel.getDaftarPelatihan().observe(this@DetailPelatihanActivity){result->
            when(result){
                is UIState.Loading -> setStartShimmerDetailPelatihan()
                is UIState.Success -> setSuccessPostDaftarGratis(result.data)
                is UIState.Failure -> setFailurePostDaftarGratis(result.message)
            }
        }
    }

    private fun setFailurePostDaftarGratis(message: String) {
        Toast.makeText(this@DetailPelatihanActivity, message, Toast.LENGTH_SHORT).show()
        setStopShimmerDetailPelatihan()
    }

    private fun setSuccessPostDaftarGratis(data: ResponseModel?) {
        if(data != null){
            if(data.status == "0"){
                Toast.makeText(this@DetailPelatihanActivity, "Berhasil Mendaftar", Toast.LENGTH_SHORT).show()
                fetchTelahDaftarPelatihan(idDaftarPelatihan, sharedPreferences.getIdUser())
                fetchPelatihan(idDaftarPelatihan)
            } else{
                Toast.makeText(this@DetailPelatihanActivity, data.message_response, Toast.LENGTH_SHORT).show()
                setStopShimmerDetailPelatihan()
            }
        }
    }

    private fun fetchTelahDaftarPelatihan(idDaftarPelatihan: Int, idUser: Int){
        viewModel.fetchTelahDaftarPelatihan(idDaftarPelatihan, idUser)
    }

    private fun getTelahDaftarPelatihan(){
        viewModel.getTelahDaftarPelatihan().observe(this@DetailPelatihanActivity){result->
            when(result){
                is UIState.Loading -> {}
                is UIState.Success -> setSuccessFetchTelahDaftarPelatihan(result.data)
                is UIState.Failure -> setFailureFetchTelahDaftarPelatihan(result.message)
            }
        }
    }

    private fun setFailureFetchTelahDaftarPelatihan(message: String) {
        // jika belum mendaftar
        binding.apply {
            btnTerdaftar.visibility = View.GONE
            btnDaftar.visibility = View.VISIBLE
            btnCetakSertifikat.visibility = View.GONE

            Log.d(TAG, "failure fetch pelatihan: ")
            fetchPermohonan(idDaftarPelatihan, sharedPreferences.getIdUser())
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setSuccessFetchTelahDaftarPelatihan(data: PendaftarModel?) {
        if(data != null){
            Log.d(TAG, "Ada data fetch pelatihan: ")
            // sudah mendaftar
            binding.apply {
//                tvKeterangan.text = "Telah Terdaftar"
                data.ket!!.let {
                    if(it.isEmpty()){
                        // Terdaftar tapi belum ada ket
                        btnTerdaftar.visibility = View.VISIBLE
                        btnDaftar.visibility = View.GONE
                        btnCetakSertifikat.visibility = View.GONE
                        btnCetakSertifikat.visibility = View.GONE
                        btnProsesPermohonan.visibility = View.GONE
                    } else if(it == "lulus"){
                        // lulus
                        tvKeterangan.text = """
                            Telah Terdaftar
                            - Lulus -
                        """.trimIndent()
                        btnTerdaftar.visibility = View.GONE
                        btnDaftar.visibility = View.GONE
                        btnCetakSertifikat.visibility = View.VISIBLE
                        btnCetakSertifikat.visibility = View.GONE
                        btnProsesPermohonan.visibility = View.GONE
                        idPendaftar = data.idPendaftar!!
                    } else if(it == "tidak lulus"){
                        // Tidak lulus
                        tvKeterangan.text = """
                            Telah Terdaftar
                            - Tidak Lulus -
                        """.trimIndent()
                        btnTerdaftar.visibility = View.GONE
                        btnDaftar.visibility = View.GONE
                        btnCetakSertifikat.visibility = View.GONE
                        btnCetakSertifikat.visibility = View.GONE
                        btnProsesPermohonan.visibility = View.GONE
                    } else {
                        // Belum daftar
                        btnTerdaftar.visibility = View.GONE
                        btnDaftar.visibility = View.VISIBLE
                        btnCetakSertifikat.visibility = View.GONE
                        btnCetakSertifikat.visibility = View.GONE
                        btnProsesPermohonan.visibility = View.GONE
                    }
                }

//                when (data.ket) {
//                    "lulus" -> {
//                        tvKeterangan.text = """
//                            Telah Terdaftar
//                            - Lulus -
//                        """.trimIndent()
//                        btnTerdaftar.visibility = View.GONE
//                        btnDaftar.visibility = View.GONE
//                        btnCetakSertifikat.visibility = View.VISIBLE
//                    }
//                    "tidak lulus" -> {
//                        tvKeterangan.text = """
//                            Telah Terdaftar
//                            - Tidak Lulus -
//                        """.trimIndent()
//                        btnTerdaftar.visibility = View.GONE
//                        btnDaftar.visibility = View.GONE
//                        btnCetakSertifikat.visibility = View.GONE
//                    }
//                    "" -> {
//                        btnTerdaftar.visibility = View.VISIBLE
//                        btnDaftar.visibility = View.GONE
//                        btnCetakSertifikat.visibility = View.GONE
//                    }
//                    else -> {
//                        btnTerdaftar.visibility = View.GONE
//                        btnDaftar.visibility = View.VISIBLE
//                        btnCetakSertifikat.visibility = View.GONE
//                    }
//                }
            }
        } else{
            // jika belum mendaftar
            binding.apply {
                btnTerdaftar.visibility = View.GONE
                btnDaftar.visibility = View.VISIBLE
                btnCetakSertifikat.visibility = View.GONE

                Log.d(TAG, "tidak ada data fetch pelatihan: ")
                fetchPermohonan(idDaftarPelatihan, sharedPreferences.getIdUser())
            }
        }
    }

    private fun fetchPelatihan(idDaftarPelatihan: Int){
        viewModel.fetchPelatihan(idDaftarPelatihan)
    }

    private fun getPelatihan(){
        viewModel.getPelatihan().observe(this@DetailPelatihanActivity){result->
            when(result){
                is UIState.Loading -> setStartShimmerDetailPelatihan()
                is UIState.Success -> setSuccessFetchPelatihan(result.data)
                is UIState.Failure -> setFailureFetchPelatihan(result.message)
            }
        }
    }

    private fun setFailureFetchPelatihan(message: String) {
        Toast.makeText(this@DetailPelatihanActivity, message, Toast.LENGTH_SHORT).show()
        Toast.makeText(this@DetailPelatihanActivity, "asddf", Toast.LENGTH_SHORT).show()
        setStopShimmerDetailPelatihan()
    }

    @SuppressLint("SetTextI18n")
    private fun setSuccessFetchPelatihan(data: DaftarPelatihanModel?) {
        Log.d(TAG, "success: $data")
        if(data != null){
            dataDaftarPelatihan = data
//            Log.d(TAG, "success not null: $data")
//            Log.d(TAG, "jenis pelatihan: ${data.pelatihanModel!!.jenisPelatihanModel!!.jenisPelatihan}")
//            Log.d(TAG, "nama pelatihan: ${data.pelatihanModel!!.namaPelatihan}")
//            Log.d(TAG, "kuota: ${data.kuota}")
//            Log.d(TAG, "biaya: ${data.biaya!!}")
//            Log.d(TAG, "pendaftaran: ${data.tglMulaiDaftar}")
//            Log.d(TAG, "pelaksanaan: ${data.tglPelaksanaan}")
//            Log.d(TAG, "lokasi: ${data.lokasi}")
//            Log.d(TAG, "deskripsi: ${data.pelatihanModel!!.deskripsi}")
            data.let { result ->
                binding.apply {
                    Log.d(TAG, "setSuccessFetchPelatihan: ")
                    kuota = result.kuota!!
                    harga = result.biaya!!
                    idPelatihan = data.idPelatihan!!

                    val biaya = rupiah.rupiah(result.biaya!!.toLong())
                    tglMulaiPendaftaran = result.tglMulaiDaftar!!
                    tglBerakhirPendaftaran = result.tglBerakhirDaftar!!

                    val tglMulaiPendaftaranTemp = tanggalDanWaktu.konversiBulanSingkatan(result.tglMulaiDaftar!!)
                    val tglBerakhirPendaftaranTemp = tanggalDanWaktu.konversiBulanSingkatan(result.tglBerakhirDaftar!!)
                    val pelaksanaan = tanggalDanWaktu.konversiBulanDanWaktu(result.tglPelaksanaan!!)

                    tvJenisPelatihan.text = (result.pelatihanModel!!.jenisPelatihanModel!!.jenisPelatihan)!!.toUpperCase(Locale.ROOT)
                    tvNamaPelatihan.text = result.pelatihanModel!!.namaPelatihan!!.toUpperCase(Locale.ROOT)
                    tvKuota.text = result.kuota.toString()
                    tvBiaya.text = biaya
                    tvPendaftaran.text = "$tglMulaiPendaftaranTemp - $tglBerakhirPendaftaranTemp"
                    tvPelaksanaan.text = pelaksanaan
                    tvLokasi.text = result.lokasi
                    tvDeskripsi.text = result.pelatihanModel!!.deskripsi


                    Glide.with(this@DetailPelatihanActivity)
                        .load("${Constant.LOCATION_GAMBAR}${result.pelatihanModel!!.gambar}") // URL Gambar
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.img_pelatihan)
                        .into(ivGambarPelatihan) // imageView mana yang akan diterapkan

                }
            }
        } else{
            Toast.makeText(this@DetailPelatihanActivity, "Data tidak ada", Toast.LENGTH_SHORT).show()
        }
        setStopShimmerDetailPelatihan()
    }

    private fun fetchPermohonan(idDaftarPelatihan: Int, idUser: Int){
        Log.d(TAG, "fetchPermohonan: $idDaftarPelatihan, $idUser")
        viewModel.fetchPermohonan(idDaftarPelatihan, idUser)
    }

    private fun getPermohonan(){
        viewModel.getPermohonan().observe(this@DetailPelatihanActivity){result->
            when(result){
                is UIState.Loading -> {}
                is UIState.Success -> setSuccessFetchPermohonan(result.data)
                is UIState.Failure -> setFailureFetchPermohonan(result.message)
            }
        }
    }

    private fun setFailureFetchPermohonan(message: String) {
//        Toast.makeText(this@DetailPelatihanActivity, message, Toast.LENGTH_SHORT).show()
//        setStopShimmerDetailPelatihan()
        Log.d(TAG, "setFailureFetchPermohonan: error")
    }

    @SuppressLint("SetTextI18n")
    private fun setSuccessFetchPermohonan(data: PermohonanModel?) {
        Log.d(TAG, "success: $data")
        binding.apply {
            if(data != null){
                // Belum daftar
                if(data.ket=="1"){
                    btnTerdaftar.visibility = View.GONE
                    btnDaftar.visibility = View.VISIBLE
                    btnCetakSertifikat.visibility = View.GONE
                    btnPermohonan.visibility = View.GONE
                    btnProsesPermohonan.visibility = View.GONE
                } else if(data.ket=="0"){
                    btnTerdaftar.visibility = View.GONE
                    btnDaftar.visibility = View.GONE
                    btnCetakSertifikat.visibility = View.GONE
                    btnPermohonan.visibility = View.GONE
                    btnProsesPermohonan.visibility = View.VISIBLE
                } else{
                    if(data.jenis_dokumen!!.isNotEmpty()){
                        btnTerdaftar.visibility = View.GONE
                        btnDaftar.visibility = View.GONE
                        btnCetakSertifikat.visibility = View.GONE
                        btnPermohonan.visibility = View.VISIBLE
                        btnProsesPermohonan.visibility = View.GONE

                        for(value in data.jenis_dokumen!!){
                            arrayJenisDokumen.add(
                                DokumenPostModel(value.jenis_dokumen, null, value.ekstensi)
                            )
                        }

                        Log.d(TAG, "permohonan modal null: ")
                        Log.d(TAG, "daftar pelatihan modal ada: ")
                    } else{
                        btnTerdaftar.visibility = View.GONE
                        btnDaftar.visibility = View.VISIBLE
                        btnCetakSertifikat.visibility = View.GONE
                        btnPermohonan.visibility = View.GONE
                        btnProsesPermohonan.visibility = View.GONE

                        Log.d(TAG, "permohonan modal null: ")
                        Log.d(TAG, "daftar pelatihan modal null: ")
                    }
                }
            } else{
                btnTerdaftar.visibility = View.GONE
                btnDaftar.visibility = View.VISIBLE
                btnCetakSertifikat.visibility = View.GONE
                btnPermohonan.visibility = View.GONE
                btnProsesPermohonan.visibility = View.GONE

                Log.d(TAG, "permohonan modal null: ")
            }
        }
//        setStopShimmerDetailPelatihan()
    }

    private fun setStartShimmerDetailPelatihan(){
        binding.apply {
            smImage.startShimmer()
            smPenjelasan.startShimmer()

            clPenjelasan.visibility = View.GONE
            ivGambarPelatihan.visibility = View.GONE
            smImage.visibility = View.VISIBLE
            smPenjelasan.visibility = View.VISIBLE
        }
    }

    private fun setStopShimmerDetailPelatihan(){
        binding.apply {
            smImage.stopShimmer()
            smPenjelasan.stopShimmer()

            clPenjelasan.visibility = View.VISIBLE
            ivGambarPelatihan.visibility = View.VISIBLE
            smImage.visibility = View.GONE
            smPenjelasan.visibility = View.GONE
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

    private fun pickFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "application/pdf"
        }

        startActivityForResult(intent, Constant.STORAGE_PERMISSION_CODE)
    }

    private fun pickImage() {
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

            tempTvFile?.text = nameImage

            val file = uploadImageToStorage(fileUri, nameImage, "file")
            val jenisDokumen = arrayJenisDokumen[position].jenis_dokumen
            val ekstensi = arrayJenisDokumen[position].ekstensi
            arrayJenisDokumen[position] = DokumenPostModel(jenisDokumen, file, ekstensi)
        }
    }

    private fun convertStringToMultipartBody(data: String): RequestBody {
        return RequestBody.create("multipart/form-data".toMediaTypeOrNull(), data)
    }
}