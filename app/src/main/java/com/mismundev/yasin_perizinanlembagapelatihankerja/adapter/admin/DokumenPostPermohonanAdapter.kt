package com.mismundev.yasin_perizinanlembagapelatihankerja.adapter.admin

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.DokumenPostModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ListDokumenPostBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.OnClickItem

class DokumenPostPermohonanAdapter(
    private var listDokumen: ArrayList<DokumenPostModel>,
    private var onClick: OnClickItem.ClickDokumenPostPermohonan
): RecyclerView.Adapter<DokumenPostPermohonanAdapter.ViewHolder>() {

    class ViewHolder(val binding: ListDokumenPostBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListDokumenPostBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listDokumen.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listDokumen[position]
        holder.binding.apply {
            val title = "${data.jenis_dokumen} (${data.ekstensi})"
            tvTitle.text = title

            etEditFile.setOnClickListener {
                onClick.clickFile(etEditFile, position, data.ekstensi!!)
            }
        }
    }
}