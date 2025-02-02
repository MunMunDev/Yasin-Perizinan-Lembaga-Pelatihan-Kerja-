package com.mismundev.yasin_perizinanlembagapelatihankerja.data.database.api

import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.DaftarPelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.ResponseModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.UsersModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @GET("pelatihan-kerja/api/get.php")
    suspend fun getAllUser(
        @Query("all_user") allUser: String
    ): ArrayList<UsersModel>

    @GET("pelatihan-kerja/api/get.php")
    suspend fun getUser(
        @Query("get_user") getUser: String,
        @Query("email") email: String,
        @Query("password") password: String
    ): ArrayList<UsersModel>

    @GET("pelatihan-kerja/api/get.php")
    suspend fun getPelatihanTerdaftar(
        @Query("get_pelatihan_terdaftar") get_pelatihan_terdaftar: String,
        @Query("id_user") id_user: Int
    ): ArrayList<DaftarPelatihanModel>

    @GET("pelatihan-kerja/api/get.php")
    suspend fun getPelatihan(
        @Query("get_pelatihan") get_pelatihan: String,
    ): ArrayList<DaftarPelatihanModel>



    // POST
    @FormUrlEncoded
    @POST("pelatihan-kerja/api/post.php")
    suspend fun addUser(
        @Field("add_user") addUser:String,
        @Field("nama") nama:String,
        @Field("alamat") alamat:String,
        @Field("nomor_hp") nomorHp:String,
        @Field("email") email:String,
        @Field("password") password:String,
        @Field("sebagai") sebagai:String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("pelatihan-kerja/api/post.php")
    suspend fun postUpdateUser(
        @Field("update_akun") updateAkun:String,
        @Field("id_user") idUser: String,
        @Field("nama") nama:String,
        @Field("alamat") alamat:String,
        @Field("nomor_hp") nomorHp:String,
        @Field("email") email:String,
        @Field("password") password:String,
        @Field("email_lama") emailLama: String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("pelatihan-kerja/api/post.php")
    suspend fun postHapusUser(
        @Field("hapus_akun") hapusAkun:String,
        @Field("id_user") idUser: String
    ): ArrayList<ResponseModel>

}