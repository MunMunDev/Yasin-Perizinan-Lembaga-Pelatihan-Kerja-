package com.mismundev.yasin_perizinanlembagapelatihankerja.adapter.user

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mismundev.yasin_perizinanlembagapelatihankerja.R
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.PermohonanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ListPelatihanBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ListPermohonanBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.Constant
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.KonversiRupiah
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.OnClickItem
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.TanggalDanWaktu
import java.util.Locale

class DokumenPermohonanAdapter(
    private var listPermohonan: ArrayList<PermohonanModel>,
    private var click: OnClickItem.ClickPermohonan,
): RecyclerView.Adapter<DokumenPermohonanAdapter.ViewHolder>() {

    private val tanggalDanWaktu = TanggalDanWaktu()

    class ViewHolder(val binding: ListPermohonanBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListPermohonanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listPermohonan.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val permohonan = listPermohonan[position]
        holder.apply {
            binding.apply {
                if(permohonan.ket=="0"){
                    Glide.with(itemView.context)
                        .load(R.drawable.icon_proses_permohonan) // URL Gambar
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.img_pelatihan)
                        .into(ivGambarPelatihan) // imageView mana yang akan diterapkan
                } else{
                    Glide.with(itemView.context)
                        .load(R.drawable.icon_download_permohonan) // URL Gambar
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.img_pelatihan)
                        .into(ivGambarPelatihan) // imageView mana yang akan diterapkan
                }

                val tanggal = tanggalDanWaktu.konversiBulanSingkatan(permohonan.tanggal!!)
                val waktu = tanggalDanWaktu.waktuNoSecond(permohonan.waktu!!)
                val proses = if(permohonan.ket=="1") "Selesai" else "Proses"
                if(permohonan.ket=="1") {
                    tvProses.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.primaryColor))
                }else{
                    tvProses.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.edit))
                }

                tvJenisPelatihan.text = permohonan.pelatihan?.namaPelatihan
//                tvCatatan.text = permohonan.catatan
                tvProses.text = proses
                tvTglPelatihan.text = "$tanggal $waktu"
                ivGambarPelatihan.setOnClickListener {
                    if(permohonan.ket=="1"){
                        click.clickDownload(permohonan.file!!)
                    }
                }
                itemView.setOnClickListener {
                    click.clickFile(permohonan)
                }

            }
        }
    }
}