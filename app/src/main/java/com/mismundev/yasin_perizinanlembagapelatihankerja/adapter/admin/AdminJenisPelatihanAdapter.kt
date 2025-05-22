package com.mismundev.yasin_perizinanlembagapelatihankerja.adapter.admin

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mismundev.yasin_perizinanlembagapelatihankerja.R
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.JenisPelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ListAdminJenisPelatihanBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.OnClickItem

class AdminJenisPelatihanAdapter(
    private var listJenisPelatihan: ArrayList<JenisPelatihanModel>,
    private var onClick: OnClickItem.AdminClickJenisPelatihan
): RecyclerView.Adapter<AdminJenisPelatihanAdapter.ViewHolder>() {
    class ViewHolder(val binding: ListAdminJenisPelatihanBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListAdminJenisPelatihanBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return (listJenisPelatihan.size+1)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            if(position==0){
                tvNo.text = "NO"
                tvJenisPelatihan.text = "Nama"
                tvSetting.text = ""

                tvNo.setBackgroundResource(R.drawable.bg_table_title)
                tvJenisPelatihan.setBackgroundResource(R.drawable.bg_table_title)
                tvSetting.setBackgroundResource(R.drawable.bg_table_title)

                tvNo.setTextColor(Color.parseColor("#ffffff"))
                tvJenisPelatihan.setTextColor(Color.parseColor("#ffffff"))
                tvSetting.setTextColor(Color.parseColor("#ffffff"))

                tvNo.setTypeface(null, Typeface.BOLD)
                tvJenisPelatihan.setTypeface(null, Typeface.BOLD)
                tvSetting.setTypeface(null, Typeface.BOLD)
            }
            else{
                val jenisPelatihan = listJenisPelatihan[(position-1)]

                tvNo.text = "$position"
                tvJenisPelatihan.text = jenisPelatihan.jenisPelatihan
                tvSetting.text = ":::"

                tvNo.setBackgroundResource(R.drawable.bg_table)
                tvJenisPelatihan.setBackgroundResource(R.drawable.bg_table)
                tvSetting.setBackgroundResource(R.drawable.bg_table)

                tvNo.setTextColor(Color.parseColor("#000000"))
                tvJenisPelatihan.setTextColor(Color.parseColor("#000000"))
                tvSetting.setTextColor(Color.parseColor("#000000"))

                tvNo.setTypeface(null, Typeface.NORMAL)
                tvJenisPelatihan.setTypeface(null, Typeface.NORMAL)
                tvSetting.setTypeface(null, Typeface.NORMAL)


                tvJenisPelatihan.setOnClickListener{
                    onClick.clickJenisPelatihan(jenisPelatihan.jenisPelatihan!!, "Jenis Pelatihan")
                }
                tvSetting.setOnClickListener {
                    onClick.clickItemSetting(jenisPelatihan, it)
                }
            }

        }
    }
}