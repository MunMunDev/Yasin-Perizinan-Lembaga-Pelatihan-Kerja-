package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.fragment.user.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.database.api.ApiService
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.DaftarPelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.PelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.ResponseModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RiwayatViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private var _pelatihan = MutableLiveData<UIState<ArrayList<DaftarPelatihanModel>>>()

    fun fetchRiwayat(idUser: Int){
        viewModelScope.launch(Dispatchers.IO) {
            _pelatihan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val pelatihanTerdaftar = api.getRiwayat("", idUser)
                _pelatihan.postValue(UIState.Success(pelatihanTerdaftar))
            } catch (ex: Exception){
                _pelatihan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun getRiwayat(): LiveData<UIState<ArrayList<DaftarPelatihanModel>>> = _pelatihan
}