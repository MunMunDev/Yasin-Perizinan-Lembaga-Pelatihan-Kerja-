package com.mismundev.yasin_perizinanlembagapelatihankerja.adapter.admin

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mismundev.yasin_perizinanlembagapelatihankerja.R
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.PembayaranModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.UsersModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ListAdminLogPembayaranBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ListAdminUserBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.OnClickItem

class AdminLogPembayaranAdapter(
    private var listUser: ArrayList<PembayaranModel>
): RecyclerView.Adapter<AdminLogPembayaranAdapter.ViewHolder>() {
    class ViewHolder(val binding: ListAdminLogPembayaranBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListAdminLogPembayaranBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return (listUser.size+1)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            if(position==0){
                tvNo.text = "NO"
                tvKodeUnik.text = "Kode Unik"
                tvBatch.text = "Batch"
                tvPelatihan.text = "Pelatihan"
                tvKeterangan.text = "Keterangan"
                tvTglDaftar.text = "Tanggal Daftar"
                tvSetting.text = ""

                tvNo.setBackgroundResource(R.drawable.bg_table_title)
                tvKodeUnik.setBackgroundResource(R.drawable.bg_table_title)
                tvBatch.setBackgroundResource(R.drawable.bg_table_title)
                tvPelatihan.setBackgroundResource(R.drawable.bg_table_title)
                tvKeterangan.setBackgroundResource(R.drawable.bg_table_title)
                tvTglDaftar.setBackgroundResource(R.drawable.bg_table_title)
                tvSetting.setBackgroundResource(R.drawable.bg_table_title)

                tvNo.setTextColor(Color.parseColor("#ffffff"))
                tvKodeUnik.setTextColor(Color.parseColor("#ffffff"))
                tvBatch.setTextColor(Color.parseColor("#ffffff"))
                tvPelatihan.setTextColor(Color.parseColor("#ffffff"))
                tvKeterangan.setTextColor(Color.parseColor("#ffffff"))
                tvTglDaftar.setTextColor(Color.parseColor("#ffffff"))
                tvSetting.setTextColor(Color.parseColor("#ffffff"))

                tvNo.setTypeface(null, Typeface.BOLD)
                tvKodeUnik.setTypeface(null, Typeface.BOLD)
                tvBatch.setTypeface(null, Typeface.BOLD)
                tvPelatihan.setTypeface(null, Typeface.BOLD)
                tvKeterangan.setTypeface(null, Typeface.BOLD)
                tvTglDaftar.setTypeface(null, Typeface.BOLD)
                tvSetting.setTypeface(null, Typeface.BOLD)
            }
            else{
                val user = listUser[(position-1)]

                tvNo.text = "$position"
                user.apply {
                    tvKodeUnik.text = kode_unik
                    tvBatch.text = "${daftarPelatihanModel!!.batch}"
                    tvPelatihan.text = "${daftarPelatihanModel!!.pelatihanModel!!.namaPelatihan}"
                    tvKeterangan.text = keterangan
                    tvTglDaftar.text = tgl_daftar
                }
                tvSetting.text = ":::"

                tvNo.setBackgroundResource(R.drawable.bg_table)
                tvKodeUnik.setBackgroundResource(R.drawable.bg_table)
                tvBatch.setBackgroundResource(R.drawable.bg_table)
                tvPelatihan.setBackgroundResource(R.drawable.bg_table)
                tvKeterangan.setBackgroundResource(R.drawable.bg_table)
                tvTglDaftar.setBackgroundResource(R.drawable.bg_table)
                tvSetting.setBackgroundResource(R.drawable.bg_table)

                tvNo.setTextColor(Color.parseColor("#000000"))
                tvKodeUnik.setTextColor(Color.parseColor("#000000"))
                tvBatch.setTextColor(Color.parseColor("#000000"))
                tvPelatihan.setTextColor(Color.parseColor("#000000"))
                tvKeterangan.setTextColor(Color.parseColor("#000000"))
                tvTglDaftar.setTextColor(Color.parseColor("#000000"))
                tvSetting.setTextColor(Color.parseColor("#000000"))

                tvNo.setTypeface(null, Typeface.NORMAL)
                tvKodeUnik.setTypeface(null, Typeface.NORMAL)
                tvBatch.setTypeface(null, Typeface.NORMAL)
                tvPelatihan.setTypeface(null, Typeface.NORMAL)
                tvKeterangan.setTypeface(null, Typeface.NORMAL)
                tvTglDaftar.setTypeface(null, Typeface.NORMAL)
                tvSetting.setTypeface(null, Typeface.NORMAL)

                tvBatch.gravity = Gravity.CENTER
                tvPelatihan.gravity = Gravity.CENTER_VERTICAL
                tvKeterangan.gravity = Gravity.CENTER_VERTICAL
                tvTglDaftar.gravity = Gravity.CENTER_VERTICAL

            }

        }
    }
}