package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.user.payment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.midtrans.sdk.uikit.api.model.CustomerDetails
import com.midtrans.sdk.uikit.api.model.ItemDetails
import com.midtrans.sdk.uikit.api.model.SnapTransactionDetail
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.ResponseModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ActivityPaymentBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.user.pelatihan.detail_pelatihan.DetailPelatihanActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.Constant
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.KataAcak
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.KonversiRupiah
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.LoadingAlertDialog
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.SharedPreferencesLogin
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.TanggalDanWaktu
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
@Suppress("DEPRECATION")
class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    private val viewModel: PaymentViewModel by viewModels()
    @Inject
    lateinit var tanggalDanWaktu: TanggalDanWaktu
    @Inject
    lateinit var rupiah: KonversiRupiah
    @Inject
    lateinit var loading: LoadingAlertDialog
    private var totalBiaya = 0.0
//    private var uuid = UUID.randomUUID().toString()
    @Inject
    lateinit var kataAcak: KataAcak

    private var acak = ""

    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var customerDetails: CustomerDetails
    private var itemDetails: ArrayList<ItemDetails> = arrayListOf()
    private lateinit var initTransactionDetails: SnapTransactionDetail

    private var harga = 0
    private var namaPelatihan = ""
    private var idDaftarPelatihan = 0
    private var idUser = 0
    private var email = ""
    private var namaLengkap = ""
    private var nomorHp = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        acak = kataAcak.getHurufDanAngka()
        setSharedPreferencesLogin()
        fetchDataSebelumnya()
        setAppNavbarDrawer()
        setButton()
        konfigurationMidtrans()
        getDataRegistrasiPembayaran()
    }

    private fun fetchDataSebelumnya() {
        val extras = intent.extras
        if(extras != null) {
            setDataDaftarPelatihan(extras)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDataDaftarPelatihan(extras: Bundle?) {
        if(extras!= null){
            idDaftarPelatihan = intent.getIntExtra("id_daftar_pelatihan", 0)
            val jenisPelatihan = intent.getStringExtra("jenis_pelatihan")!!
            namaPelatihan = intent.getStringExtra("nama_pelatihan")!!
            harga = intent.getIntExtra("biaya", 0)
            val pendaftaran = intent.getStringExtra("pendaftaran")!!
            val pelaksanaan = intent.getStringExtra("pelaksanaan")!!
            val lokasi = intent.getStringExtra("lokasi")!!
            val hargaRp = rupiah.rupiah(harga.toLong())

            binding.apply {
                tvJenisPelatihan.text = jenisPelatihan.uppercase(
                    Locale.ROOT
                )
                tvNamaPelatihan.text = namaPelatihan.uppercase(
                    Locale.ROOT
                )
                tvBiaya.text = hargaRp
                tvPendaftaran.text = pendaftaran
                tvPelaksanaan.text = pelaksanaan
                tvLokasi.text = lokasi
                tvTotalTagihan.text = hargaRp

                itemDetails.add(
                    ItemDetails(
                        "1", harga.toDouble(), 1, namaPelatihan
                    )
                )
            }

        } else{
            Toast.makeText(this@PaymentActivity, "Terima Kasih Telah Memesan", Toast.LENGTH_SHORT).show()
            val i = Intent(this@PaymentActivity, DetailPelatihanActivity::class.java)
            i.putExtra("id_daftar_pelatihan", idDaftarPelatihan)
            i.putExtra("nama_pelatihan", namaPelatihan)
            startActivity(i)
            finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setAppNavbarDrawer() {
        binding.appNavbarDrawer.apply {
            tvTitle.text = "Pembayaran"
            ivNavDrawer.visibility = View.GONE
            ivBack.visibility = View.VISIBLE
        }
    }

    private fun setSharedPreferencesLogin() {
        sharedPreferencesLogin = SharedPreferencesLogin(this@PaymentActivity)
        idUser = sharedPreferencesLogin.getIdUser()
        email = sharedPreferencesLogin.getEmail()
        namaLengkap = sharedPreferencesLogin.getNama()
        nomorHp = sharedPreferencesLogin.getNomorHp()
    }

    private fun setButton() {
        binding.apply {
            binding.appNavbarDrawer.ivBack.setOnClickListener {
                val i = Intent(this@PaymentActivity, DetailPelatihanActivity::class.java)
                i.putExtra("id_daftar_pelatihan", idDaftarPelatihan)
                i.putExtra("nama_pelatihan", namaPelatihan)
                startActivity(i)
                finish()
            }
            btnBayar.setOnClickListener {
                postDataRegistrasiPembayaran(acak, idUser, idDaftarPelatihan)
            }
        }
    }

    private fun postDataRegistrasiPembayaran(
        kodeUnik: String,
        idUser: Int,
        idDaftarPelatihan: Int,
    ) {
        viewModel.postRegistrasiPembayaran(kodeUnik, idUser, idDaftarPelatihan)
    }

    private fun getDataRegistrasiPembayaran() {
        viewModel.getRegistrasiPembayaran().observe(this@PaymentActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@PaymentActivity)
                is UIState.Success-> setDataSuccessRegistrasiPembayaran(result.data)
                is UIState.Failure-> setDataFailureRegistrasiPembayaran(result.message)
                else->{}
            }
        }
    }

    private fun setDataFailureRegistrasiPembayaran(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@PaymentActivity, "Ada masalah : $message", Toast.LENGTH_SHORT).show()
    }

    private fun setDataSuccessRegistrasiPembayaran(data: ResponseModel?) {
        loading.alertDialogCancel()
        if(data != null){
            if(data.status == "0"){
                UiKitApi.getDefaultInstance().startPaymentUiFlow(
                    activity = this@PaymentActivity,
                    launcher = launcher,
                    transactionDetails = initTransactionDetails,
                    customerDetails = customerDetails,
                    itemDetails = itemDetails
                )
            }else{
                Toast.makeText(this@PaymentActivity, "Gagal Registrasi", Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@PaymentActivity, "Bermasalah di web", Toast.LENGTH_SHORT).show()
        }
    }

    private fun konfigurationMidtrans() {
        setLauncher()
        setCustomerDetails()
        setInitTransactionDetails()
        buildUiKit()
    }

    private fun buildUiKit() {
        setInitTransactionDetails()
        UiKitApi.Builder()
            .withContext(this.applicationContext)
            .withMerchantUrl(Constant.MIDTRANS_BASE_URL)
            .withMerchantClientKey(Constant.MIDTRANS_CLIENT_KEY)
            .enableLog(true)
            .build()
        uiKitCustomSetting()
    }

    private fun uiKitCustomSetting() {
        val uIKitCustomSetting = UiKitApi.getDefaultInstance().uiKitSetting
        uIKitCustomSetting.saveCardChecked = true
    }

    private fun setInitTransactionDetails() {
//        initTransactionDetails = SnapTransactionDetail(
//            uuid,
//            totalBiaya
//        )
        initTransactionDetails = SnapTransactionDetail(
            acak,
            totalBiaya
        )
    }

    private fun setCustomerDetails() {
//        var nomorHp = sharedPreferencesLogin.getNomorHp()
//        if(nomorHp == ""){
//            nomorHp = "0"
//        }
//        customerDetails = CustomerDetails(
//            firstName = sharedPreferencesLogin.getNama(),
//            customerIdentifier = "${sharedPreferencesLogin.getIdUser()}",
//            email = "mail@mail.com",
//            phone = nomorHp
//        )
        if(nomorHp == ""){
            nomorHp = "0"
        }

        customerDetails = CustomerDetails(
            firstName = namaLengkap,
            customerIdentifier = "${sharedPreferencesLogin.getIdUser()}",
            email = email,
            phone = nomorHp
        )
    }

    private fun setLauncher() {
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.let {
//                    val transactionResult = it.getParcelableExtra<com.midtrans.sdk.uikit.api.model.TransactionResult>(
//                        UiKitConstants.KEY_TRANSACTION_RESULT)


//                    Toast.makeText(this, "${transactionResult?.transactionId}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    @Deprecated("""
        This method has been deprecated in favor of using the Activity Result API
        which brings increased type safety via an {@link ActivityResultContract} and the prebuilt
        contracts for common intents available in
        {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for
        testing, and allow receiving results in separate, testable classes independent from your
        activity. Use
        {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}
        with the appropriate {@link ActivityResultContract} and handling the result in the
        {@link ActivityResultCallback#onActivityResult(Object) callback}.
    """)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            val transactionResult = data?.getParcelableExtra<com.midtrans.sdk.uikit.api.model.TransactionResult>(
                UiKitConstants.KEY_TRANSACTION_RESULT)
            if (transactionResult != null) {
                loading.alertDialogLoading(this@PaymentActivity)
                when (transactionResult.status) {
                    UiKitConstants.STATUS_SUCCESS -> {
                        loading.alertDialogCancel()
                        Toast.makeText(this, "Transaction Finished. ID: " + transactionResult.transactionId, Toast.LENGTH_LONG).show()
//                        fetchDataPembayaran(idUser)
                    }
                    UiKitConstants.STATUS_PENDING -> {
                        loading.alertDialogCancel()
                        Toast.makeText(this, "Transaction Pending. ID: " + transactionResult.transactionId, Toast.LENGTH_LONG).show()
                        acak = kataAcak.getHurufDanAngka()
//                        fetchDataPembayaran(idUser)
                    }
                    UiKitConstants.STATUS_FAILED -> {
                        loading.alertDialogCancel()
                        Toast.makeText(this, "Transaction Failed. ID: " + transactionResult.transactionId, Toast.LENGTH_LONG).show()
                        acak = kataAcak.getHurufDanAngka()
//                        fetchDataPembayaran(idUser)
                    }
                    UiKitConstants.STATUS_CANCELED -> {
                        loading.alertDialogCancel()
                        Toast.makeText(this, "Transaction Cancelled", Toast.LENGTH_LONG).show()
                        acak = kataAcak.getHurufDanAngka()
//                        fetchDataPembayaran(idUser)
                    }
                    UiKitConstants.STATUS_INVALID -> {
                        loading.alertDialogCancel()
                        Toast.makeText(this, "Transaction Invalid. ID: " + transactionResult.transactionId, Toast.LENGTH_LONG).show()
                        acak = kataAcak.getHurufDanAngka()
//                        fetchDataPembayaran(idUser)
                    }
                    else -> {
                        Toast.makeText(this, "Transaction ID: " + transactionResult.transactionId + ". Message: " + transactionResult.status, Toast.LENGTH_LONG).show()
                        loading.alertDialogCancel()
//                        fetchDataPembayaran(idUser)
                    }
                }
            } else {
                Toast.makeText(this@PaymentActivity, "Gagal Tranksaksi", Toast.LENGTH_LONG).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    @Deprecated("""
        This method has been deprecated in favor of using the
        {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.
        The OnBackPressedDispatcher controls how back button events are dispatched
        to one or more {@link OnBackPressedCallback} objects.
    """)
    override fun onBackPressed() {
        super.onBackPressed()

        val i = Intent(this@PaymentActivity, DetailPelatihanActivity::class.java)
        i.putExtra("id_daftar_pelatihan", idDaftarPelatihan)
        i.putExtra("nama_pelatihan", namaPelatihan)
        startActivity(i)
        finish()
    }
}