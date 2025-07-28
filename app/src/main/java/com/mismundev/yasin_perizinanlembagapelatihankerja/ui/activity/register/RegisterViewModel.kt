package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.database.api.ApiService
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.ResponseModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val api: ApiService
) : ViewModel(){

    private var _registerUser = MutableLiveData<UIState<ResponseModel>>()

    fun postRegisterUser(nama:String, alamat:String, nomorHp:String, email:String, password:String, sebagai:String){
        viewModelScope.launch(Dispatchers.IO) {
            _registerUser.postValue(UIState.Loading)
            delay(500)
            try {
                val registerUser = api.addUser("", nama, alamat, nomorHp, email, password, sebagai)
                _registerUser.postValue(UIState.Success(registerUser))
            } catch (ex: Exception){
                _registerUser.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun getRegisterUser(): LiveData<UIState<ResponseModel>> = _registerUser
}