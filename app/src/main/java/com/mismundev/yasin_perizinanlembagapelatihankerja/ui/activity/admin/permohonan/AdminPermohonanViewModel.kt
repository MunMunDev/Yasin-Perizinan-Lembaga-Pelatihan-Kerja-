package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.permohonan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.database.api.ApiService
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.DaftarPelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.PermohonanModel
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
class AdminPermohonanViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private var _jenisPelatihan = MutableLiveData<UIState<ArrayList<PermohonanModel>>>()
    private var _user = MutableLiveData<UIState<ArrayList<UsersModel>>>()
    private var _daftarPelatihan = MutableLiveData<UIState<ArrayList<DaftarPelatihanModel>>>()
    private var _postTambahPermohonan = MutableLiveData<UIState<ResponseModel>>()
    private var _responsePostUpdatePermohonan = MutableLiveData<UIState<ResponseModel>>()

    fun getPermohonan(): LiveData<UIState<ArrayList<PermohonanModel>>> = _jenisPelatihan
    fun getUser(): LiveData<UIState<ArrayList<UsersModel>>> = _user
    fun getDaftarPelatihan(): LiveData<UIState<ArrayList<DaftarPelatihanModel>>> = _daftarPelatihan
    fun getTambahPermohonan(): LiveData<UIState<ResponseModel>> = _postTambahPermohonan
    fun getUpdatePermohonan(): LiveData<UIState<ResponseModel>> = _responsePostUpdatePermohonan

    fun fetchPermohonan() {
        viewModelScope.launch(Dispatchers.IO) {
            _jenisPelatihan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val fetchPermohonan = api.getPermohonan("")
                _jenisPelatihan.postValue(UIState.Success(fetchPermohonan))
            } catch (ex: Exception) {
                _jenisPelatihan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    private fun fetchUser() {
        viewModelScope.launch(Dispatchers.IO) {
            _user.postValue(UIState.Loading)
            delay(1_000)
            try {
                val data = api.getAllUser("")
                _user.postValue(UIState.Success(data))
            } catch (ex: Exception) {
                _user.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    private fun fetchDaftaPelatihan() {
        viewModelScope.launch(Dispatchers.IO) {
            _daftarPelatihan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val data = api.getAllDaftarPelatihan("")
                _daftarPelatihan.postValue(UIState.Success(data))
            } catch (ex: Exception) {
                _daftarPelatihan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun allData(){
        fetchPermohonan()
        fetchUser()
        fetchDaftaPelatihan()
    }

//    fun postTambahPermohonan(
//        jenisPelatihan: String
//    ){
//        viewModelScope.launch(Dispatchers.IO){
//            _postTambahPermohonan.postValue(UIState.Loading)
//            try {
//                val data = api.postAddPermohonan("", jenisPelatihan)
//                _postTambahPermohonan.postValue(UIState.Success(data))
//            } catch (ex: Exception){
//                _postTambahPermohonan.postValue(UIState.Failure("Error: ${ex.message}"))
//            }
//        }
//    }

    fun postUpdatePermohonan(
        idPermohonan: Int, idUser: Int, idDaftarPelatihan: Int, tanggal: String, waktu: String, idKeterangan: Int
    ){
        viewModelScope.launch(Dispatchers.IO){
            _responsePostUpdatePermohonan.postValue(UIState.Loading)
            try {
                val data = api.postUpdatePermohonan(
                    "", idPermohonan, idUser, idDaftarPelatihan, tanggal, waktu, idKeterangan
                )
                _responsePostUpdatePermohonan.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _responsePostUpdatePermohonan.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdatePermohonanImage(
        post:RequestBody, idPermohonan: RequestBody, idUser: RequestBody, idDaftarPelatihan: RequestBody,
        tanggal: RequestBody, waktu: RequestBody, file: MultipartBody.Part
    ){
        viewModelScope.launch(Dispatchers.IO){
            _responsePostUpdatePermohonan.postValue(UIState.Loading)
            try {
                val data = api.postUpdatePermohonanAddImage(
                    post, idPermohonan, idUser, idDaftarPelatihan, tanggal, waktu, file
                )
                _responsePostUpdatePermohonan.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _responsePostUpdatePermohonan.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }
}