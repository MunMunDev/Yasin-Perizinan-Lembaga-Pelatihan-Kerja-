package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.jenis_dokumen.list_daftar_pelatihan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.database.api.ApiService
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.DaftarPelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminJenisDokumenDaftarPelatihanViewModel  @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private var _daftarPelatihan = MutableLiveData<UIState<ArrayList<DaftarPelatihanModel>>>()

    fun getDaftarPelatihan(): LiveData<UIState<ArrayList<DaftarPelatihanModel>>> = _daftarPelatihan

    fun fetchDaftarPelatihan() {
        viewModelScope.launch(Dispatchers.IO) {
            _daftarPelatihan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val fetchDaftarPelatihan = api.getAllDaftarPelatihan("")
                _daftarPelatihan.postValue(UIState.Success(fetchDaftarPelatihan))
            } catch (ex: Exception) {
                _daftarPelatihan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }
}