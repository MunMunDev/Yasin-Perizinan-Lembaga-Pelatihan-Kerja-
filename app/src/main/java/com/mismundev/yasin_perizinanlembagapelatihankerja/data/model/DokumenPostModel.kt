package com.mismundev.yasin_perizinanlembagapelatihankerja.data.model

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

class DokumenPostModel (
    @SerializedName("jenis_dokumen")
    var jenis_dokumen: String? = null,

    @SerializedName("file")
    var file: MultipartBody.Part? = null,

    @SerializedName("ekstensi")
    var ekstensi: String? = null,

)