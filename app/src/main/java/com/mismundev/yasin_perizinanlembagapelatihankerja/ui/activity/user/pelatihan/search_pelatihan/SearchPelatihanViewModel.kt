package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.user.pelatihan.search_pelatihan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.database.api.ApiService
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.DaftarPelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchPelatihanViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private var _pelatihan = MutableLiveData<UIState<ArrayList<DaftarPelatihanModel>>>()
    private var _riwayat = MutableLiveData<UIState<ArrayList<DaftarPelatihanModel>>>()

    fun fetchPelatihan(){
        viewModelScope.launch(Dispatchers.IO) {
            _pelatihan.postValue(UIState.Loading)
            delay(500)
            try {
                val pelatihanTerdaftar = api.getPelatihan("")
                _pelatihan.postValue(UIState.Success(pelatihanTerdaftar))
            } catch (ex: Exception){
                _pelatihan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun fetchRiwayat(idUser: Int){
        viewModelScope.launch(Dispatchers.IO) {
            _riwayat.postValue(UIState.Loading)
            delay(500)
            try {
                val pelatihanTerdaftar = api.getRiwayat("", idUser)
                _riwayat.postValue(UIState.Success(pelatihanTerdaftar))
            } catch (ex: Exception){
                _riwayat.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun getPelatihan(): LiveData<UIState<ArrayList<DaftarPelatihanModel>>> = _pelatihan
    fun getRiwayat(): LiveData<UIState<ArrayList<DaftarPelatihanModel>>> = _riwayat
}