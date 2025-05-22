package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.user

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.mismundev.yasin_perizinanlembagapelatihankerja.R
import com.mismundev.yasin_perizinanlembagapelatihankerja.adapter.admin.AdminUserAdapter
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.ResponseModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.UsersModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.ActivityAdminUserBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.AlertDialogAkunBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.AlertDialogKeteranganBinding
import com.mismundev.yasin_perizinanlembagapelatihankerja.ui.activity.admin.main.AdminMainActivity
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.KontrolNavigationDrawer
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.LoadingAlertDialog
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.OnClickItem
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.SharedPreferencesLogin
import com.mismundev.yasin_perizinanlembagapelatihankerja.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList
import javax.inject.Inject

@AndroidEntryPoint
class AdminUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminUserBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    private val viewModel: AdminUserViewModel by viewModels()
    @Inject
    lateinit var loading: LoadingAlertDialog
    private lateinit var adapter: AdminUserAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        setButton()
        setSharedPreferencesLogin()
        fetchUser()
        getUser()
        getPostTambahData()
        getPostUpdateData()
    }

    private fun setSharedPreferencesLogin() {
        sharedPreferencesLogin = SharedPreferencesLogin((this@AdminUserActivity))
    }

    @SuppressLint("SetTextI18n")
    private fun setKontrolNavigationDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminUserActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, myAppBar.ivNavDrawer, this@AdminUserActivity)

            myAppBar.tvTitle.text = "Akun User"
        }
    }

    private fun setButton() {
        binding.apply {
            btnTambah.setOnClickListener {
                setShowDialogTambah()
            }
        }
    }



    private fun setShowDialogTambah() {
        val view = AlertDialogAkunBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminUserActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            btnSimpan.setOnClickListener {
                var cek = false
                if (etEditNama.toString().isEmpty()) {
                    etEditNama.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if (etEditNomorHp.toString().isEmpty()) {
                    etEditNomorHp.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if (etEditEmail.toString().isEmpty()) {
                    etEditEmail.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if (etEditPassword.toString().isEmpty()) {
                    etEditPassword.error = "Tidak Boleh Kosong"
                    cek = true
                }

                if (!cek) {
                    postTambahDataUser(
                        etEditNama.text.toString(),
                        etEditAlamat.text.toString(),
                        etEditNomorHp.text.toString(),
                        etEditEmail.text.toString(),
                        etEditPassword.text.toString(),
                    )

                    dialogInputan.dismiss()
                }
            }

            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postTambahDataUser(nama: String, alamat: String, nomorHp: String, email: String, password: String){
        viewModel.postTambahUser(
            nama, alamat, nomorHp, email, password
        )
    }

    private fun getPostTambahData(){
        viewModel.getTambahUser().observe(this@AdminUserActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminUserActivity)
                is UIState.Success-> setSuccessTambahData(result.data)
                is UIState.Failure-> setFailureTambahData(result.message)
            }
        }
    }

    private fun setFailureTambahData(message: String) {
        Toast.makeText(this@AdminUserActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessTambahData(data: ResponseModel?) {
        if(data != null){
            if(data.status == "0"){
                fetchUser()
            } else{
                Toast.makeText(this@AdminUserActivity, data.message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminUserActivity, "Gagal: Ada kesalahan di API", Toast.LENGTH_SHORT).show()
        }
        loading.alertDialogCancel()
    }

    private fun fetchUser() {
        viewModel.fetchUser()
    }
    private fun getUser(){
        viewModel.getUser().observe(this@AdminUserActivity){result->
            when(result){
                is UIState.Loading-> setStartShimmer()
                is UIState.Failure-> setFailureFetchUser(result.message)
                is UIState.Success-> setSuccessFetchUser(result.data)
            }
        }
    }

    private fun setFailureFetchUser(message: String) {
        setStopShimmer()
        Toast.makeText(this@AdminUserActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessFetchUser(data: ArrayList<UsersModel>) {
        setStopShimmer()
        if(data.isNotEmpty()){
            setAdapterUser(data)
        } else{
            Toast.makeText(this@AdminUserActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapterUser(data: ArrayList<UsersModel>) {
        adapter = AdminUserAdapter(data, object : OnClickItem.ClickAdminUser{

            override fun clickItemNama(title: String, nama: String) {
                showKeterangan(title, nama)
            }

            override fun clickItemAlamat(title: String, alamat: String) {
                showKeterangan(title, alamat)
            }

            override fun clickItemEmail(title: String, email: String) {
                showKeterangan(title, email)
            }

            override fun clickItemSetting(wo: UsersModel, it: View) {
                val popupMenu = PopupMenu(this@AdminUserActivity, it)
                popupMenu.inflate(R.menu.popup_edit)
                popupMenu.setOnMenuItemClickListener(object :
                    PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                        when (menuItem!!.itemId) {
                            R.id.edit -> {
                                setShowDialogEdit(wo)
                                return true
                            }
                        }
                        return true
                    }

                })
                popupMenu.show()
            }

        })

        binding.apply {
            rvUser.layoutManager = LinearLayoutManager(this@AdminUserActivity, LinearLayoutManager.VERTICAL, false)
            rvUser.adapter = adapter
        }
    }

    private fun showKeterangan(title:String, keterangan: String) {
        val view = AlertDialogKeteranganBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminUserActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKeterangan.text = title
            tvBodyKeterangan.text = keterangan

            btnClose.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun setShowDialogEdit(user: UsersModel) {
        val view = AlertDialogAkunBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminUserActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {

            etEditNama.setText(user.nama)
            etEditAlamat.setText(user.alamat)
            etEditNomorHp.setText(user.nomorHp)
            etEditEmail.setText(user.email)
            etEditPassword.setText(user.password)

            btnSimpan.setOnClickListener {
                var cek = false
                if (etEditNama.toString().isEmpty()) {
                    etEditNama.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if (etEditNomorHp.toString().isEmpty()) {
                    etEditNomorHp.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if (etEditEmail.toString().isEmpty()) {
                    etEditEmail.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if (etEditPassword.toString().isEmpty()) {
                    etEditPassword.error = "Tidak Boleh Kosong"
                    cek = true
                }

                if (!cek) {
                    postUpdateDataUser(
                        user.idUser!!,
                        etEditNama.text.toString(),
                        etEditAlamat.text.toString(),
                        etEditNomorHp.text.toString(),
                        etEditEmail.text.toString(),
                        etEditPassword.text.toString(),
                        user.email!!,
                    )

                    dialogInputan.dismiss()
                }
            }

            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postUpdateDataUser(idUser: Int, nama: String, alamat: String, nomorHp: String, email: String, password: String, emailLama: String){
        viewModel.postUpdateUser(
            idUser, nama, alamat, nomorHp, email,
            password,  emailLama
        )
    }

    private fun getPostUpdateData(){
        viewModel.getUpdateUser().observe(this@AdminUserActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminUserActivity)
                is UIState.Success-> setSuccessUpdateData(result.data)
                is UIState.Failure-> setFailureUpdateData(result.message)
            }
        }
    }

    private fun setFailureUpdateData(message: String) {
        Toast.makeText(this@AdminUserActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessUpdateData(data: ResponseModel?) {
        if(data != null){
            if(data.status == "0"){
                fetchUser()
            } else{
                Toast.makeText(this@AdminUserActivity, data.message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminUserActivity, "Gagal: Ada kesalahan di API", Toast.LENGTH_SHORT).show()
        }
        loading.alertDialogCancel()
    }


    private fun setStartShimmer(){
        binding.apply {
            smUser.startShimmer()
            smUser.visibility = View.VISIBLE
            hsUser.visibility = View.GONE
        }
    }

    private fun setStopShimmer(){
        binding.apply {
            smUser.stopShimmer()
            smUser.visibility = View.GONE
            hsUser.visibility = View.VISIBLE
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@AdminUserActivity, AdminMainActivity::class.java))
        finish()
    }
}