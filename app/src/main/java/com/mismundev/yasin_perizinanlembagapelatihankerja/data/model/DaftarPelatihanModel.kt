package com.mismundev.yasin_perizinanlembagapelatihankerja.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class DaftarPelatihanModel (
    @SerializedName("id_daftar_pelatihan")
    var idDaftarPelatihan: Int? = null,

    @SerializedName("id_pelatihan")
    var idPelatihan: Int? = null,

    @SerializedName("batch")
    var batch: String? = null,

    @SerializedName("kuota")
    var kuota: Int? = null,

    @SerializedName("biaya")
    var biaya: Int? = null,

    @SerializedName("tgl_pelaksanaan")
    var tglPelaksanaan: String? = null,

    @SerializedName("tgl_mulai_daftar")
    var tglMulaiDaftar: String? = null,

    @SerializedName("tgl_berakhir_daftar")
    var tglBerakhirDaftar: String? = null,

    @SerializedName("lokasi")
    var lokasi: String? = null,

    @SerializedName("sertifikat")
    var sertifikat: String? = null,

    @SerializedName("pelatihan")
    var pelatihanModel: PelatihanModel? = null,

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(PelatihanModel::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(idDaftarPelatihan)
        parcel.writeValue(idPelatihan)
        parcel.writeString(batch)
        parcel.writeValue(kuota)
        parcel.writeValue(biaya)
        parcel.writeString(tglPelaksanaan)
        parcel.writeString(tglMulaiDaftar)
        parcel.writeString(tglBerakhirDaftar)
        parcel.writeString(lokasi)
        parcel.writeString(sertifikat)
        parcel.writeParcelable(pelatihanModel, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DaftarPelatihanModel> {
        override fun createFromParcel(parcel: Parcel): DaftarPelatihanModel {
            return DaftarPelatihanModel(parcel)
        }

        override fun newArray(size: Int): Array<DaftarPelatihanModel?> {
            return arrayOfNulls(size)
        }
    }
}