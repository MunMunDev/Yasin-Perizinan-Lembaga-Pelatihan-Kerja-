package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.user.payment

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
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    val _responseRegistrasiPembayaran = MutableLiveData<UIState<ResponseModel>>()

    fun postRegistrasiPembayaran(kodeUnik:String, idUser:Int, idDaftarPelatihan:Int){
        viewModelScope.launch(Dispatchers.IO){
            _responseRegistrasiPembayaran.postValue(UIState.Loading)
            delay(500)
            try {
                val dataRegistrasiPembayaran = api.postRegistrasiPembayaran(
                    "", kodeUnik, idUser, idDaftarPelatihan
                )
                _responseRegistrasiPembayaran.postValue(UIState.Success(dataRegistrasiPembayaran))
            } catch (ex: Exception){
                _responseRegistrasiPembayaran.postValue(UIState.Failure("Error pada : ${ex.message}"))
            }
        }
    }

    fun getRegistrasiPembayaran(): LiveData<UIState<ResponseModel>> = _responseRegistrasiPembayaran
}