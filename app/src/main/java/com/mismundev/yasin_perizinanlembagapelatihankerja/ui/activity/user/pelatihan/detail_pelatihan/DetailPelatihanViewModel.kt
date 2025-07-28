package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.user.pelatihan.detail_pelatihan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.database.api.ApiService
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.DaftarPelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.PendaftarModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.PermohonanModel
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
class DetailPelatihanViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private var _telahDaftar = MutableLiveData<UIState<PendaftarModel>>()
    private var _pelatihan = MutableLiveData<UIState<DaftarPelatihanModel>>()
    private var _permohonan = MutableLiveData<UIState<PermohonanModel>>()
    private var _responseDaftarPelatihan = MutableLiveData<UIState<ResponseModel>>()
    private var _responsePostPermohonan = MutableLiveData<UIState<ResponseModel>>()
    private var _responseDokumenPost = MutableLiveData<UIState<ResponseModel>>()

    fun getTelahDaftarPelatihan(): LiveData<UIState<PendaftarModel>> = _telahDaftar
    fun getPelatihan(): LiveData<UIState<DaftarPelatihanModel>> = _pelatihan
    fun getPermohonan(): LiveData<UIState<PermohonanModel>> = _permohonan
    fun getDaftarPelatihan(): LiveData<UIState<ResponseModel>> = _responseDaftarPelatihan
    fun getPostPermohonan(): LiveData<UIState<ResponseModel>> = _responsePostPermohonan
    fun getDokumenPermohonan(): LiveData<UIState<ResponseModel>> = _responseDokumenPost

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

    fun fetchPelatihan(idDaftarPelatihan: Int){
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

    fun fetchPermohonan(idDaftarPelatihan: Int, idUser: Int){
        viewModelScope.launch(Dispatchers.IO) {
            _permohonan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val pelatihanTerdaftar = api.getPermohonanDetailPelatihan("", idDaftarPelatihan, idUser)
                _permohonan.postValue(UIState.Success(pelatihanTerdaftar))
            } catch (ex: Exception){
                _permohonan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun postDaftarPelatihan(idDaftarPelatihan: Int, idUser: Int){
        viewModelScope.launch(Dispatchers.IO) {
            _responseDaftarPelatihan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val pelatihanTerdaftar = api.postDaftarPelatihan("", idDaftarPelatihan, idUser)
                _responseDaftarPelatihan.postValue(UIState.Success(pelatihanTerdaftar))
            } catch (ex: Exception){
                _responseDaftarPelatihan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun postPermohonan(
        idUser: Int, idPelatihan: Int,
        idDaftarPelatihan: Int
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _responsePostPermohonan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val pelatihanTerdaftar = api.postPermohonan(
                    "", idUser, idPelatihan, idDaftarPelatihan
                )
                _responsePostPermohonan.postValue(UIState.Success(pelatihanTerdaftar))
            } catch (ex: Exception){
                _responsePostPermohonan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun postDokumenPermohonan(
        post: RequestBody, idPermohonan:RequestBody, idDaftarPelatihan: RequestBody,
        jenisDokumen: RequestBody, ekstensi: RequestBody, file: MultipartBody.Part
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _responseDokumenPost.postValue(UIState.Loading)
            delay(1_000)
            try {
                val pelatihanTerdaftar = api.postDokumenPost(
                    post, idPermohonan, idDaftarPelatihan, jenisDokumen, ekstensi, file
                )
                _responseDokumenPost.postValue(UIState.Success(pelatihanTerdaftar))
            } catch (ex: Exception){
                _responseDokumenPost.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

}