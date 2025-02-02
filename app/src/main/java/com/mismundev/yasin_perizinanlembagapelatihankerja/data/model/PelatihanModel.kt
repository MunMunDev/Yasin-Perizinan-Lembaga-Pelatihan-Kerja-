package com.mismundev.yasin_perizinanlembagapelatihankerja.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class PelatihanModel (
    @SerializedName("id_pelatihan")
    var idPelatihan: Int? = null,

    @SerializedName("id_jenis_pelatihan")
    var idJenisPelatihan: String? = null,

    @SerializedName("nama_pelatihan")
    var namaPelatihan: String? = null,

    @SerializedName("deskripsi")
    var deskripsi: String? = null,

    @SerializedName("gambar")
    var gambar: String? = null,

    @SerializedName("jenis_pelatihan")
    var jenisPelatihanModel: JenisPelatihanModel? = null,

)