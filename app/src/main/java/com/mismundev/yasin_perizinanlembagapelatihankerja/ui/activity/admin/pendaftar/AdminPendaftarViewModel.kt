package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.pendaftar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.database.api.ApiService
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.DaftarPelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.PendaftarModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.ResponseModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.UsersModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class AdminPendaftarViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private var _daftarUser = MutableLiveData<UIState<ArrayList<UsersModel>>>()
    private var _daftarPelatihan = MutableLiveData<UIState<ArrayList<DaftarPelatihanModel>>>()
    private var _pendaftar = MutableLiveData<UIState<ArrayList<PendaftarModel>>>()
    private var _postTambahPendaftar = MutableLiveData<UIState<ResponseModel>>()
    private var _responsePostUpdatePendaftar = MutableLiveData<UIState<ResponseModel>>()
    private var _responsePostDeletePendaftar = MutableLiveData<UIState<ResponseModel>>()

    fun fetchUser() {
        viewModelScope.launch(Dispatchers.IO) {
            _daftarUser.postValue(UIState.Loading)
            delay(1_000)
            try {
                val fetchPendaftar = api.getAllUser("")
                _daftarUser.postValue(UIState.Success(fetchPendaftar))
            } catch (ex: Exception) {
                _daftarUser.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun fetchDaftarPelatihan() {
        viewModelScope.launch(Dispatchers.IO) {
            _daftarPelatihan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val fetchPendaftar = api.getAllDaftarPelatihan("")
                _daftarPelatihan.postValue(UIState.Success(fetchPendaftar))
            } catch (ex: Exception) {
                _daftarPelatihan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun fetchPendaftar() {
        viewModelScope.launch(Dispatchers.IO) {
            _pendaftar.postValue(UIState.Loading)
            delay(1_000)
            try {
                val fetchPendaftar = api.getPendaftar("")
                _pendaftar.postValue(UIState.Success(fetchPendaftar))
            } catch (ex: Exception) {
                _pendaftar.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun postTambahPendaftar(
        idUser: Int, idDaftarPelatihan: Int, ket: String
    ){
        viewModelScope.launch(Dispatchers.IO){
            _postTambahPendaftar.postValue(UIState.Loading)
            try {
                val data = api.postAddAdminPendaftar("", idUser, idDaftarPelatihan, ket)
                _postTambahPendaftar.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _postTambahPendaftar.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdatePendaftar(
        idPendaftar: Int, idUser: Int, idDaftarPelatihan: Int, ket: String
    ){
        viewModelScope.launch(Dispatchers.IO){
            _responsePostUpdatePendaftar.postValue(UIState.Loading)
            try {
                val data = api.postUpdateAdminPendaftar("", idPendaftar, idUser, idDaftarPelatihan, ket)
                _responsePostUpdatePendaftar.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _responsePostUpdatePendaftar.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postDeletePendaftar(
        idPendaftar: Int
    ){
        viewModelScope.launch(Dispatchers.IO){
            _responsePostDeletePendaftar.postValue(UIState.Loading)
            try {
                val data = api.postDeleteAdminPendaftar(
                    "", idPendaftar
                )
                _responsePostDeletePendaftar.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _responsePostDeletePendaftar.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getUser(): LiveData<UIState<ArrayList<UsersModel>>> = _daftarUser
    fun getDaftarPelatihan(): LiveData<UIState<ArrayList<DaftarPelatihanModel>>> = _daftarPelatihan
    fun getPendaftar(): LiveData<UIState<ArrayList<PendaftarModel>>> = _pendaftar
    fun getTambahPendaftar(): LiveData<UIState<ResponseModel>> = _postTambahPendaftar
    fun getUpdatePendaftar(): LiveData<UIState<ResponseModel>> = _responsePostUpdatePendaftar
    fun getDeletePendaftar(): LiveData<UIState<ResponseModel>> = _responsePostDeletePendaftar

}