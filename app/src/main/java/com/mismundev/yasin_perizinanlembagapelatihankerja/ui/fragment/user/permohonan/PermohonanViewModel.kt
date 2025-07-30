package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.fragment.user.permohonan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.database.api.ApiService
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.PermohonanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PermohonanViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private var _permohonan = MutableLiveData<UIState<ArrayList<PermohonanModel>>>()

    fun fetchPermohonan(idUser: Int){
        viewModelScope.launch(Dispatchers.IO) {
            _permohonan.postValue(UIState.Loading)
            delay(500)
            try {
                val pelatihanTerdaftar = api.getPermohonanUser("", idUser)
                _permohonan.postValue(UIState.Success(pelatihanTerdaftar))
            } catch (ex: Exception){
                _permohonan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun getPermohonan(): LiveData<UIState<ArrayList<PermohonanModel>>> = _permohonan
}