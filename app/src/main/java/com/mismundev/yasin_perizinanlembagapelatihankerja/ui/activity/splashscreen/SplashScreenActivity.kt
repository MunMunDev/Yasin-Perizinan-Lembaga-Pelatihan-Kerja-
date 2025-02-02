package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.mismundev.yasin_perizinanlembagapelatihankerja.R
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ActivitySplashScreenBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.login.LoginActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.user.main.MainActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.SharedPreferencesLogin

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    private lateinit var binding : ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        sharedPreferencesLogin = SharedPreferencesLogin(this@SplashScreenActivity)

        Handler(Looper.getMainLooper()).postDelayed({
//            sharedPreferencesLogin.setLogin(0, "","", "","", "", "")
//            startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
//            finish()
            if(sharedPreferencesLogin.getIdUser() == 0){
                startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                finish()
            }
            else{
                if(sharedPreferencesLogin.getSebagai() == "user"){
                    startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                    finish()
                } else if(sharedPreferencesLogin.getSebagai() == "admin"){
//                    startActivity(Intent(this@SplashScreenActivity, AdminMainActivity::class.java))
//                    finish()
                }
            }
        }, 3000)
    }
}