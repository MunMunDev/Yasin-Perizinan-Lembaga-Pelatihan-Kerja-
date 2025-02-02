package com.mismundev.yasin_perizinanlembagapelatihankerja.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class JenisPelatihanModel (
    @SerializedName("id_jenis_pelatihan")
    var idJenisPelatihan: Int? = null,

    @SerializedName("jenis_pelatihan")
    var jenisPelatihan: String? = null,
)