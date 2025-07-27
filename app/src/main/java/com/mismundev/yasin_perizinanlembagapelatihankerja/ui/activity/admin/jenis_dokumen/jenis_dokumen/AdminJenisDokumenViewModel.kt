package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.jenis_dokumen.jenis_dokumen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.database.api.ApiService
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.JenisDokumenModel
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
class AdminJenisDokumenViewModel @Inject constructor(
    private val api: ApiService
): ViewModel(){
    private var _dokumenPermohonan = MutableLiveData<UIState<ArrayList<JenisDokumenModel>>>()
    private var _postTambahJenisDokumen = MutableLiveData<UIState<ResponseModel>>()
    private var _responsePostUpdateJenisDokumen = MutableLiveData<UIState<ResponseModel>>()

    fun getJenisDokumen(): LiveData<UIState<ArrayList<JenisDokumenModel>>> = _dokumenPermohonan
    fun getTambahPermohonan(): LiveData<UIState<ResponseModel>> = _postTambahJenisDokumen
    fun getUpdatePermohonan(): LiveData<UIState<ResponseModel>> = _responsePostUpdateJenisDokumen
    fun getDeletePermohonan(): LiveData<UIState<ResponseModel>> = _responsePostUpdateJenisDokumen

    fun fetchJenisDokumen(idJenisPelatihan: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _dokumenPermohonan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val fetchPermohonan = api.getJenisDokumen("", idJenisPelatihan)
                _dokumenPermohonan.postValue(UIState.Success(fetchPermohonan))
            } catch (ex: Exception) {
                _dokumenPermohonan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun postTambahJenisDokumen(
        idDaftarPelatihan: Int, jenisDokumen: String,
        ekstensi: String,
    ){
        viewModelScope.launch(Dispatchers.IO){
            _postTambahJenisDokumen.postValue(UIState.Loading)
            try {
                val data = api.postTambahJenisDokumen(
                    "", idDaftarPelatihan, jenisDokumen, ekstensi
                )
                _postTambahJenisDokumen.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _postTambahJenisDokumen.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdateJenisDokumen(
        idJenisDokumen: Int, jenisDokumen: String, ekstensi: String
    ){
        viewModelScope.launch(Dispatchers.IO){
            _responsePostUpdateJenisDokumen.postValue(UIState.Loading)
            try {
                val data = api.postUpdateJenisDokumen(
                    "", idJenisDokumen, jenisDokumen, ekstensi
                )
                _responsePostUpdateJenisDokumen.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _responsePostUpdateJenisDokumen.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postDeleteJenisDokumen(
        idJenisDokumen: Int
    ){
        viewModelScope.launch(Dispatchers.IO){
            _responsePostUpdateJenisDokumen.postValue(UIState.Loading)
            try {
                val data = api.postDeleteJenisDokumen(
                    "", idJenisDokumen
                )
                _responsePostUpdateJenisDokumen.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _responsePostUpdateJenisDokumen.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }
}