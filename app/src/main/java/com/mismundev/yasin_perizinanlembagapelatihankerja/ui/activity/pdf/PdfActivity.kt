package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.pdf

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mismundev.yasin_perizinanlembagapelatihankerja.R
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ActivityPdfBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.Constant
import com.rajat.pdfviewer.PdfEngine
import com.rajat.pdfviewer.PdfQuality

class PdfActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPdfBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDataSebelumnya()
    }

    private fun setButtonDownload(link: String) {
        binding.apply {
            btnDownload.setOnClickListener {
                val intent = Intent(Intent.ACTION_WEB_SEARCH)
                intent.putExtra(
                    SearchManager.QUERY,
                    "https://e-portofolio.web.id/pelatihan-kerja/download-dokumen.php?link=$link"
                )
                startActivity(intent)
            }
        }
    }

    private fun setDataSebelumnya() {
        val i = intent.extras
        if(i != null){
            val file = i.getString("file")!!
            binding.apply {
                pdfView.initWithUrl(
                    url = "${Constant.LOCATION_DOKUMEN}$file",
                    PdfQuality.ENHANCED,
                    PdfEngine.GOOGLE,
                )

                setButtonDownload(file)
            }
        }
    }
}