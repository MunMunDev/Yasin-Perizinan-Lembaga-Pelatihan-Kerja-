package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.user.permohonan

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
class DetailPermohonanViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private var _dokumenPermohonan = MutableLiveData<UIState<ArrayList<DokumenModel>>>()
    private var _responsePostUpdateDokumenPermohonan = MutableLiveData<UIState<ResponseModel>>()

    fun getDokumenPermohonan(): LiveData<UIState<ArrayList<DokumenModel>>> = _dokumenPermohonan
    fun getUpdatePermohonan(): LiveData<UIState<ResponseModel>> = _responsePostUpdateDokumenPermohonan

    fun fetchDokumenPermohonan(idPermohonan: Int, idUser: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _dokumenPermohonan.postValue(UIState.Loading)
            delay(500)
            try {
                val fetchPermohonan = api.getDokumenPermohonanUser(
                    "", idPermohonan, idUser
                )
                _dokumenPermohonan.postValue(UIState.Success(fetchPermohonan))
            } catch (ex: Exception) {
                _dokumenPermohonan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun postUpdateDokumenPermohonanFile(
        post: RequestBody, idDokumen: RequestBody, file: MultipartBody.Part
    ){
        viewModelScope.launch(Dispatchers.IO){
            _responsePostUpdateDokumenPermohonan.postValue(UIState.Loading)
            try {
                val data = api.postUpdateDokumenPermohonanUser(
                    post, idDokumen, file
                )
                _responsePostUpdateDokumenPermohonan.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _responsePostUpdateDokumenPermohonan.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

}