package com.mismundev.yasin_perizinanlembagapelatihankerja.adapter.admin

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mismundev.yasin_perizinanlembagapelatihankerja.R
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.PermohonanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ListAdminPelatihanBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ListAdminPermohonanIzinBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.main.AdminMainActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.permohonan.dokumen.AdminPermohonanDokumenActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.OnClickItem
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.TanggalDanWaktu

class AdminPermohonanAdapter(
    private var listPermohonan: ArrayList<PermohonanModel>,
    private var onClick: OnClickItem.AdminClickPermohonan
): RecyclerView.Adapter<AdminPermohonanAdapter.ViewHolder>() {
    private val tanggalDanWaktu = TanggalDanWaktu()

    class ViewHolder(val binding: ListAdminPermohonanIzinBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListAdminPermohonanIzinBinding.inflate(
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
                tvNamaUser.text = "Nama User"
                tvNamaPelatihan.text = "Nama Pelatihan"
//                tvJenisDokumen.text = "Jenis Dokumen"
                tvTanggal.text = "Tanggal"
                tvWaktu.text = "Waktu"
                tvEkstensi.text = "Ekstensi"
                tvFile.text = "File"
                tvDokumen.text = "Dokumen"
                tvVerifikasi.text = "Terverifikasi"
                tvCatatan.text = "Catatan"
                tvSetting.text = ""

                tvNo.setBackgroundResource(R.drawable.bg_table_title)
                tvNamaUser.setBackgroundResource(R.drawable.bg_table_title)
                tvNamaPelatihan.setBackgroundResource(R.drawable.bg_table_title)
//                tvJenisDokumen.setBackgroundResource(R.drawable.bg_table_title)
                tvTanggal.setBackgroundResource(R.drawable.bg_table_title)
                tvWaktu.setBackgroundResource(R.drawable.bg_table_title)
                tvEkstensi.setBackgroundResource(R.drawable.bg_table_title)
                tvFile.setBackgroundResource(R.drawable.bg_table_title)
                tvDokumen.setBackgroundResource(R.drawable.bg_table_title)
                tvVerifikasi.setBackgroundResource(R.drawable.bg_table_title)
                tvCatatan.setBackgroundResource(R.drawable.bg_table_title)
                tvSetting.setBackgroundResource(R.drawable.bg_table_title)

                tvNo.setTextColor(Color.parseColor("#ffffff"))
                tvNamaUser.setTextColor(Color.parseColor("#ffffff"))
                tvNamaPelatihan.setTextColor(Color.parseColor("#ffffff"))
//                tvJenisDokumen.setTextColor(Color.parseColor("#ffffff"))
                tvTanggal.setTextColor(Color.parseColor("#ffffff"))
                tvWaktu.setTextColor(Color.parseColor("#ffffff"))
                tvEkstensi.setTextColor(Color.parseColor("#ffffff"))
                tvFile.setTextColor(Color.parseColor("#ffffff"))
                tvDokumen.setTextColor(Color.parseColor("#ffffff"))
                tvVerifikasi.setTextColor(Color.parseColor("#ffffff"))
                tvCatatan.setTextColor(Color.parseColor("#ffffff"))
                tvSetting.setTextColor(Color.parseColor("#ffffff"))

                tvNo.setTypeface(null, Typeface.BOLD)
                tvNamaUser.setTypeface(null, Typeface.BOLD)
                tvNamaPelatihan.setTypeface(null, Typeface.BOLD)
//                tvJenisDokumen.setTypeface(null, Typeface.BOLD)
                tvTanggal.setTypeface(null, Typeface.BOLD)
                tvWaktu.setTypeface(null, Typeface.BOLD)
                tvEkstensi.setTypeface(null, Typeface.BOLD)
                tvFile.setTypeface(null, Typeface.BOLD)
                tvDokumen.setTypeface(null, Typeface.BOLD)
                tvVerifikasi.setTypeface(null, Typeface.BOLD)
                tvCatatan.setTypeface(null, Typeface.BOLD)
                tvSetting.setTypeface(null, Typeface.BOLD)
            }
            else{
                val permohonan = listPermohonan[(position-1)]

                tvNo.text = "$position"

                permohonan.let {
                    tvNamaUser.text = it.user!!.nama
                    tvNamaPelatihan.text = it.daftar_pelatihan?.pelatihanModel?.namaPelatihan+" - "+it.daftar_pelatihan?.batch
                    tvTanggal.text = tanggalDanWaktu.konversiBulan(it.tanggal!!)
                    tvWaktu.text =  it.waktu
                    tvEkstensi.text = it.ekstensi
                    tvFile.text = "File"
                    tvDokumen.text = "Dokumen"
                    tvVerifikasi.text = if(it.ket == "0") "Belum" else "Sudah"
                    tvCatatan.text = it.catatan
                    tvSetting.text = ":::"
                }

                tvNo.setBackgroundResource(R.drawable.bg_table)
                tvNamaUser.setBackgroundResource(R.drawable.bg_table)
                tvNamaPelatihan.setBackgroundResource(R.drawable.bg_table)
//                tvJenisDokumen.setBackgroundResource(R.drawable.bg_table)
                tvTanggal.setBackgroundResource(R.drawable.bg_table)
                tvWaktu.setBackgroundResource(R.drawable.bg_table)
                tvEkstensi.setBackgroundResource(R.drawable.bg_table)
                tvFile.setBackgroundResource(R.drawable.bg_table)
                tvDokumen.setBackgroundResource(R.drawable.bg_table)
                tvVerifikasi.setBackgroundResource(R.drawable.bg_table)
                tvCatatan.setBackgroundResource(R.drawable.bg_table)
                tvSetting.setBackgroundResource(R.drawable.bg_table)

                tvNo.setTextColor(Color.parseColor("#000000"))
                tvNamaUser.setTextColor(Color.parseColor("#000000"))
                tvNamaPelatihan.setTextColor(Color.parseColor("#000000"))
//                tvJenisDokumen.setTextColor(Color.parseColor("#000000"))
                tvTanggal.setTextColor(Color.parseColor("#000000"))
                tvWaktu.setTextColor(Color.parseColor("#000000"))
                tvEkstensi.setTextColor(Color.parseColor("#000000"))
                tvFile.setTextColor(Color.parseColor("#000000"))
                tvDokumen.setTextColor(Color.parseColor("#000000"))
                tvVerifikasi.setTextColor(Color.parseColor("#000000"))
                tvCatatan.setTextColor(Color.parseColor("#000000"))
                tvSetting.setTextColor(Color.parseColor("#000000"))

                tvNo.setTypeface(null, Typeface.NORMAL)
                tvNamaUser.setTypeface(null, Typeface.NORMAL)
                tvNamaPelatihan.setTypeface(null, Typeface.NORMAL)
//                tvJenisDokumen.setTypeface(null, Typeface.NORMAL)
                tvTanggal.setTypeface(null, Typeface.NORMAL)
                tvWaktu.setTypeface(null, Typeface.NORMAL)
                tvEkstensi.setTypeface(null, Typeface.NORMAL)
                tvFile.setTypeface(null, Typeface.NORMAL)
                tvDokumen.setTypeface(null, Typeface.NORMAL)
                tvVerifikasi.setTypeface(null, Typeface.NORMAL)
                tvCatatan.setTypeface(null, Typeface.NORMAL)
                tvSetting.setTypeface(null, Typeface.NORMAL)

                tvNamaUser.setOnClickListener{
                    onClick.clickNamaUser(permohonan.user!!.nama!!, "Nama User")
                }
                tvNamaPelatihan.setOnClickListener{
                    onClick.clickNamaPelatihan(permohonan.pelatihan!!.namaPelatihan!!, "Nama Pelatihan")
                }
//                tvJenisDokumen.setOnClickListener{
//                    onClick.clickJenisDokumen(pelatihan.jenis_dokumen!!.jenis_dokumen!!, "Jenis Dokumen")
//                }
                tvFile.setOnClickListener{
                    onClick.clickFile(permohonan.file!!, permohonan.ekstensi!!, "Gambar")
                }
                tvDokumen.setOnClickListener{
                    val i = Intent(holder.itemView.context, AdminPermohonanDokumenActivity::class.java)
                    i.putExtra("id_permohonan", permohonan.id_permohonan!!)
                    i.putExtra("id_daftar_pelatihan", permohonan.id_daftar_pelatihan!!)
                    holder.itemView.context.startActivity(i)
                }
                tvSetting.setOnClickListener {
                    onClick.clickItemSetting(permohonan, it)
                }
            }

        }
    }
}