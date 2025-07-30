package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.jenis_pelatihan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.database.api.ApiService
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.JenisPelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.ResponseModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminJenisPelatihanViewModel @Inject constructor(
   private val api: ApiService
): ViewModel() {
    private var _jenisPelatihan = MutableLiveData<UIState<ArrayList<JenisPelatihanModel>>>()
    private var _postTambahJenisPelatihan = MutableLiveData<UIState<ResponseModel>>()
    private var _responsePostUpdateJenisPelatihan = MutableLiveData<UIState<ResponseModel>>()

    fun fetchJenisPelatihan() {
        viewModelScope.launch(Dispatchers.IO) {
            _jenisPelatihan.postValue(UIState.Loading)
            delay(500)
            try {
                val fetchJenisPelatihan = api.getAllJenisPelatihan("")
                _jenisPelatihan.postValue(UIState.Success(fetchJenisPelatihan))
            } catch (ex: Exception) {
                _jenisPelatihan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun postTambahJenisPelatihan(
        jenisPelatihan: String
    ){
        viewModelScope.launch(Dispatchers.IO){
            _postTambahJenisPelatihan.postValue(UIState.Loading)
            try {
                val data = api.postAddJenisPelatihan("", jenisPelatihan)
                _postTambahJenisPelatihan.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _postTambahJenisPelatihan.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdateJenisPelatihan(
        idJenisPelatihan: Int, jenisPelatihan: String
    ){
        viewModelScope.launch(Dispatchers.IO){
            _responsePostUpdateJenisPelatihan.postValue(UIState.Loading)
            try {
                val data = api.postUpdateJenisPelatihan("", idJenisPelatihan, jenisPelatihan)
                _responsePostUpdateJenisPelatihan.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _responsePostUpdateJenisPelatihan.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getJenisPelatihan(): LiveData<UIState<ArrayList<JenisPelatihanModel>>> = _jenisPelatihan
    fun getTambahJenisPelatihan(): LiveData<UIState<ResponseModel>> = _postTambahJenisPelatihan
    fun getUpdateJenisPelatihan(): LiveData<UIState<ResponseModel>> = _responsePostUpdateJenisPelatihan

}