package com.mismundev.yasin_perizinanlembagapelatihankerja.adapter.admin

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mismundev.yasin_perizinanlembagapelatihankerja.R
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.DaftarPelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.JenisDokumenModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ListAdminJenisDokumenBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.jenis_dokumen.jenis_dokumen.AdminJenisDokumenActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.permohonan.dokumen.AdminPermohonanDokumenActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.OnClickItem

class AdminJenisDokumenAdapter(
    private var listPermohonan: ArrayList<JenisDokumenModel>,
    private var onClick: OnClickItem.AdminClickJenisDokumen
): RecyclerView.Adapter<AdminJenisDokumenAdapter.ViewHolder>() {

    class ViewHolder(val binding: ListAdminJenisDokumenBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListAdminJenisDokumenBinding.inflate(
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

                tvNo.setBackgroundResource(R.drawable.bg_table_title)
                tvJenisDokumen.setBackgroundResource(R.drawable.bg_table_title)
                tvEkstensi.setBackgroundResource(R.drawable.bg_table_title)

                tvNo.setTextColor(Color.parseColor("#ffffff"))
                tvJenisDokumen.setTextColor(Color.parseColor("#ffffff"))
                tvEkstensi.setTextColor(Color.parseColor("#ffffff"))

                tvNo.setTypeface(null, Typeface.BOLD)
                tvJenisDokumen.setTypeface(null, Typeface.BOLD)
                tvEkstensi.setTypeface(null, Typeface.BOLD)
            }
            else{
                val jenisDokumen = listPermohonan[(position-1)]

                tvNo.text = "$position"

                jenisDokumen.let {
                    tvJenisDokumen.text = it.jenis_dokumen
                    tvEkstensi.text = it.ekstensi
                }

                tvNo.setBackgroundResource(R.drawable.bg_table)
                tvJenisDokumen.setBackgroundResource(R.drawable.bg_table)
                tvEkstensi.setBackgroundResource(R.drawable.bg_table)

                tvNo.setTextColor(Color.parseColor("#000000"))
                tvJenisDokumen.setTextColor(Color.parseColor("#000000"))
                tvEkstensi.setTextColor(Color.parseColor("#000000"))

                tvNo.setTypeface(null, Typeface.NORMAL)
                tvJenisDokumen.setTypeface(null, Typeface.NORMAL)
                tvEkstensi.setTypeface(null, Typeface.NORMAL)

                tvJenisDokumen.setOnClickListener{
                    onClick.clickJenisDokumen(jenisDokumen.jenis_dokumen!!, "Jenis Dokumen")
                }
                tvSetting.setOnClickListener {
                    onClick.clickItemSetting(jenisDokumen, it)
                }
            }

        }
    }
}