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

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(JenisPelatihanModel::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(idPelatihan)
        parcel.writeString(idJenisPelatihan)
        parcel.writeString(namaPelatihan)
        parcel.writeString(deskripsi)
        parcel.writeString(gambar)
        parcel.writeParcelable(jenisPelatihanModel, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PelatihanModel> {
        override fun createFromParcel(parcel: Parcel): PelatihanModel {
            return PelatihanModel(parcel)
        }

        override fun newArray(size: Int): Array<PelatihanModel?> {
            return arrayOfNulls(size)
        }
    }
}