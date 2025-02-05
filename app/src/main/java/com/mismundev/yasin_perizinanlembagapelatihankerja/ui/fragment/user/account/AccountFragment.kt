package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.fragment.user.account

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.ResponseModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.UsersModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.AlertDialogAkunBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.FragmentAccountBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.LoadingAlertDialog
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.SharedPreferencesLogin
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AccountFragment : Fragment() {
    private lateinit var binding : FragmentAccountBinding
    private val viewModel: AccountViewModel by viewModels()
    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferencesLogin
    @Inject
    lateinit var loading: LoadingAlertDialog
    private var tempUser: UsersModel = UsersModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(layoutInflater)
        context = requireContext().applicationContext

        setStartShimmerProfile()
        setSharedPreferences()
        setData()
        button()
        getPostUpdateData()
        setSwipeRefreshLayout()

        return binding.root
    }

    private fun setSwipeRefreshLayout() {
        binding.swipeRefresh.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeRefresh.isRefreshing = false
                setStartShimmerProfile()
                setData()
            }, 2000)
        }
    }

    private fun setSharedPreferences() {
        sharedPreferences = SharedPreferencesLogin(context)
    }

    private fun setData(){
        binding.apply {
//            etNama.text = sharedPreferences.getNama()
//            etNomorHp.text = sharedPreferences.getNomorHp()
//            etAlamat.text = sharedPreferences.getAlamat()
//            etEmail.text = sharedPreferences.getEmail()
//            etPassword.text = sharedPreferences.getPassword()
//
//            setStopShimmerProfile()
            Handler(Looper.getMainLooper()).postDelayed({
                etNama.text = sharedPreferences.getNama()
                etNomorHp.text = sharedPreferences.getNomorHp()
                etAlamat.text = sharedPreferences.getAlamat()
                etEmail.text = sharedPreferences.getEmail()
                etPassword.text = sharedPreferences.getPassword()

                setStopShimmerProfile()
            }, 1000)
        }
    }

    private fun button() {
        binding.btnUbahData.setOnClickListener {
            setDialogUpdateData()
        }
    }

    private fun setDialogUpdateData() {
        val view = AlertDialogAkunBinding.inflate(layoutInflater)
        val alertDialog = AlertDialog.Builder(requireActivity())
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            etEditNama.setText(sharedPreferences.getNama())
            etEditAlamat.setText(sharedPreferences.getAlamat())
            etEditNomorHp.setText(sharedPreferences.getNomorHp())
            etEditEmail.setText(sharedPreferences.getEmail())
            etEditPassword.setText(sharedPreferences.getPassword())

            btnSimpan.setOnClickListener {
                var cek = false
                if(etEditNama.toString().isEmpty()){
                    etEditNama.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if(etEditNomorHp.toString().isEmpty()){
                    etEditNomorHp.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if(etEditEmail.toString().isEmpty()){
                    etEditEmail.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if(etEditPassword.toString().isEmpty()){
                    etEditPassword.error = "Tidak Boleh Kosong"
                    cek = true
                }

                if(!cek){
                    tempUser = UsersModel(
                        sharedPreferences.getIdUser(),
                        etEditNama.text.toString(),
                        etEditNomorHp.text.toString(),
                        etEditAlamat.text.toString(),
                        etEditEmail.text.toString(),
                        etEditPassword.text.toString(),
                        sharedPreferences.getEmail()
                    )
                    postUpdateData(
                        sharedPreferences.getIdUser().toString(),
                        etEditNama.text.toString(),
                        etEditAlamat.text.toString(),
                        etEditNomorHp.text.toString(),
                        etEditEmail.text.toString(),
                        etEditPassword.text.toString(),
                        sharedPreferences.getEmail()
                    )

                    dialogInputan.dismiss()
                }

            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postUpdateData(idUser: String, nama: String, alamat: String, nomorHp: String, email: String, password: String, emailLama: String) {
        viewModel.postUpdateUser(idUser, nama, alamat, nomorHp, email, password, emailLama)
    }

    private fun getPostUpdateData(){
        viewModel.getUpdateData().observe(viewLifecycleOwner){result->
            when(result){
                is UIState.Loading-> setStartShimmerProfile()
                is UIState.Success-> setSuccessUpdateData(result.data)
                is UIState.Failure-> setFailureUpdateData(result.message)
            }
        }
    }

    private fun setFailureUpdateData(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessUpdateData(data: ResponseModel?) {
        if(data != null){
            if(data.status == "0"){
                Toast.makeText(context, "Berhasil Update Akun", Toast.LENGTH_SHORT).show()
                sharedPreferences.setLogin(
                    tempUser.idUser!!,
                    tempUser.nama!!,
                    tempUser.nomorHp!!,
                    tempUser.alamat!!,
                    tempUser.email!!,
                    tempUser.password!!,
                    "user"
                )
                tempUser = UsersModel()
                setData()
            } else{
                Toast.makeText(context, data.message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(context, "Gagal: Ada kesalahan di API", Toast.LENGTH_SHORT).show()
        }
        setStopShimmerProfile()
    }

    private fun setStartShimmerProfile(){
        binding.apply {
            smProfile.startShimmer()
            lProfile.visibility = View.GONE
            smProfile.visibility = View.VISIBLE
        }
    }

    private fun setStopShimmerProfile(){
        binding.apply {
            smProfile.stopShimmer()
            lProfile.visibility = View.VISIBLE
            smProfile.visibility = View.GONE
        }
    }
}