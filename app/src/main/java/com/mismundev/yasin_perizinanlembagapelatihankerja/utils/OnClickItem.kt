package com.mismundev.yasin_perizinanlembagapelatihankerja.utils

import android.view.View
import android.widget.TextView
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.DaftarPelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.DokumenModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.JenisDokumenModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.JenisPelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.PelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.PendaftarModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.PermohonanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.UsersModel

interface OnClickItem {
    interface ClickPelatihan{
        fun clickPelatihan(
            idDaftarPelatihan: Int,
            namaPelatihan: String
        )
    }

    interface ClickDokumenPostPermohonan{
        fun clickFile(
            tvFile: TextView,
            position: Int,
            ekstensi: String,
        )
    }

    interface ClickPermohonan{
        fun clickDownload(
            file: String,
        )
        fun clickFile(
            permohonan: PermohonanModel,
        )
    }

    interface ClickDokumenPermohonan{
        fun clickFile(
            file: String,
            jenisDokumen: String,
            ekstensi: String,
        )
        fun clickUpdate(
            dokumen: DokumenModel,
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

    interface AdminClickPermohonan{
        fun clickNamaUser(
            keterangan: String,
            title: String,
        )
        fun clickNamaPelatihan(
            keterangan: String,
            title: String,
        )
        fun clickJenisDokumen(
            keterangan: String,
            title: String,
        )
        fun clickFile(
            file: String,
            ekstensi: String,
            title: String,
        )
        fun clickItemSetting(permohonan: PermohonanModel, it: View)
    }

    interface AdminClickDokumenPermohonan{
        fun clickJenisDokumen(
            keterangan: String,
            title: String,
        )
        fun clickFile(
            file: String,
            ekstensi: String,
            title: String,
        )
        fun clickItemSetting(dokumen: DokumenModel, it: View)
    }

    interface AdminClickDaftarPelatihanJenisDokumen{
        fun clickJenisPelatihan(
            keterangan: String,
            title: String,
        )
        fun clickNamaPelatihan(
            keterangan: String,
            title: String,
        )
    }

    interface AdminClickJenisDokumen{
        fun clickJenisDokumen(
            keterangan: String,
            title: String,
        )
        fun clickItemSetting(jenisDokumen: JenisDokumenModel, it: View)
    }
}