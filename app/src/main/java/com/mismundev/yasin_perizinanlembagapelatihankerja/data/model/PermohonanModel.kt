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

    @SerializedName("catatan")
    var catatan: String? = null,

    @SerializedName("user")
    var user: UsersModel? = null,

    @SerializedName("pelatihan")
    var pelatihan: PelatihanModel? = null,

    @SerializedName("daftar_pelatihan")
    var daftar_pelatihan: DaftarPelatihanModel? = null,

    @SerializedName("jenis_dokumen")
    var jenis_dokumen: ArrayList<JenisDokumenModel>? = null,

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(UsersModel::class.java.classLoader),
        parcel.readParcelable(PelatihanModel::class.java.classLoader),
        parcel.readParcelable(DaftarPelatihanModel::class.java.classLoader),
        TODO("jenis_dokumen")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id_permohonan)
        parcel.writeString(id_user)
        parcel.writeString(id_pelatihan)
        parcel.writeString(id_daftar_pelatihan)
        parcel.writeString(tanggal)
        parcel.writeString(waktu)
        parcel.writeString(ekstensi)
        parcel.writeString(file)
        parcel.writeString(ket)
        parcel.writeString(catatan)
        parcel.writeParcelable(user, flags)
        parcel.writeParcelable(pelatihan, flags)
        parcel.writeParcelable(daftar_pelatihan, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PermohonanModel> {
        override fun createFromParcel(parcel: Parcel): PermohonanModel {
            return PermohonanModel(parcel)
        }

        override fun newArray(size: Int): Array<PermohonanModel?> {
            return arrayOfNulls(size)
        }
    }
}