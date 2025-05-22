package com.mismundev.yasin_perizinanlembagapelatihankerja.data.model

import com.google.gson.annotations.SerializedName

class PendaftarModel (
    @SerializedName("id_pendaftar")
    var idPendaftar: Int? = null,

    @SerializedName("id_user")
    var idUser: String? = null,

    @SerializedName("id_daftar_pelatihan")
    var idDaftarPelatihan: String? = null,

    @SerializedName("ket")
    var ket: String? = null,

    @SerializedName("tgl_daftar")
    var tglDaftar: String? = null,

    @SerializedName("daftar_pelatihan")
    var daftarPelatihanModel: DaftarPelatihanModel? = null,

    @SerializedName("user")
    var userModel: UsersModel? = null,
)