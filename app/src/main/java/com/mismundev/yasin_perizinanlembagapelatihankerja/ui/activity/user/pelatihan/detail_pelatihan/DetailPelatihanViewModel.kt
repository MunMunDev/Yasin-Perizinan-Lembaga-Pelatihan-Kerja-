package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.user.pelatihan.detail_pelatihan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.database.api.ApiService
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.DaftarPelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.PendaftarModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailPelatihanViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private var _telahDaftar = MutableLiveData<UIState<ArrayList<PendaftarModel>>>()
    private var _pelatihan = MutableLiveData<UIState<ArrayList<DaftarPelatihanModel>>>()

    fun fetchTelahDaftarPelatihan(idDaftarPelatihan: Int, idUser: Int){
        viewModelScope.launch(Dispatchers.IO) {
            _telahDaftar.postValue(UIState.Loading)
            delay(1_000)
            try {
                val pelatihanTerdaftar = api.getTelahDaftarPelatihan("", idDaftarPelatihan, idUser)
                _telahDaftar.postValue(UIState.Success(pelatihanTerdaftar))
            } catch (ex: Exception){
                _telahDaftar.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun fetchDetailPelatihan(idDaftarPelatihan: Int){
        viewModelScope.launch(Dispatchers.IO) {
            _pelatihan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val pelatihanTerdaftar = api.getDetailPelatihan("", idDaftarPelatihan)
                _pelatihan.postValue(UIState.Success(pelatihanTerdaftar))
            } catch (ex: Exception){
                _pelatihan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun getTelahDaftarPelatihan(): LiveData<UIState<ArrayList<PendaftarModel>>> = _telahDaftar
    fun getDetailPelatihan(): LiveData<UIState<ArrayList<DaftarPelatihanModel>>> = _pelatihan
}