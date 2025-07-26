package com.mismundev.yasin_perizinanlembagapelatihankerja.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class PermohonanModel (
    @SerializedName("id_permohonan")
    var id_permohonan: Int? = null,

    @SerializedName("id_user")
    var id_user: String? = null,

    @SerializedName("id_pelatihan")
    var id_pelatihan: String? = null,

    @SerializedName("id_daftar_pelatihan")
    var id_daftar_pelatihan: String? = null,

    @SerializedName("tanggal")
    var tanggal: String? = null,

    @SerializedName("waktu")
    var waktu: String? = null,

    @SerializedName("ekstensi")
    var ekstensi: String? = null,

    @SerializedName("file")
    var file: String? = null,

    @SerializedName("ket")
    var ket: String? = null,

    @SerializedName("user")
    var user: UsersModel? = null,

    @SerializedName("pelatihan")
    var pelatihan: PelatihanModel? = null,

    @SerializedName("daftar_pelatihan")
    var daftar_pelatihan: DaftarPelatihanModel? = null,

)