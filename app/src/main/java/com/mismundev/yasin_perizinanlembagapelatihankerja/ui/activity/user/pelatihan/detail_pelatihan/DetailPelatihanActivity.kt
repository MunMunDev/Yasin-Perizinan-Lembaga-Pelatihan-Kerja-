package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.user.pelatihan.detail_pelatihan

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.mismundev.yasin_perizinanlembagapelatihankerja.R
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.DaftarPelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.PelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.PendaftarModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.ResponseModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ActivityDetailPelatihanBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.AlertDialogDaftarBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.user.payment.PaymentActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.Constant
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.KonversiRupiah
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.SharedPreferencesLogin
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.TanggalDanWaktu
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class DetailPelatihanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPelatihanBinding
    private val viewModel: DetailPelatihanViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferencesLogin
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
        getDaftarGratis()
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

            }
        }
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
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setSuccessFetchTelahDaftarPelatihan(data: PendaftarModel?) {
        if(data != null){
            // sudah mendaftar
            binding.apply {
//                tvKeterangan.text = "Telah Terdaftar"
                data.ket!!.let {
                    if(it.isEmpty()){
                        // Terdaftar tapi belum ada ket
                        btnTerdaftar.visibility = View.VISIBLE
                        btnDaftar.visibility = View.GONE
                        btnCetakSertifikat.visibility = View.GONE
                    } else if(it == "lulus"){
                        // lulus
                        tvKeterangan.text = """
                            Telah Terdaftar
                            - Lulus -
                        """.trimIndent()
                        btnTerdaftar.visibility = View.GONE
                        btnDaftar.visibility = View.GONE
                        btnCetakSertifikat.visibility = View.VISIBLE
                    } else if(it == "tidak lulus"){
                        // Tidak lulus
                        tvKeterangan.text = """
                            Telah Terdaftar
                            - Tidak Lulus -
                        """.trimIndent()
                        btnTerdaftar.visibility = View.GONE
                        btnDaftar.visibility = View.GONE
                        btnCetakSertifikat.visibility = View.GONE
                    } else {
                        // Belum daftar
                        btnTerdaftar.visibility = View.GONE
                        btnDaftar.visibility = View.VISIBLE
                        btnCetakSertifikat.visibility = View.GONE
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
        setStopShimmerDetailPelatihan()
    }

    @SuppressLint("SetTextI18n")
    private fun setSuccessFetchPelatihan(data: DaftarPelatihanModel?) {
        Log.d(TAG, "success: $data")
        if(data != null){
            dataDaftarPelatihan = data
            Log.d(TAG, "success not null: $data")
            Log.d(TAG, "jenis pelatihan: ${data.pelatihanModel!!.jenisPelatihanModel!!.jenisPelatihan}")
            Log.d(TAG, "nama pelatihan: ${data.pelatihanModel!!.namaPelatihan}")
            Log.d(TAG, "kuota: ${data.kuota}")
            Log.d(TAG, "biaya: ${data.biaya!!}")
            Log.d(TAG, "pendaftaran: ${data.tglMulaiDaftar}")
            Log.d(TAG, "pelaksanaan: ${data.tglPelaksanaan}")
            Log.d(TAG, "lokasi: ${data.lokasi}")
            Log.d(TAG, "deskripsi: ${data.pelatihanModel!!.deskripsi}")
            data.let { result ->
                binding.apply {
                    Log.d(TAG, "setSuccessFetchPelatihan: ")
                    kuota = result.kuota!!
                    harga = result.biaya!!

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
                        .placeholder(com.midtrans.sdk.uikit.R.drawable.gif_loading_ios)
                        .error(R.drawable.img_pelatihan)
                        .into(ivGambarPelatihan) // imageView mana yang akan diterapkan

                }
            }
        } else{
            Toast.makeText(this@DetailPelatihanActivity, "Data tidak ada", Toast.LENGTH_SHORT).show()
        }
        setStopShimmerDetailPelatihan()
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
}