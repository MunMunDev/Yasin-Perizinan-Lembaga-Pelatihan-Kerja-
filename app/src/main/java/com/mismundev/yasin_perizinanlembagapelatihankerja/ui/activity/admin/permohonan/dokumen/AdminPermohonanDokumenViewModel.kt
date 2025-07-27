package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.permohonan.dokumen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.database.api.ApiService
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.DokumenModel
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
class AdminPermohonanDokumenViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private var _dokumenPermohonan = MutableLiveData<UIState<ArrayList<DokumenModel>>>()
    private var _postTambahDokumenPermohonan = MutableLiveData<UIState<ResponseModel>>()
    private var _responsePostUpdateDokumenPermohonan = MutableLiveData<UIState<ResponseModel>>()

    fun getDokumenPermohonan(): LiveData<UIState<ArrayList<DokumenModel>>> = _dokumenPermohonan
    fun getTambahPermohonan(): LiveData<UIState<ResponseModel>> = _postTambahDokumenPermohonan
    fun getUpdatePermohonan(): LiveData<UIState<ResponseModel>> = _responsePostUpdateDokumenPermohonan
    fun getDeletePermohonan(): LiveData<UIState<ResponseModel>> = _responsePostUpdateDokumenPermohonan

    fun fetchDokumenPermohonan(idPermohonan: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _dokumenPermohonan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val fetchPermohonan = api.getDokumenPermohonan("", idPermohonan)
                _dokumenPermohonan.postValue(UIState.Success(fetchPermohonan))
            } catch (ex: Exception) {
                _dokumenPermohonan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun postTambahDokumenPermohonan(
        post: RequestBody, idPermohonan: RequestBody, idDaftarPelatihan: RequestBody,
        jenisDokumen: RequestBody, file: MultipartBody.Part
    ){
        viewModelScope.launch(Dispatchers.IO){
            _postTambahDokumenPermohonan.postValue(UIState.Loading)
            try {
                val data = api.postTambahDokumenPermohonanAddImage(
                    post, idPermohonan, idDaftarPelatihan, jenisDokumen, file
                )
                _postTambahDokumenPermohonan.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _postTambahDokumenPermohonan.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdateDokumenPermohonan(
        idDokumen: Int, jenisDokumen: String
    ){
        viewModelScope.launch(Dispatchers.IO){
            _responsePostUpdateDokumenPermohonan.postValue(UIState.Loading)
            try {
                val data = api.postUpdateDokumenPermohonan(
                    "", idDokumen, jenisDokumen
                )
                _responsePostUpdateDokumenPermohonan.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _responsePostUpdateDokumenPermohonan.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdateDokumenPermohonanImage(
        post: RequestBody, idDokumen: RequestBody, jenisDokumen: RequestBody, file: MultipartBody.Part
    ){
        viewModelScope.launch(Dispatchers.IO){
            _responsePostUpdateDokumenPermohonan.postValue(UIState.Loading)
            try {
                val data = api.postUpdateDokumenPermohonanAddImage(
                    post, idDokumen, jenisDokumen, file
                )
                _responsePostUpdateDokumenPermohonan.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _responsePostUpdateDokumenPermohonan.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postDeleteDokumenPermohonan(
        idDokumen: Int
    ){
        viewModelScope.launch(Dispatchers.IO){
            _responsePostUpdateDokumenPermohonan.postValue(UIState.Loading)
            try {
                val data = api.postDeleteDokumenPermohonan(
                    "", idDokumen
                )
                _responsePostUpdateDokumenPermohonan.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _responsePostUpdateDokumenPermohonan.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

}