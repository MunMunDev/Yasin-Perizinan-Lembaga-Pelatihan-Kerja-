package com.mismundev.yasin_perizinanlembagapelatihankerja.adapter.admin

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mismundev.yasin_perizinanlembagapelatihankerja.R
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.DokumenModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ListAdminDokumenPermohonanBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.OnClickItem
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.TanggalDanWaktu

class AdminDokumenPermohonanAdapter(
    private var listPermohonan: ArrayList<DokumenModel>,
    private var onClick: OnClickItem.AdminClickDokumenPermohonan
): RecyclerView.Adapter<AdminDokumenPermohonanAdapter.ViewHolder>() {

    class ViewHolder(val binding: ListAdminDokumenPermohonanBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListAdminDokumenPermohonanBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return (listPermohonan.size+1)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            if(position==0){
                tvNo.text = "NO"
                tvJenisDokumen.text = "Jenis Dokumen"
                tvEkstensi.text = "Ekstensi"
                tvFile.text = "File"
                tvSetting.text = ""

                tvNo.setBackgroundResource(R.drawable.bg_table_title)
                tvJenisDokumen.setBackgroundResource(R.drawable.bg_table_title)
                tvEkstensi.setBackgroundResource(R.drawable.bg_table_title)
                tvFile.setBackgroundResource(R.drawable.bg_table_title)
                tvSetting.setBackgroundResource(R.drawable.bg_table_title)

                tvNo.setTextColor(Color.parseColor("#ffffff"))
                tvJenisDokumen.setTextColor(Color.parseColor("#ffffff"))
                tvEkstensi.setTextColor(Color.parseColor("#ffffff"))
                tvFile.setTextColor(Color.parseColor("#ffffff"))
                tvSetting.setTextColor(Color.parseColor("#ffffff"))

                tvNo.setTypeface(null, Typeface.BOLD)
                tvJenisDokumen.setTypeface(null, Typeface.BOLD)
                tvEkstensi.setTypeface(null, Typeface.BOLD)
                tvFile.setTypeface(null, Typeface.BOLD)
                tvSetting.setTypeface(null, Typeface.BOLD)
            }
            else{
                val permohonan = listPermohonan[(position-1)]

                tvNo.text = "$position"

                permohonan.let {
                    tvJenisDokumen.text = it.jenis_dokumen
                    tvEkstensi.text = it.ekstensi
                    tvFile.text = "File"
                    tvSetting.text = ":::"
                }

                tvNo.setBackgroundResource(R.drawable.bg_table)
                tvJenisDokumen.setBackgroundResource(R.drawable.bg_table)
                tvEkstensi.setBackgroundResource(R.drawable.bg_table)
                tvFile.setBackgroundResource(R.drawable.bg_table)
                tvSetting.setBackgroundResource(R.drawable.bg_table)

                tvNo.setTextColor(Color.parseColor("#000000"))
                tvJenisDokumen.setTextColor(Color.parseColor("#000000"))
                tvEkstensi.setTextColor(Color.parseColor("#000000"))
                tvFile.setTextColor(Color.parseColor("#000000"))
                tvSetting.setTextColor(Color.parseColor("#000000"))

                tvNo.setTypeface(null, Typeface.NORMAL)
                tvJenisDokumen.setTypeface(null, Typeface.NORMAL)
                tvEkstensi.setTypeface(null, Typeface.NORMAL)
                tvFile.setTypeface(null, Typeface.NORMAL)
                tvSetting.setTypeface(null, Typeface.NORMAL)

                tvJenisDokumen.setOnClickListener{
                    onClick.clickJenisDokumen(permohonan.jenis_dokumen!!, "Jenis Dokumen")
                }
                tvFile.setOnClickListener{
                    onClick.clickFile(permohonan.file!!, permohonan.ekstensi!!, permohonan.jenis_dokumen!!)
                }
                tvSetting.setOnClickListener {
                    onClick.clickItemSetting(permohonan, it)
                }
            }

        }
    }
}