package com.mismundev.yasin_perizinanlembagapelatihankerja.utils

import android.view.View
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.DaftarPelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.JenisPelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.PelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.PendaftarModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.UsersModel

interface OnClickItem {
    interface ClickPelatihan{
        fun clickPelatihan(
            idDaftarPelatihan: Int,
            namaPelatihan: String
        )
    }

    interface ClickAkun{
        fun clickItemSetting(akun: UsersModel, it: View)
        fun clickItemAlamat(alamat: String, it: View)
    }

    interface AdminClickJenisPelatihan{
        fun clickJenisPelatihan(
            jenisPelatihan: String,
            title: String,
        )
        fun clickItemSetting(jenisPelatihan: JenisPelatihanModel, it: View)
    }

    interface AdminClickPelatihan{
        fun clickJenisPelatihan(
            jenisPelatihan: String,
            title: String,
        )
        fun clickNamaPelatihan(
            namaPelatihan: String,
            title: String,
        )
        fun clickDeskripsi(
            deskripsi: String,
            title: String,
        )
        fun clickGambar(
            gambar: String,
            title: String,
        )
        fun clickItemSetting(pelatihan: PelatihanModel, it: View)
    }

    interface AdminClickDaftarPelatihan{
        fun clickPenjelasan(
            deskripsi: String,
            title: String,
        )
        fun clickGambar(
            gambar: String,
            title: String,
        )
        fun clickItemSetting(daftarPelatihan: DaftarPelatihanModel, it: View)
    }

    interface ClickAdminUser{
        fun clickItemNama(title:String, nama: String)
        fun clickItemAlamat(title:String, alamat: String)
        fun clickItemEmail(title:String, email: String)
        fun clickItemSetting(wo: UsersModel, it: View)
    }

    interface AdminClickPendaftar{
        fun clickPendaftar(
            pendaftar: String,
            title: String,
        )
        fun clickPelatihan(
            namaPelatihan: String,
            title: String,
        )
        fun clickItemSetting(pendaftar: PendaftarModel, it: View)
    }
}