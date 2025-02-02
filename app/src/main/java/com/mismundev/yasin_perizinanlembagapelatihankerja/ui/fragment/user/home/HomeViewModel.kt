package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.fragment.user.home

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
class HomeViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private var _pelatihanTerdaftar = MutableLiveData<UIState<ArrayList<DaftarPelatihanModel>>>()
    private var _pelatihan = MutableLiveData<UIState<ArrayList<DaftarPelatihanModel>>>()

    fun fetchPelatihanTerdaftar(idUser: Int){
        viewModelScope.launch(Dispatchers.IO) {
            _pelatihanTerdaftar.postValue(UIState.Loading)
            delay(1_000)
            try {
                val pelatihanTerdaftar = api.getPelatihanTerdaftar("", idUser)
                _pelatihanTerdaftar.postValue(UIState.Success(pelatihanTerdaftar))
            } catch (ex: Exception){
                _pelatihanTerdaftar.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun fetchPelatihan(){
        viewModelScope.launch(Dispatchers.IO) {
            _pelatihan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val pelatihanTerdaftar = api.getPelatihan("")
                _pelatihan.postValue(UIState.Success(pelatihanTerdaftar))
            } catch (ex: Exception){
                _pelatihan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun getPelatihanTerdaftar(): LiveData<UIState<ArrayList<DaftarPelatihanModel>>> = _pelatihanTerdaftar
    fun getPelatihan(): LiveData<UIState<ArrayList<DaftarPelatihanModel>>> = _pelatihan
}