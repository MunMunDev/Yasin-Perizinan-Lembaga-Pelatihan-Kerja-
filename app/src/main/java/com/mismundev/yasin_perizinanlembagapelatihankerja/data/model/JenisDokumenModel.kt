package com.mismundev.yasin_perizinanlembagapelatihankerja.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class JenisDokumenModel (
    @SerializedName("id_jenis_dokumen")
    var id_jenis_dokumen: Int? = null,

    @SerializedName("id_jenis_pelatihan")
    var id_jenis_pelatihan: String? = null,

    @SerializedName("jenis_dokumen")
    var jenis_dokumen: String? = null,

    @SerializedName("ekstensi")
    var ekstensi: String? = null,

)