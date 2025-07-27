package com.mismundev.yasin_perizinanlembagapelatihankerja.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.user.main.MainActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.R
import com.mismundev.yasin_perizinanlembagapelatihankerja.adapter.admin.AdminUserAdapter
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.daftar_pelatihan.AdminDaftarPelatihanActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.jenis_pelatihan.AdminJenisPelatihanActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.log_pembayaran.AdminLogPembayaranActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.main.AdminMainActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.pelatihan.AdminPelatihanActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.pendaftar.AdminPendaftarActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.permohonan.AdminPermohonanActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.user.AdminUserActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.login.LoginActivity

class KontrolNavigationDrawer(var context: Context) {
    var sharedPreferences = SharedPreferencesLogin(context)
    fun cekSebagai(navigation: com.google.android.material.navigation.NavigationView){
        if(sharedPreferences.getSebagai() == "user"){
            navigation.menu.clear()
            navigation.inflateMenu(R.menu.nav_menu_user)
        }
        else if(sharedPreferences.getSebagai() == "admin"){
            navigation.menu.clear()
            navigation.inflateMenu(R.menu.nav_menu_admin)
        }
    }
    @SuppressLint("ResourceAsColor")
    fun onClickItemNavigationDrawer(navigation: com.google.android.material.navigation.NavigationView, navigationLayout: DrawerLayout, igNavigation:ImageView, activity: Activity){
        navigation.setNavigationItemSelectedListener {
            if(sharedPreferences.getSebagai() == "user") {
                when (it.itemId) {
                    R.id.userNavDrawerHome -> {
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }

                    R.id.userBtnKeluar -> {
                        logout(activity)
                    }
                }
            }
            else if(sharedPreferences.getSebagai() == "admin"){
                when(it.itemId){
                    R.id.adminNavDrawerHome -> {
                        val intent = Intent(context, AdminMainActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerPermohonan -> {
                        val intent = Intent(context, AdminPermohonanActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerJenisPelatihan -> {
                        val intent = Intent(context, AdminJenisPelatihanActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerPelatihan -> {
                        val intent = Intent(context, AdminPelatihanActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerDaftarPelatihan -> {
                        val intent = Intent(context, AdminDaftarPelatihanActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerPendaftar -> {
                        val intent = Intent(context, AdminPendaftarActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerLogPembayaran -> {
                        val intent = Intent(context, AdminLogPembayaranActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerAkunUser -> {
                        val intent = Intent(context, AdminUserActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminBtnKeluar ->{
                        logout(activity)
                    }
                }

            }
            navigationLayout.setBackgroundColor(R.color.white)
            navigationLayout.closeDrawer(GravityCompat.START)
            true
        }
        // garis 3 navigasi
        igNavigation.setOnClickListener {
            navigationLayout.openDrawer(GravityCompat.START)
        }
    }

    fun logout(activity: Activity){
        sharedPreferences.setLogin(0, "","", "","", "", "")
        context.startActivity(Intent(context, LoginActivity::class.java))
        activity.finish()
    }

}