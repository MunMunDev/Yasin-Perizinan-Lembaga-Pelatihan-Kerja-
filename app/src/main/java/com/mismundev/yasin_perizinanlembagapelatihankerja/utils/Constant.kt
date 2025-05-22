package com.mismundev.yasin_perizinanlembagapelatihankerja.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.KeyGenerator

object Constant {
    const val BASE_URL = "https://e-portofolio.web.id/"
//    const val BASE_URL = "http://192.168.17.7"
//    const val BASE_URL = "http://192.168.1.22/"s
//    const val BASE_URL = "http://192.168.233.137/"
    private const val BASE_URL_FILE = "${BASE_URL}pelatihan-kerja/"
    const val LOCATION_GAMBAR = "$BASE_URL_FILE/gambar/"

    const val STORAGE_PERMISSION_CODE = 10

    const val MIDTRANS_SERVER_KEY = "\"SB-Mid-server-3XXeHtewxccaCF8cPt3jTgY4\""
    const val MIDTRANS_CLIENT_KEY = "SB-Mid-client-TVtOnG76Y3rS_nMh"
    //    const val MIDTRANS_BASE_URL = "https://aplikasitugas17.000webhostapp.com/penjualan-plafon/api/response-midtrans.php/"
//    const val MIDTRANS_BASE_URL = "https://aplikasi-skripsi-um-parepare.000webhostapp.com/penjualan-plafon/api/response-midtrans.php//"
    const val MIDTRANS_BASE_URL = "https://e-portofolio.web.id/pelatihan-kerja/api/response-midtrans.php//"
//    const val MIDTRANS_BASE_URL = "https://e-portofolio.web.id/pelatihan-kerja/Midtrans/examples/notification-handler.php/"

    // Membuat keystore. Bisa juga digunakan untuk keystore API jika menggunakan key
    private const val KEY_ALIAS = "MySecretKey"

    fun generateKey() {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        if (keyStore.containsAlias(KEY_ALIAS)) return // Jangan buat lagi jika sudah ada

        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            "AndroidKeyStore"
        )
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()

        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
    }

}