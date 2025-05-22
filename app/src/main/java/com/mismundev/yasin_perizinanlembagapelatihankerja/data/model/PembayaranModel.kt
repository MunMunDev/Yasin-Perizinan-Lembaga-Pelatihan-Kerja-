package com.mismundev.yasin_perizinanlembagapelatihankerja.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class PembayaranModel (
    @SerializedName("id_log_pembayaran")
    var id_log_pembayaran: Int? = null,

    @SerializedName("kode_unik")
    var kode_unik: String? = null,

    @SerializedName("id_user")
    var id_user: String? = null,

    @SerializedName("id_daftar_pelatihan")
    var id_daftar_pelatihan: String? = null,

    @SerializedName("keterangan")
    var keterangan: String? = null,

    @SerializedName("tgl_daftar")
    var tgl_daftar: String? = null,

    @SerializedName("daftar_pelatihan")
    var daftarPelatihanModel: DaftarPelatihanModel? = null,

)