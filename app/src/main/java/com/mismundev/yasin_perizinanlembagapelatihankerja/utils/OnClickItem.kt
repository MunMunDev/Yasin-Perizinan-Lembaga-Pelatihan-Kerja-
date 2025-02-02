package com.mismundev.yasin_perizinanlembagapelatihankerja.utils

import android.view.View
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.DaftarPelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.PelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.UsersModel

interface OnClickItem {
    interface ClickPelatihan{
        fun clickPelatihan(
            pelatihanModel: DaftarPelatihanModel
        )
    }

    interface ClickAkun{
        fun clickItemSetting(akun: UsersModel, it: View)
        fun clickItemAlamat(alamat: String, it: View)
    }

}