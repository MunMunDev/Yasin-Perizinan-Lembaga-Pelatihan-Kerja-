package com.mismundev.yasin_perizinanlembagapelatihankerja.adapter.admin

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mismundev.yasin_perizinanlembagapelatihankerja.R
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.PendaftarModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ListAdminPendaftarBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.OnClickItem

class AdminPendaftarAdapter(
    private var listPelatihan: ArrayList<PendaftarModel>,
    private var onClick: OnClickItem.AdminClickPendaftar
): RecyclerView.Adapter<AdminPendaftarAdapter.ViewHolder>() {
    class ViewHolder(val binding: ListAdminPendaftarBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListAdminPendaftarBinding.inflate(
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
                tvUser.text = "Pendaftar"
                tvPelatihan.text = "Nama Pelatihan"
                tvKet.text = "Ket."
                tvTglDaftar.text = "Tgl Daftar"
                tvSetting.text = ""

                tvNo.setBackgroundResource(R.drawable.bg_table_title)
                tvUser.setBackgroundResource(R.drawable.bg_table_title)
                tvPelatihan.setBackgroundResource(R.drawable.bg_table_title)
                tvKet.setBackgroundResource(R.drawable.bg_table_title)
                tvTglDaftar.setBackgroundResource(R.drawable.bg_table_title)
                tvSetting.setBackgroundResource(R.drawable.bg_table_title)

                tvNo.setTextColor(Color.parseColor("#ffffff"))
                tvUser.setTextColor(Color.parseColor("#ffffff"))
                tvPelatihan.setTextColor(Color.parseColor("#ffffff"))
                tvKet.setTextColor(Color.parseColor("#ffffff"))
                tvTglDaftar.setTextColor(Color.parseColor("#ffffff"))
                tvSetting.setTextColor(Color.parseColor("#ffffff"))

                tvNo.setTypeface(null, Typeface.BOLD)
                tvUser.setTypeface(null, Typeface.BOLD)
                tvPelatihan.setTypeface(null, Typeface.BOLD)
                tvKet.setTypeface(null, Typeface.BOLD)
                tvTglDaftar.setTypeface(null, Typeface.BOLD)
                tvSetting.setTypeface(null, Typeface.BOLD)
            }
            else{
                val pendaftar = listPelatihan[(position-1)]
                var nama : String? = ""
                var batch : String? = ""
                var pelatihan : String? = ""
                var ket : String? = ""
                var tglDaftar : String? = ""

                tvNo.text = "$position"
                pendaftar.let { it->
                    nama = it.userModel!!.nama
                    batch = it.daftarPelatihanModel!!.batch
                    pelatihan = it.daftarPelatihanModel!!.pelatihanModel!!.namaPelatihan+" - Batch $batch"
                    ket = it.ket
                    tglDaftar = it.tglDaftar

                    tvUser.text = nama
                    tvPelatihan.text = pelatihan
                    tvKet.text = ket
                    tvTglDaftar.text = tglDaftar
                    tvSetting.text = ":::"
                }

                tvNo.setBackgroundResource(R.drawable.bg_table)
                tvUser.setBackgroundResource(R.drawable.bg_table)
                tvPelatihan.setBackgroundResource(R.drawable.bg_table)
                tvKet.setBackgroundResource(R.drawable.bg_table)
                tvTglDaftar.setBackgroundResource(R.drawable.bg_table)
                tvSetting.setBackgroundResource(R.drawable.bg_table)

                tvNo.setTextColor(Color.parseColor("#000000"))
                tvUser.setTextColor(Color.parseColor("#000000"))
                tvPelatihan.setTextColor(Color.parseColor("#000000"))
                tvKet.setTextColor(Color.parseColor("#000000"))
                tvTglDaftar.setTextColor(Color.parseColor("#000000"))
                tvSetting.setTextColor(Color.parseColor("#000000"))

                tvNo.setTypeface(null, Typeface.NORMAL)
                tvUser.setTypeface(null, Typeface.NORMAL)
                tvPelatihan.setTypeface(null, Typeface.NORMAL)
                tvKet.setTypeface(null, Typeface.NORMAL)
                tvTglDaftar.setTypeface(null, Typeface.NORMAL)
                tvSetting.setTypeface(null, Typeface.NORMAL)

                tvUser.setOnClickListener{
                    onClick.clickPendaftar(nama!!, "Nama Pendaftar")
                }

                tvPelatihan.setOnClickListener{
                    onClick.clickPelatihan(pelatihan!!, "Pelatihan")
                }

                tvSetting.setOnClickListener {
                    onClick.clickItemSetting(pendaftar, it)
                }
            }

        }
    }
}