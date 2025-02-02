package com.mismundev.yasin_perizinanlembagapelatihankerja.adapter.user

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mismundev.yasin_perizinanlembagapelatihankerja.R
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.DaftarPelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ListPelatihanBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.Constant
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.KonversiRupiah
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.OnClickItem
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.TanggalDanWaktu

class PelatihanAdapter(
    private var listPelatihan: ArrayList<DaftarPelatihanModel>,
    private var click: OnClickItem.ClickPelatihan
): RecyclerView.Adapter<PelatihanAdapter.ViewHolder>() {

    private val rupiah = KonversiRupiah()
    private val tanggalDanWaktu = TanggalDanWaktu()
    private var tempPelatihan = listPelatihan

    @SuppressLint("NotifyDataSetChanged")
    fun searchData(kata: String){
        val vKata = kata.toLowerCase().trim()
        var data = listPelatihan.filter {
            it.pelatihanModel!!.jenisPelatihanModel!!.jenisPelatihan!!.lowercase().trim().contains(vKata)
        } as ArrayList
        if(data.size==0){
            data = listPelatihan.filter {
                it.pelatihanModel!!.jenisPelatihanModel!!.jenisPelatihan!!.lowercase().trim().contains(vKata)
            } as ArrayList
        }
        Log.d("DetailTAG", "searchData: ${data.size}")
        tempPelatihan = data
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ListPelatihanBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListPelatihanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return tempPelatihan.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val daftarPelatihan = tempPelatihan[position]
        holder.apply {
            binding.apply {
                Glide.with(itemView.context)
                    .load("${Constant.BASE_URL}${Constant.LOCATION_GAMBAR}${daftarPelatihan.pelatihanModel!!.gambar}") // URL Gambar
                    .error(R.drawable.img_pelatihan)
                    .into(ivGambarPelatihan) // imageView mana yang akan diterapkan

                daftarPelatihan.apply {
                    pelatihanModel?.let { pelatihan ->
                        tvJenisPelatihan.text = pelatihan.jenisPelatihanModel!!.jenisPelatihan
                        tvPelatihan.text = pelatihan.namaPelatihan
                    }
                    val harga = rupiah.rupiah(biaya!!.toLong())
                    val tanggalWaktu = tglPelaksanaan!!.split(" ")
                    val tanggal = tanggalDanWaktu.konversiBulanSingkatan(tanggalWaktu[0])
                    val waktu = tanggalDanWaktu.waktuNoSecond(tanggalWaktu[1])

                    tvHarga.text = harga
                    tvTglPelatihan.text = "$tanggal $waktu"
                    itemView.setOnClickListener {
                        click.clickPelatihan(daftarPelatihan)
                    }
                }

            }
        }
    }
}