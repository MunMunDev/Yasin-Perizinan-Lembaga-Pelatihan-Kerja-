package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ActivityAdminMainBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.daftar_pelatihan.AdminDaftarPelatihanActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.jenis_pelatihan.AdminJenisPelatihanActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.log_pembayaran.AdminLogPembayaranActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.pelatihan.AdminPelatihanActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.pendaftar.AdminPendaftarActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.permohonan.AdminPermohonanActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.user.AdminUserActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.KontrolNavigationDrawer
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.SharedPreferencesLogin
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminMainBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSharedPreferencesLogin()
        setAppNavBar()
        setKontrolNavigationDrawer()
        setButton()
    }

    private fun setSharedPreferencesLogin() {
        sharedPreferencesLogin = SharedPreferencesLogin(this@AdminMainActivity)
    }

    private fun setAppNavBar() {
        binding.myAppBar.apply {
            ivBack.visibility = View.GONE
            ivNavDrawer.visibility = View.VISIBLE

//            tvTitle.text = "Halaman Wedding Organizer"
            tvTitle.text = sharedPreferencesLogin.getNama()
        }
    }

    private fun setKontrolNavigationDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminMainActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, myAppBar.ivNavDrawer, this@AdminMainActivity)
        }
    }

    private fun setButton() {
        binding.apply {
            cvPermohonan.setOnClickListener {
                startActivity(Intent(this@AdminMainActivity, AdminPermohonanActivity::class.java))
            }
            cvJenisPelatihan.setOnClickListener {
                startActivity(Intent(this@AdminMainActivity, AdminJenisPelatihanActivity::class.java))
            }
            cvPelatihan.setOnClickListener {
                startActivity(Intent(this@AdminMainActivity, AdminPelatihanActivity::class.java))
            }
            cvDaftarPelatihan.setOnClickListener {
                startActivity(Intent(this@AdminMainActivity, AdminDaftarPelatihanActivity::class.java))
            }
            cvPendaftar.setOnClickListener {
                startActivity(Intent(this@AdminMainActivity, AdminPendaftarActivity::class.java))
            }
            cvLogPembayaran.setOnClickListener {
                startActivity(Intent(this@AdminMainActivity, AdminLogPembayaranActivity::class.java))
            }
            cvAkunUser.setOnClickListener {
                startActivity(Intent(this@AdminMainActivity, AdminUserActivity::class.java))
            }
        }
    }
}