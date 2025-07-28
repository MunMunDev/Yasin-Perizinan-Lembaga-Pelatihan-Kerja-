package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.daftar_daftarDaftarPelatihan

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
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class AdminDaftarPelatihanViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private var _pelatihan = MutableLiveData<UIState<ArrayList<PelatihanModel>>>()
    private var _daftarDaftarPelatihan = MutableLiveData<UIState<ArrayList<DaftarPelatihanModel>>>()
    private var _postTambahDaftarPelatihan = MutableLiveData<UIState<ResponseModel>>()
    private var _responsePostUpdateDaftarPelatihan = MutableLiveData<UIState<ResponseModel>>()

    fun fetchPelatihan() {
        viewModelScope.launch(Dispatchers.IO) {
            _pelatihan.postValue(UIState.Loading)
            delay(500)
            try {
                val fetchDaftarPelatihan = api.getAllPelatihan("")
                _pelatihan.postValue(UIState.Success(fetchDaftarPelatihan))
            } catch (ex: Exception) {
                _pelatihan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun fetchDaftarPelatihan() {
        viewModelScope.launch(Dispatchers.IO) {
            _daftarDaftarPelatihan.postValue(UIState.Loading)
            delay(500)
            try {
                val fetchDaftarPelatihan = api.getAllDaftarPelatihan("")
                _daftarDaftarPelatihan.postValue(UIState.Success(fetchDaftarPelatihan))
            } catch (ex: Exception) {
                _daftarDaftarPelatihan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun postTambahDaftarPelatihan(
        post: RequestBody, idPelatihan: RequestBody, kuota: RequestBody,
        biaya: RequestBody, tglMulai: RequestBody, tglBerakhir: RequestBody,
        tglPelaksanaan: RequestBody, lokasi: RequestBody, sertifikat: MultipartBody.Part
    ){
        viewModelScope.launch(Dispatchers.IO){
            _postTambahDaftarPelatihan.postValue(UIState.Loading)
            try {
                val data = api.postAddDaftarPelatihan(
                    post, idPelatihan, kuota, biaya, tglMulai, tglBerakhir,
                    tglPelaksanaan, lokasi, sertifikat
                )
                _postTambahDaftarPelatihan.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _postTambahDaftarPelatihan.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdateDaftarPelatihanAddImage(
        post: RequestBody, idDaftarPelatihan: RequestBody, idPelatihan: RequestBody,
        kuota: RequestBody, biaya: RequestBody, tglMulai: RequestBody, tglBerakhir: RequestBody,
        tglPelaksanaan: RequestBody, lokasi: RequestBody, sertifikat: MultipartBody.Part
    ){
        viewModelScope.launch(Dispatchers.IO){
            _responsePostUpdateDaftarPelatihan.postValue(UIState.Loading)
            try {
                val data = api.postUpdateDaftarPelatihanAddImage(
                    post, idDaftarPelatihan, idPelatihan, kuota, biaya, tglMulai,
                    tglBerakhir, tglPelaksanaan, lokasi, sertifikat
                )
                _responsePostUpdateDaftarPelatihan.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _responsePostUpdateDaftarPelatihan.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdateDaftarPelatihan(
        idDaftarPelatihan: Int, idPelatihan: Int, kuota: Int, biaya: Int,
        tglMulai: String, tglBerakhir: String, tglPelaksanaan: String, lokasi: String
    ){
        viewModelScope.launch(Dispatchers.IO){
            _responsePostUpdateDaftarPelatihan.postValue(UIState.Loading)
            try {
                val data = api.postUpdateDaftarPelatihan(
                    "", idDaftarPelatihan, idPelatihan, kuota, biaya, tglMulai,
                    tglBerakhir, tglPelaksanaan, lokasi
                )
                _responsePostUpdateDaftarPelatihan.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _responsePostUpdateDaftarPelatihan.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getPelatihan(): LiveData<UIState<ArrayList<PelatihanModel>>> = _pelatihan
    fun getDaftarPelatihan(): LiveData<UIState<ArrayList<DaftarPelatihanModel>>> = _daftarDaftarPelatihan
    fun getTambahDaftarPelatihan(): LiveData<UIState<ResponseModel>> = _postTambahDaftarPelatihan
    fun getUpdateDaftarPelatihan(): LiveData<UIState<ResponseModel>> = _responsePostUpdateDaftarPelatihan

}