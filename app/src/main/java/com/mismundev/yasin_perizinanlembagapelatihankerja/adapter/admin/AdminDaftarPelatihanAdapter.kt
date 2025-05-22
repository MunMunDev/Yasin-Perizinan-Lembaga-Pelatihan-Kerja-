package com.mismundev.yasin_perizinanlembagapelatihankerja.adapter.admin

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mismundev.yasin_perizinanlembagapelatihankerja.R
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.DaftarPelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ListAdminDaftarPelatihanBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.Constant
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.KonversiRupiah
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.OnClickItem

class AdminDaftarPelatihanAdapter(
    private var listDaftarPelatihan: ArrayList<DaftarPelatihanModel>,
    private var onClick: OnClickItem.AdminClickDaftarPelatihan
): RecyclerView.Adapter<AdminDaftarPelatihanAdapter.ViewHolder>() {
    private val rupiah: KonversiRupiah = KonversiRupiah()
    class ViewHolder(val binding: ListAdminDaftarPelatihanBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListAdminDaftarPelatihanBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return (listDaftarPelatihan.size + 1)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            if (position == 0) {
                tvNo.text = "NO"
                tvBatch.text = "Batch"
                tvPelatihan.text = "Nama Pelatihan"
                tvKuota.text = "Kuota"
                tvBiaya.text = "Biaya"
                tvTanggalPelaksanaan.text = "Tangal Pelaksanaan"
                tvTanggalMulaiDaftar.text = "Mulai Daftar"
                tvTanggalBerakhirDaftar.text = "Berakhir Daftar"
                tvLokasi.text = "Lokasi"
                tvSetting.text = ""

                tvNo.setBackgroundResource(R.drawable.bg_table_title)
                tvBatch.setBackgroundResource(R.drawable.bg_table_title)
                tvPelatihan.setBackgroundResource(R.drawable.bg_table_title)
                tvKuota.setBackgroundResource(R.drawable.bg_table_title)
                tvBiaya.setBackgroundResource(R.drawable.bg_table_title)
                tvTanggalPelaksanaan.setBackgroundResource(R.drawable.bg_table_title)
                tvTanggalMulaiDaftar.setBackgroundResource(R.drawable.bg_table_title)
                tvTanggalBerakhirDaftar.setBackgroundResource(R.drawable.bg_table_title)
                tvLokasi.setBackgroundResource(R.drawable.bg_table_title)
                ivGambar.setBackgroundResource(R.drawable.bg_table_title)
                tvSetting.setBackgroundResource(R.drawable.bg_table_title)

                tvNo.setTextColor(Color.parseColor("#ffffff"))
                tvBatch.setTextColor(Color.parseColor("#ffffff"))
                tvPelatihan.setTextColor(Color.parseColor("#ffffff"))
                tvKuota.setTextColor(Color.parseColor("#ffffff"))
                tvBiaya.setTextColor(Color.parseColor("#ffffff"))
                tvTanggalPelaksanaan.setTextColor(Color.parseColor("#ffffff"))
                tvTanggalMulaiDaftar.setTextColor(Color.parseColor("#ffffff"))
                tvTanggalBerakhirDaftar.setTextColor(Color.parseColor("#ffffff"))
                tvLokasi.setTextColor(Color.parseColor("#ffffff"))
                tvSetting.setTextColor(Color.parseColor("#ffffff"))

                tvNo.setTypeface(null, Typeface.BOLD)
                tvBatch.setTypeface(null, Typeface.BOLD)
                tvPelatihan.setTypeface(null, Typeface.BOLD)
                tvKuota.setTypeface(null, Typeface.BOLD)
                tvBiaya.setTypeface(null, Typeface.BOLD)
                tvTanggalPelaksanaan.setTypeface(null, Typeface.BOLD)
                tvTanggalMulaiDaftar.setTypeface(null, Typeface.BOLD)
                tvTanggalBerakhirDaftar.setTypeface(null, Typeface.BOLD)
                tvLokasi.setTypeface(null, Typeface.BOLD)
                tvSetting.setTypeface(null, Typeface.BOLD)
            } else {
                val daftarPelatihan : DaftarPelatihanModel = listDaftarPelatihan[(position - 1)]

                tvNo.text = "$position"
                val harga = rupiah.rupiah(daftarPelatihan.biaya!!.toLong())

                daftarPelatihan.let { it ->
                    tvBatch.text = "${it.batch}"
                    tvPelatihan.text = it.pelatihanModel!!.namaPelatihan
                    tvKuota.text = it.kuota.toString()
                    tvBiaya.text = harga
                    tvTanggalPelaksanaan.text = it.tglPelaksanaan
                    tvTanggalMulaiDaftar.text = it.tglMulaiDaftar
                    tvTanggalBerakhirDaftar.text = it.tglBerakhirDaftar
                    tvLokasi.text = it.lokasi

                    Glide.with(holder.itemView)
                        .load("${Constant.LOCATION_GAMBAR}${it.sertifikat}")
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.image_error)
                        .into(ivGambar)

                    tvSetting.text = ":::"
                }

                tvNo.setBackgroundResource(R.drawable.bg_table)
                tvBatch.setBackgroundResource(R.drawable.bg_table)
                tvPelatihan.setBackgroundResource(R.drawable.bg_table)
                tvKuota.setBackgroundResource(R.drawable.bg_table)
                tvBiaya.setBackgroundResource(R.drawable.bg_table)
                tvTanggalPelaksanaan.setBackgroundResource(R.drawable.bg_table)
                tvTanggalMulaiDaftar.setBackgroundResource(R.drawable.bg_table)
                tvTanggalBerakhirDaftar.setBackgroundResource(R.drawable.bg_table)
                tvLokasi.setBackgroundResource(R.drawable.bg_table)
                ivGambar.setBackgroundResource(R.drawable.bg_table)
                tvSetting.setBackgroundResource(R.drawable.bg_table)

                tvNo.setTextColor(Color.parseColor("#000000"))
                tvBatch.setTextColor(Color.parseColor("#000000"))
                tvPelatihan.setTextColor(Color.parseColor("#000000"))
                tvKuota.setTextColor(Color.parseColor("#000000"))
                tvBiaya.setTextColor(Color.parseColor("#000000"))
                tvTanggalPelaksanaan.setTextColor(Color.parseColor("#000000"))
                tvTanggalMulaiDaftar.setTextColor(Color.parseColor("#000000"))
                tvTanggalBerakhirDaftar.setTextColor(Color.parseColor("#000000"))
                tvLokasi.setTextColor(Color.parseColor("#000000"))
                tvSetting.setTextColor(Color.parseColor("#000000"))

                tvNo.setTypeface(null, Typeface.NORMAL)
                tvBatch.setTypeface(null, Typeface.NORMAL)
                tvPelatihan.setTypeface(null, Typeface.NORMAL)
                tvKuota.setTypeface(null, Typeface.NORMAL)
                tvBiaya.setTypeface(null, Typeface.NORMAL)
                tvTanggalPelaksanaan.setTypeface(null, Typeface.NORMAL)
                tvTanggalMulaiDaftar.setTypeface(null, Typeface.NORMAL)
                tvTanggalBerakhirDaftar.setTypeface(null, Typeface.NORMAL)
                tvLokasi.setTypeface(null, Typeface.NORMAL)
                tvSetting.setTypeface(null, Typeface.NORMAL)

                // Click
                tvPelatihan.setOnClickListener {
                    onClick.clickPenjelasan(
                        daftarPelatihan.pelatihanModel!!.namaPelatihan!!,
                        "Pelatihan"
                    )
                }
                tvBiaya.setOnClickListener {
                    onClick.clickPenjelasan(
                        harga,
                        "Biaya Pelatihan"
                    )
                }
                tvTanggalPelaksanaan.setOnClickListener {
                    onClick.clickPenjelasan(
                        daftarPelatihan.tglPelaksanaan!!,
                        "Tanggal Pelaksanaan"
                    )
                }
                tvTanggalMulaiDaftar.setOnClickListener {
                    onClick.clickPenjelasan(
                        daftarPelatihan.tglMulaiDaftar!!,
                        "Tanggal Mulai Daftar"
                    )
                }
                tvTanggalBerakhirDaftar.setOnClickListener {
                    onClick.clickPenjelasan(
                        daftarPelatihan.tglBerakhirDaftar!!,
                        "Tanggal Berakhir Daftar"
                    )
                }
                tvLokasi.setOnClickListener {
                    onClick.clickPenjelasan(
                        daftarPelatihan.lokasi!!,
                        "Lokasi Pelatihan"
                    )
                }
                ivGambar.setOnClickListener {
                    onClick.clickGambar(
                        daftarPelatihan.lokasi!!,
                        "Gambar"
                    )
                }
                tvSetting.setOnClickListener {
                    onClick.clickItemSetting(
                        daftarPelatihan, it
                    )
                }

            }
        }
    }
}