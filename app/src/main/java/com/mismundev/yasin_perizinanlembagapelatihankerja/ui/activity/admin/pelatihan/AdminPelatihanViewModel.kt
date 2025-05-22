package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.pelatihan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.database.api.ApiService
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.JenisPelatihanModel
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
class AdminPelatihanViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private var _jenisPelatihan = MutableLiveData<UIState<ArrayList<JenisPelatihanModel>>>()
    private var _pelatihan = MutableLiveData<UIState<ArrayList<PelatihanModel>>>()
    private var _postTambahPelatihan = MutableLiveData<UIState<ResponseModel>>()
    private var _responsePostUpdatePelatihan = MutableLiveData<UIState<ResponseModel>>()

    fun fetchJenisPelatihan() {
        viewModelScope.launch(Dispatchers.IO) {
            _jenisPelatihan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val fetchPelatihan = api.getAllJenisPelatihan("")
                _jenisPelatihan.postValue(UIState.Success(fetchPelatihan))
            } catch (ex: Exception) {
                _jenisPelatihan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun fetchPelatihan() {
        viewModelScope.launch(Dispatchers.IO) {
            _pelatihan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val fetchPelatihan = api.getAllPelatihan("")
                _pelatihan.postValue(UIState.Success(fetchPelatihan))
            } catch (ex: Exception) {
                _pelatihan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun postTambahPelatihan(
        post: RequestBody, idJenisPelatihan: RequestBody, namaPelatihan: RequestBody,
        deskripsi: RequestBody, gambar: MultipartBody.Part
    ){
        viewModelScope.launch(Dispatchers.IO){
            _postTambahPelatihan.postValue(UIState.Loading)
            try {
                val data = api.postAddPelatihan(post, idJenisPelatihan, namaPelatihan, deskripsi, gambar)
                _postTambahPelatihan.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _postTambahPelatihan.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdatePelatihanAddImage(
        post: RequestBody, idPelatihan: RequestBody, idJenisPelatihan: RequestBody, namaPelatihan: RequestBody,
        deskripsi: RequestBody, gambar: MultipartBody.Part
    ){
        viewModelScope.launch(Dispatchers.IO){
            _responsePostUpdatePelatihan.postValue(UIState.Loading)
            try {
                val data = api.postUpdatePelatihanAddImage(post, idPelatihan, idJenisPelatihan, namaPelatihan, deskripsi, gambar)
                _responsePostUpdatePelatihan.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _responsePostUpdatePelatihan.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdatePelatihan(
        idPelatihan: Int, idJenisPelatihan: Int,
        namaPelatihan: String, deskripsi: String
    ){
        viewModelScope.launch(Dispatchers.IO){
            _responsePostUpdatePelatihan.postValue(UIState.Loading)
            try {
                val data = api.postUpdatePelatihan(
                    "", idPelatihan, idJenisPelatihan, namaPelatihan, deskripsi
                )
                _responsePostUpdatePelatihan.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _responsePostUpdatePelatihan.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getJenisPelatihan(): LiveData<UIState<ArrayList<JenisPelatihanModel>>> = _jenisPelatihan
    fun getPelatihan(): LiveData<UIState<ArrayList<PelatihanModel>>> = _pelatihan
    fun getTambahPelatihan(): LiveData<UIState<ResponseModel>> = _postTambahPelatihan
    fun getUpdatePelatihan(): LiveData<UIState<ResponseModel>> = _responsePostUpdatePelatihan

}