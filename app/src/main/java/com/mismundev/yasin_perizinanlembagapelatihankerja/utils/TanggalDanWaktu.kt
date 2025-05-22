package com.mismundev.yasin_perizinanlembagapelatihankerja.utils

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.widget.TextView
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@SuppressLint("SimpleDateFormat")
class TanggalDanWaktu {
    fun konversiBulan(bulan: String): String{
        val arrayBulan = arrayOf(
            "Januari",
            "Februari",
            "Maret",
            "April",
            "Mei",
            "Juni",
            "Juli",
            "Agustus",
            "September",
            "Oktober",
            "November",
            "Desember"
        )

        val splitBulan = bulan.split("-")
        val valueBulan = "${splitBulan[2]} ${arrayBulan[(splitBulan[1].toInt()-1)]} ${splitBulan[0]}"

        return valueBulan

    }
    fun konversiBulanSingkatan(bulan: String): String{
        val arrayBulan = arrayOf(
            "Jan",
            "Feb",
            "Mar",
            "Apr",
            "Mei",
            "Juni",
            "Juli",
            "Agust",
            "Sep",
            "Okt",
            "Nov",
            "Des"
        )

        val splitBulan = bulan.split("-")
        val valueBulan = "${splitBulan[2]} ${arrayBulan[(splitBulan[1].toInt()-1)]} ${splitBulan[0]}"

        return valueBulan
    }

    fun waktuNoSecond(waktu: String): String{
        val arrayWaktu = waktu.split(":")
        return "${arrayWaktu[0]}:${arrayWaktu[1]}"
    }

    fun konversiBulanDanWaktu(tanggalDanWaktu: String): String{
        val splitTanggalDanWaktu = tanggalDanWaktu.split(" ")
        val tanggal = konversiBulan(splitTanggalDanWaktu[0])
        val waktu = waktuNoSecond(splitTanggalDanWaktu[1])

        return "$tanggal $waktu"
    }

    fun konversiBulanSingkatanDanWaktu(tanggalDanWaktu: String): String{
        val splitTanggalDanWaktu = tanggalDanWaktu.split(" ")
        val tanggal = konversiBulanSingkatan(splitTanggalDanWaktu[0])
        val waktu = waktuNoSecond(splitTanggalDanWaktu[1])

        return "$tanggal $waktu"
    }

    fun tanggalSekarangZonaMakassar():String{
        var date = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val makassarZone  = ZoneId.of("Asia/Makassar")
            val makassarTanggal = LocalDate.now(makassarZone)
            val tanggal = makassarTanggal
            date = "$tanggal"
        } else {
            val makassarTimeZone = TimeZone.getTimeZone("Asia/Makassar")
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            dateFormat.timeZone = makassarTimeZone
            val currentDate = Date()
            val makassarDate = dateFormat.format(currentDate)
            date = makassarDate
        }
        return date
    }

    fun waktuSekarangZonaMakassar():String{
        var time = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val makassarZone  = ZoneId.of("Asia/Makassar")
            val makassarTime = LocalTime.now(makassarZone)
            val waktu = makassarTime.toString().split(".")
            time = waktu[0]

        } else {
            val makassarTimeZone = TimeZone.getTimeZone("Asia/Makassar")
            val timeFormat = SimpleDateFormat("HH:mm:ss")
            timeFormat.timeZone = makassarTimeZone
            val currentTime = Date()
            val makassarTime = timeFormat.format(currentTime)
            time = makassarTime
        }
        return time
    }

    fun tanggalDanWaktuZonaMakassar(): String{
        return tanggalSekarangZonaMakassar()+" "+waktuSekarangZonaMakassar()
    }

    fun selectedDate(tanggal:String, tv: TextView, context: Context){
        var arrayTanggalSekarang = tanggal.split("-")

        val c = Calendar.getInstance()
        val year = arrayTanggalSekarang[0].toInt()
        val month = arrayTanggalSekarang[1].toInt()-1   // Kurang 1, diambil dari array
        val day = arrayTanggalSekarang[2].toInt()


        val mDatePicker = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            var tahun = year.toString()
            var bulan = (monthOfYear+1).toString()
            var tanggal = dayOfMonth.toString()
            if(bulan.length==1){
                bulan = "0$bulan"
            }
            if(tanggal.length==1){
                tanggal = "0$tanggal"
            }

            val tanggalFull = "$tahun-$bulan-$tanggal"
            tv.text = tanggalFull

        }, year, month, day)
        mDatePicker.setTitle("Pilih Tanggal")
        mDatePicker.show()

    }

    fun selectedTime(waktu:String, tv: TextView, context: Context){
        var valueWaktu = ""
        var arrayWaktu = waktu.split(":")
//        val hour = 12
//        val minute = 0
        val hour = arrayWaktu[0].toInt()
        val minute = arrayWaktu[1].toInt()
        val mTimePicker: TimePickerDialog = TimePickerDialog(context,
            { timePicker, selectedHour, selectedMinute ->
                var menit = selectedMinute.toString()
                var jam = selectedHour.toString()
                if(jam.length==1){
                    jam = "0$selectedHour"
                }
                if(menit.length==1){
                    menit = "0$selectedMinute"
                }
                valueWaktu = "$jam:$menit:00"

                tv.text = valueWaktu

            },
            hour,
            minute,
            true
        )
        mTimePicker.setTitle("Pilih Waktu")
        mTimePicker.show()
    }

    fun selectedDateTime(tanggalTempt:String, tv: TextView, context: Context){
        var arrayTanggalWaktuSekarang = tanggalTempt.split(" ")

        var arrayTanggalSekarang = arrayTanggalWaktuSekarang[0].split("-")
        var arrayWaktuSekarang = arrayTanggalWaktuSekarang[0].split("-")

        val c = Calendar.getInstance()
        val year = arrayTanggalSekarang[0].toInt()
        val month = arrayTanggalSekarang[1].toInt()-1   // Kurang 1, diambil dari array
        val day = arrayTanggalSekarang[2].toInt()

        val mDatePicker = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            var tahun = year.toString()
            var bulan = (monthOfYear+1).toString()
            var tanggal = dayOfMonth.toString()
            if(bulan.length==1){
                bulan = "0$bulan"
            }
            if(tanggal.length==1){
                tanggal = "0$tanggal"
            }

            val tanggalFull = "$tahun-$bulan-$tanggal"

            // Waktu
            var valueWaktu = ""
            val hour = arrayWaktuSekarang[0].toInt()
            val minute = arrayWaktuSekarang[1].toInt()
            val mTimePicker: TimePickerDialog = TimePickerDialog(context,
                { timePicker, selectedHour, selectedMinute ->
                    var menit = selectedMinute.toString()
                    var jam = selectedHour.toString()
                    if(jam.length==1){
                        jam = "0$selectedHour"
                    }
                    if(menit.length==1){
                        menit = "0$selectedMinute"
                    }
                    valueWaktu = "$jam:$menit:00"

                    tv.text = "$tanggalFull $valueWaktu"
                },
                hour,
                minute,
                true
            )
            mTimePicker.setTitle("Pilih Waktu")
            mTimePicker.show()

        }, year, month, day)
        mDatePicker.setTitle("Pilih Tanggal")
        mDatePicker.show()
    }

    fun cekTanggalMulaiPendaftaran(
        tglMulaiPendaftaran: String, format: String = "yyyy-MM-dd"
    ): Boolean{
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        val date = dateFormat.parse(tglMulaiPendaftaran)
        return if(date!!.before(Date())){
            true
        } else if(tglMulaiPendaftaran == tanggalSekarangZonaMakassar()){
            true
        } else{
            false
        }
    }

    fun cekTanggalBerakhirPendaftaran(
        tglBerakhirPendaftaran: String, format: String = "yyyy-MM-dd"
    ): Boolean{
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        val date = dateFormat.parse(tglBerakhirPendaftaran)
        return if(date!!.after(Date())){
            true
        } else if(tglBerakhirPendaftaran == tanggalSekarangZonaMakassar()){
            true
        } else{
            false
        }
    }

}