package com.mismundev.yasin_perizinanlembagapelatihankerja.utils

import android.content.Context
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.UsersModel

class SharedPreferencesLogin(val context: Context) {
    private val keyIdUser = "keyIdUser"
    private val keyNama = "keyNama"
    private val keyAlamat = "keyAlamat"
    private val keyNomorHp = "keyNomorHp"
    private val keyEmail = "keyEmail"
    private val keyPassword = "keyPassword"
    private val keySebagai = "keySebagai"

    private var sharedPref = context.getSharedPreferences("sharedpreference_login", Context.MODE_PRIVATE)
    private var editPref = sharedPref.edit()

    fun setLogin(id_user:Int, nama:String, alamat:String, nomorHp:String, email:String, password:String, sebagai:String){
        editPref.apply{
            putInt(keyIdUser, id_user)
            putString(keyNama, nama)
            putString(keyNomorHp, nomorHp)
            putString(keyAlamat, alamat)
            putString(keyEmail, email)
            putString(keyPassword, password)
            putString(keySebagai, sebagai)

            apply()
        }
    }

    fun getIdUser(): Int{
        return sharedPref.getInt(keyIdUser, 0)
    }
    fun getNama():String{
        return sharedPref.getString(keyNama, "").toString()
    }
    fun getAlamat():String{
        return sharedPref.getString(keyAlamat, "").toString()
    }
    fun getNomorHp():String{
        return sharedPref.getString(keyNomorHp, "").toString()
    }
    fun getEmail():String{
        return sharedPref.getString(keyEmail, "").toString()
    }
    fun getPassword(): String{
        return sharedPref.getString(keyPassword, "").toString()
    }
    fun getSebagai(): String{
        return sharedPref.getString(keySebagai, "").toString()
    }

}