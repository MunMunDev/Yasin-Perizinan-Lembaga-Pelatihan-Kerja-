package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.user.main

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mismundev.yasin_perizinanlembagapelatihankerja.R
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ActivityMainBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.fragment.user.account.AccountFragment
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.fragment.user.history.HistoryFragment
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.fragment.user.home.HomeFragment
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.fragment.user.pelatihan.PelatihanFragment
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.SharedPreferencesLogin
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferencesLogin
    private lateinit var scaleAnimation: ScaleAnimation
    private var checkFragmentPosition = 0   // 0 Home, 1 pelatihan, 2 history, 3 account
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurationSharedPreferences()
        setScaleAnimation()
        setFragment(HomeFragment())
        setButtonBottomBar()
    }

    private fun configurationSharedPreferences() {
        sharedPreferences = SharedPreferencesLogin(this@MainActivity)
    }

    private fun setButtonBottomBar() {
        binding.icBottom.apply {
            btnHome.setOnClickListener {
                clickHome()
            }
            btnPelatihan.setOnClickListener {
                clickPelatihan()
            }
            btnRiwayat.setOnClickListener {
                clickRiwayat()
            }
            btnAccount.setOnClickListener {
                clickAccount()
            }
        }
    }

    fun clickHome() {
        binding.icBottom.apply {
            // text color
            tvHome.setTextColor(resources.getColor(R.color.primaryColor))
            tvPelatihan.setTextColor(resources.getColor(R.color.textColorBlack))
            tvRiwayat.setTextColor(resources.getColor(R.color.textColorBlack))
            tvAccount.setTextColor(resources.getColor(R.color.textColorBlack))

            // image view visibility
            ivHome.setImageResource(R.drawable.icon_home_active)
            ivPelatihan.setImageResource(R.drawable.icon_pelatihan)
            ivRiwayat.setImageResource(R.drawable.icon_riwayat)
            ivAccount.setImageResource(R.drawable.icon_akun)

//            // button view visibility
//            btnHome.setBackgroundResource(R.drawable.bg_btn_bottom_bar)
//            btnPelatihan.setBackgroundResource(R.drawable.bg_btn_bottom_bar_transparent)
//            btnRiwayat.setBackgroundResource(R.drawable.bg_btn_bottom_bar_transparent)
//            btnAccount.setBackgroundResource(R.drawable.bg_btn_bottom_bar_transparent)

//            btnHome.startAnimation(scaleAnimation)

            setFragment(HomeFragment())
            checkFragmentPosition = 0
        }
    }

    fun clickPelatihan() {
        binding.icBottom.apply {
            // text color
            tvHome.setTextColor(resources.getColor(R.color.textColorBlack))
            tvPelatihan.setTextColor(resources.getColor(R.color.primaryColor))
            tvRiwayat.setTextColor(resources.getColor(R.color.textColorBlack))
            tvAccount.setTextColor(resources.getColor(R.color.textColorBlack))

            // image view visibility
            ivHome.setImageResource(R.drawable.icon_home)
            ivPelatihan.setImageResource(R.drawable.icon_pelatihan_active)
            ivRiwayat.setImageResource(R.drawable.icon_riwayat)
            ivAccount.setImageResource(R.drawable.icon_akun)

//            // button view visibility
//            btnHome.setBackgroundResource(R.drawable.bg_btn_bottom_bar_transparent)
//            btnPelatihan.setBackgroundResource(R.drawable.bg_btn_bottom_bar)
//            btnRiwayat.setBackgroundResource(R.drawable.bg_btn_bottom_bar_transparent)
//            btnAccount.setBackgroundResource(R.drawable.bg_btn_bottom_bar_transparent)

//            btnPelatihan.startAnimation(scaleAnimation)

            setFragment(PelatihanFragment())
            checkFragmentPosition = 1
        }
    }

    fun clickRiwayat() {
        binding.icBottom.apply {
            // text color
            tvHome.setTextColor(resources.getColor(R.color.textColorBlack))
            tvPelatihan.setTextColor(resources.getColor(R.color.textColorBlack))
            tvRiwayat.setTextColor(resources.getColor(R.color.primaryColor))
            tvAccount.setTextColor(resources.getColor(R.color.textColorBlack))

            // image view visibility
            ivHome.setImageResource(R.drawable.icon_home)
            ivPelatihan.setImageResource(R.drawable.icon_pelatihan)
            ivRiwayat.setImageResource(R.drawable.icon_riwayat_active)
            ivAccount.setImageResource(R.drawable.icon_akun)

            // button view visibility
//            btnHome.setBackgroundResource(R.drawable.bg_btn_bottom_bar_transparent)
//            btnPelatihan.setBackgroundResource(R.drawable.bg_btn_bottom_bar_transparent)
//            btnRiwayat.setBackgroundResource(R.drawable.bg_btn_bottom_bar)
//            btnAccount.setBackgroundResource(R.drawable.bg_btn_bottom_bar_transparent)

//            btnRiwayat.startAnimation(scaleAnimation)

            setFragment(HistoryFragment())
            checkFragmentPosition = 2
        }
    }

    fun clickAccount() {
        binding.icBottom.apply {
            // text color
            tvHome.setTextColor(resources.getColor(R.color.textColorBlack))
            tvPelatihan.setTextColor(resources.getColor(R.color.textColorBlack))
            tvRiwayat.setTextColor(resources.getColor(R.color.textColorBlack))
            tvAccount.setTextColor(resources.getColor(R.color.primaryColor))

            // image view visibility
            ivHome.setImageResource(R.drawable.icon_home)
            ivPelatihan.setImageResource(R.drawable.icon_pelatihan)
            ivRiwayat.setImageResource(R.drawable.icon_riwayat)
            ivAccount.setImageResource(R.drawable.icon_akun_active)

            // button view visibility
//            btnHome.setBackgroundResource(R.drawable.bg_btn_bottom_bar_transparent)
//            btnPelatihan.setBackgroundResource(R.drawable.bg_btn_bottom_bar_transparent)
//            btnRiwayat.setBackgroundResource(R.drawable.bg_btn_bottom_bar_transparent)
//            btnAccount.setBackgroundResource(R.drawable.bg_btn_bottom_bar)

//            btnRiwayat.startAnimation(scaleAnimation)

            setFragment(AccountFragment())
            checkFragmentPosition = 3
        }
    }

    @SuppressLint("CommitTransaction")
    private fun setFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.apply {
            replace(R.id.flMain, fragment)
            commit()
        }
    }

    private fun setScaleAnimation(){
        scaleAnimation = ScaleAnimation(
            0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF,
            0.0f, Animation.RELATIVE_TO_SELF, 0.0f
        )
        scaleAnimation.apply {
            duration = 200
            fillAfter = true
        }
    }

    private var tapDuaKali = false
    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if(checkFragmentPosition == 0){
            if (tapDuaKali){
                super.onBackPressed()
            }
            tapDuaKali = true
            Toast.makeText(this@MainActivity, "Tekan Sekali Lagi untuk keluar", Toast.LENGTH_SHORT).show()

            Handler().postDelayed({
                tapDuaKali = false
            }, 2000)
        } else{
            clickHome()
        }
    }
}
