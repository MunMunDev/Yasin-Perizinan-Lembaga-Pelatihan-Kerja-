package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.viewModels
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.UsersModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ActivityLoginBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.register.RegisterActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.user.main.MainActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.LoadingAlertDialog
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.SharedPreferencesLogin
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.network.CheckNetwork
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    @Inject lateinit var checkNetwork: CheckNetwork
    private var network = false
    @Inject
    lateinit var loading : LoadingAlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkUseNetwork()
        konfigurationUtils()
        button()
        getData()
    }

    private fun checkUseNetwork(){
        network = checkNetwork.isNetworkAvailable(this@LoginActivity)
    }

    private fun konfigurationUtils() {
        sharedPreferencesLogin = SharedPreferencesLogin(this@LoginActivity)
    }

    private fun button(){
        btnDaftar()
        btnLogin()
    }

    private fun btnLogin() {
        binding.apply {
            btnLogin.setOnClickListener{
                if(network){
                    if(etEmail.text.isNotEmpty() && etPassword.text.isNotEmpty()){
                        cekUsers(etEmail.text.toString(), etPassword.text.toString())
                    }
                    else{
                        if(etEmail.text.isEmpty()){
                            etEmail.error = "Masukkan Email"
                        }
                        if(etPassword.text.isEmpty()){
                            etPassword.error = "Masukkan Password"
                        }
                    }
                } else{
                    Toast.makeText(this@LoginActivity, "Cek jaringan", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun btnDaftar() {
        binding.tvDaftar.setOnClickListener{
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

    private fun cekUsers(email: String, password: String) {
        viewModel.fetchDataUser(email, password)
    }

    private fun getData(){
        viewModel.getDataUser().observe(this@LoginActivity){result ->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@LoginActivity)
                is UIState.Success-> setSuccessDataUser(result.data)
                is UIState.Failure -> setFailureDataUser(result.message)
            }
        }
    }

    private fun setFailureDataUser(message: String) {
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessDataUser(data: ArrayList<UsersModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            successFetchLogin(data)
        } else{
            Toast.makeText(this@LoginActivity, "Data tidak ditemukan \nPastikan Email dan Password Benar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun successFetchLogin(userModel: ArrayList<UsersModel>){
        loading.alertDialogCancel()
        var valueIdUser = 0
        userModel[0].idUser?.let {
            valueIdUser = it
        }
        val valueNama = userModel[0].nama.toString()
        val valueAlamat = userModel[0].alamat.toString()
        val valueNomorHp = userModel[0].nomorHp.toString()
        val valueEmail = userModel[0].email.toString()
        val valuePassword = userModel[0].password.toString()
        val valueSebagai= userModel[0].sebagai.toString()

        try{
            Toast.makeText(this@LoginActivity, "Login Berhasil", Toast.LENGTH_SHORT).show()

            sharedPreferencesLogin.setLogin(valueIdUser, valueNama, valueAlamat, valueNomorHp, valueEmail, valuePassword, valueSebagai)
            if(valueSebagai=="user"){
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            } else{
//                Toast.makeText(this@LoginActivity, "Selamat Datang Admin", Toast.LENGTH_SHORT).show()
//                startActivity(Intent(this@LoginActivity, AdminMainActivity::class.java))
            }
            finish()
        } catch (ex: Exception){
            Toast.makeText(this@LoginActivity, "gagal: $ex", Toast.LENGTH_SHORT).show()
        }
    }

    private var tapDuaKali = false
    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (tapDuaKali){
            super.onBackPressed()
        }
        tapDuaKali = true
        Toast.makeText(this@LoginActivity, "Tekan Sekali Lagi untuk keluar", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({
            tapDuaKali = false
        }, 2000)

    }
}