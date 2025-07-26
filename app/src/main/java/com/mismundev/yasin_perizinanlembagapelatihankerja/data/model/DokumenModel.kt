package com.mismundev.yasin_perizinanlembagapelatihankerja.data.model

import com.google.gson.annotations.SerializedName

class DokumenModel (
    @SerializedName("id_dokumen")
    var id_dokumen: Int? = null,

    @SerializedName("id_permohonan")
    var id_permohonan: String? = null,

    @SerializedName("id_jenis_dokumen")
    var id_jenis_dokumen: String? = null,

    @SerializedName("jenis_dokumen")
    var jenis_dokumen: String? = null,

    @SerializedName("ekstensi")
    var ekstensi: String? = null,

    @SerializedName("file")
    var file: String? = null,

    )