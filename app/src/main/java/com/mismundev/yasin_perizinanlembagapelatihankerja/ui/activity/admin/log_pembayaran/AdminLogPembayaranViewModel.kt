package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.log_pembayaran

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.database.api.ApiService
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.PembayaranModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminLogPembayaranViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private var _pembayaran = MutableLiveData<UIState<ArrayList<PembayaranModel>>>()

    fun fetchPembayaran() {
        viewModelScope.launch(Dispatchers.IO) {
            _pembayaran.postValue(UIState.Loading)
            delay(1_000)
            try {
                val fetchPembayaran = api.getAllPembayaran("")
                _pembayaran.postValue(UIState.Success(fetchPembayaran))
            } catch (ex: Exception) {
                _pembayaran.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }
    fun getPembayaran(): LiveData<UIState<ArrayList<PembayaranModel>>> = _pembayaran

}