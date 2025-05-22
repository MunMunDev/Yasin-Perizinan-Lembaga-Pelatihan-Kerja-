package com.mismundev.yasin_perizinanlembagapelatihankerja.adapter.admin

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mismundev.yasin_perizinanlembagapelatihankerja.R
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.PelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ListAdminPelatihanBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.Constant
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.OnClickItem

class AdminPelatihanAdapter(
    private var listPelatihan: ArrayList<PelatihanModel>,
    private var onClick: OnClickItem.AdminClickPelatihan
): RecyclerView.Adapter<AdminPelatihanAdapter.ViewHolder>() {
    class ViewHolder(val binding: ListAdminPelatihanBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListAdminPelatihanBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return (listPelatihan.size+1)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            if(position==0){
                tvNo.text = "NO"
                tvJenisPelatihan.text = "Jenis Pelatihan"
                tvNamaPelatihan.text = "Nama Pelatihan"
                tvDeskripsi.text = "Deskripsi"
                tvSetting.text = ""

                tvNo.setBackgroundResource(R.drawable.bg_table_title)
                tvJenisPelatihan.setBackgroundResource(R.drawable.bg_table_title)
                tvNamaPelatihan.setBackgroundResource(R.drawable.bg_table_title)
                tvDeskripsi.setBackgroundResource(R.drawable.bg_table_title)
                ivGambar.setBackgroundResource(R.drawable.bg_table_title)
                tvSetting.setBackgroundResource(R.drawable.bg_table_title)

                tvNo.setTextColor(Color.parseColor("#ffffff"))
                tvJenisPelatihan.setTextColor(Color.parseColor("#ffffff"))
                tvNamaPelatihan.setTextColor(Color.parseColor("#ffffff"))
                tvDeskripsi.setTextColor(Color.parseColor("#ffffff"))
                tvSetting.setTextColor(Color.parseColor("#ffffff"))

                tvNo.setTypeface(null, Typeface.BOLD)
                tvJenisPelatihan.setTypeface(null, Typeface.BOLD)
                tvNamaPelatihan.setTypeface(null, Typeface.BOLD)
                tvDeskripsi.setTypeface(null, Typeface.BOLD)
                tvSetting.setTypeface(null, Typeface.BOLD)
            }
            else{
                val pelatihan = listPelatihan[(position-1)]

                tvNo.text = "$position"

                pelatihan.let { it->
                    tvJenisPelatihan.text = it.jenisPelatihanModel!!.jenisPelatihan
                    tvNamaPelatihan.text = it.namaPelatihan
                    tvDeskripsi.text = it.deskripsi

                    Glide.with(holder.itemView)
                        .load("${Constant.LOCATION_GAMBAR}${it.gambar}")
                        .error(R.drawable.image_error)
                        .into(ivGambar)

                    tvSetting.text = ":::"
                }

                tvNo.setBackgroundResource(R.drawable.bg_table)
                tvJenisPelatihan.setBackgroundResource(R.drawable.bg_table)
                tvNamaPelatihan.setBackgroundResource(R.drawable.bg_table)
                tvDeskripsi.setBackgroundResource(R.drawable.bg_table)
                ivGambar.setBackgroundResource(R.drawable.bg_table)
                tvSetting.setBackgroundResource(R.drawable.bg_table)

                tvNo.setTextColor(Color.parseColor("#000000"))
                tvJenisPelatihan.setTextColor(Color.parseColor("#000000"))
                tvNamaPelatihan.setTextColor(Color.parseColor("#000000"))
                tvDeskripsi.setTextColor(Color.parseColor("#000000"))
                tvSetting.setTextColor(Color.parseColor("#000000"))

                tvNo.setTypeface(null, Typeface.NORMAL)
                tvJenisPelatihan.setTypeface(null, Typeface.NORMAL)
                tvNamaPelatihan.setTypeface(null, Typeface.NORMAL)
                tvDeskripsi.setTypeface(null, Typeface.NORMAL)
                tvSetting.setTypeface(null, Typeface.NORMAL)


                tvJenisPelatihan.setOnClickListener{
                    onClick.clickJenisPelatihan(pelatihan.jenisPelatihanModel!!.jenisPelatihan!!, "Jenis Pelatihan")
                }

                tvNamaPelatihan.setOnClickListener{
                    onClick.clickNamaPelatihan(pelatihan.namaPelatihan!!, "Nama Pelatihan")
                }

                tvDeskripsi.setOnClickListener{
                    onClick.clickDeskripsi(pelatihan.deskripsi!!, "Deskripsi")
                }

                ivGambar.setOnClickListener{
                    onClick.clickGambar(pelatihan.gambar!!, "Gambar")
                }

                tvSetting.setOnClickListener {
                    onClick.clickItemSetting(pelatihan, it)
                }
            }

        }
    }
}