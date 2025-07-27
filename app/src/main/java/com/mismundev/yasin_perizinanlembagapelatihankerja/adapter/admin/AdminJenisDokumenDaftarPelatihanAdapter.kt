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
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ListAdminJenisDokumenDaftarPelatihanBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.jenis_dokumen.jenis_dokumen.AdminJenisDokumenActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.permohonan.dokumen.AdminPermohonanDokumenActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.OnClickItem

class AdminJenisDokumenDaftarPelatihanAdapter(
    private var listPermohonan: ArrayList<DaftarPelatihanModel>,
    private var onClick: OnClickItem.AdminClickDaftarPelatihanJenisDokumen
): RecyclerView.Adapter<AdminJenisDokumenDaftarPelatihanAdapter.ViewHolder>() {

    class ViewHolder(val binding: ListAdminJenisDokumenDaftarPelatihanBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListAdminJenisDokumenDaftarPelatihanBinding.inflate(
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
                tvJenisPelatihan.text = "Jenis Pelatihan"
                tvNamaPelatihan.text = "Nama Pelatihan"
                tvBatch.text = "Batch"
                tvDokumen.text = "Dokumen"

                tvNo.setBackgroundResource(R.drawable.bg_table_title)
                tvJenisPelatihan.setBackgroundResource(R.drawable.bg_table_title)
                tvNamaPelatihan.setBackgroundResource(R.drawable.bg_table_title)
                tvBatch.setBackgroundResource(R.drawable.bg_table_title)
                tvDokumen.setBackgroundResource(R.drawable.bg_table_title)

                tvNo.setTextColor(Color.parseColor("#ffffff"))
                tvJenisPelatihan.setTextColor(Color.parseColor("#ffffff"))
                tvNamaPelatihan.setTextColor(Color.parseColor("#ffffff"))
                tvBatch.setTextColor(Color.parseColor("#ffffff"))
                tvDokumen.setTextColor(Color.parseColor("#ffffff"))

                tvNo.setTypeface(null, Typeface.BOLD)
                tvJenisPelatihan.setTypeface(null, Typeface.BOLD)
                tvNamaPelatihan.setTypeface(null, Typeface.BOLD)
                tvBatch.setTypeface(null, Typeface.BOLD)
                tvDokumen.setTypeface(null, Typeface.BOLD)
            }
            else{
                val pelatihan = listPermohonan[(position-1)]

                tvNo.text = "$position"

                pelatihan.let {
                    tvJenisPelatihan.text = it.pelatihanModel?.jenisPelatihanModel?.jenisPelatihan
                    tvNamaPelatihan.text = it.pelatihanModel?.namaPelatihan
                    tvBatch.text = it.batch
                    tvDokumen.text = "Dokumen"
                }

                tvNo.setBackgroundResource(R.drawable.bg_table)
                tvJenisPelatihan.setBackgroundResource(R.drawable.bg_table)
                tvNamaPelatihan.setBackgroundResource(R.drawable.bg_table)
                tvBatch.setBackgroundResource(R.drawable.bg_table)
                tvDokumen.setBackgroundResource(R.drawable.bg_table)

                tvNo.setTextColor(Color.parseColor("#000000"))
                tvJenisPelatihan.setTextColor(Color.parseColor("#000000"))
                tvNamaPelatihan.setTextColor(Color.parseColor("#000000"))
                tvBatch.setTextColor(Color.parseColor("#000000"))
                tvDokumen.setTextColor(Color.parseColor("#000000"))

                tvNo.setTypeface(null, Typeface.NORMAL)
                tvJenisPelatihan.setTypeface(null, Typeface.NORMAL)
                tvNamaPelatihan.setTypeface(null, Typeface.NORMAL)
                tvBatch.setTypeface(null, Typeface.NORMAL)
                tvDokumen.setTypeface(null, Typeface.NORMAL)

                tvJenisPelatihan.setOnClickListener{
                    onClick.clickJenisPelatihan(pelatihan.pelatihanModel?.jenisPelatihanModel?.jenisPelatihan!!, "Jenis Pelatihan")
                }
                tvNamaPelatihan.setOnClickListener{
                    onClick.clickNamaPelatihan(pelatihan.pelatihanModel?.namaPelatihan!!, "Nama Pelatihan")
                }
                tvDokumen.setOnClickListener{
                    val i = Intent(holder.itemView.context, AdminJenisDokumenActivity::class.java)
                    i.putExtra("id_daftar_pelatihan", pelatihan.idDaftarPelatihan!!)
                    holder.itemView.context.startActivity(i)
                }
            }

        }
    }
}