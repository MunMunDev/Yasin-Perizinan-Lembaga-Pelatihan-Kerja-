package com.mismundev.yasin_perizinanlembagapelatihankerja.adapter.user

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mismundev.yasin_perizinanlembagapelatihankerja.R
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.DokumenModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ListDokumenPermohonanBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.Constant
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.OnClickItem
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.TanggalDanWaktu

class DokumenPermohonanAdapter(
    private var listDokumen: ArrayList<DokumenModel>,
    private var click: OnClickItem.ClickDokumenPermohonan,
    private var ket: String
): RecyclerView.Adapter<DokumenPermohonanAdapter.ViewHolder>() {

    class ViewHolder(val binding: ListDokumenPermohonanBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListDokumenPermohonanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listDokumen.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dokumen = listDokumen[position]
        holder.apply {
            binding.apply {
                if(dokumen.ekstensi?.lowercase()=="pdf"){
                    Glide.with(itemView.context)
                        .load(R.drawable.icon_download_permohonan) // URL Gambar
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.img_pelatihan)
                        .into(ivGambarPelatihan) // imageView mana yang akan diterapkan
                } else{
                    Glide.with(itemView.context)
                        .load("${Constant.LOCATION_DOKUMEN}/${dokumen.file}") // URL Gambar
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.img_pelatihan)
                        .into(ivGambarPelatihan) // imageView mana yang akan diterapkan
                }
                if(ket=="1"){
                    tvFile.visibility = View.GONE
                }

                tvJenisDokumen.text = dokumen.jenis_dokumen
//                tvCatatan.text = dokumen.catatan
                tvEkstensi.text = dokumen.ekstensi
                tvCatatan.text = dokumen.catatan
                if(dokumen.catatan!!.isEmpty()){
                    tvCatatan.visibility = View.GONE
                }
                ivGambarPelatihan.setOnClickListener {
                    click.clickFile(dokumen.file!!, dokumen.jenis_dokumen!!, dokumen.ekstensi!!)
                }
                tvFile.setOnClickListener {
                    click.clickUpdate(dokumen)
                }
            }
        }
    }
}