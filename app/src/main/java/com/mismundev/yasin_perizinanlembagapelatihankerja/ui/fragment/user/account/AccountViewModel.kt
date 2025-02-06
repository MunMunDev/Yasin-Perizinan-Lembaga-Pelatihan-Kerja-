package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.fragment.user.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.database.api.ApiService
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.ResponseModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    var _responsePostUpdateUser = MutableLiveData<UIState<ResponseModel>>()

    fun postUpdateUser(
        idUser:String, nama: String, alamat: String, nomorHp: String,
        email: String, password: String, emailLama:String
    ){
        viewModelScope.launch(Dispatchers.IO){
            _responsePostUpdateUser.postValue(UIState.Loading)
            try {
                val data = api.postUpdateUser("", idUser, nama, alamat, nomorHp, email, password, emailLama)
                _responsePostUpdateUser.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _responsePostUpdateUser.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getUpdateData(): LiveData<UIState<ResponseModel>> = _responsePostUpdateUser
}