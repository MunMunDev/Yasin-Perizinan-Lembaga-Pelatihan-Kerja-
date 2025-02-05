package com.mismundev.yasin_perizinanlembagapelatihankerja.data.database.api

import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.DaftarPelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.PendaftarModel
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

    @GET("pelatihan-kerja/api/get.php")
    suspend fun getDetailPelatihan(
        @Query("get_detail_pelatihan") get_detail_pelatihan: String,
        @Query("id_daftar_pelatihan") id_daftar_pelatihan: Int,
    ): DaftarPelatihanModel

    @GET("pelatihan-kerja/api/get.php")
    suspend fun getTelahDaftarPelatihan(
        @Query("get_telah_daftar_pelatihan") get_telah_daftar_pelatihan: String,
        @Query("id_daftar_pelatihan") id_daftar_pelatihan: Int,
        @Query("id_user") id_user: Int,
    ): PendaftarModel



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
    ): ResponseModel

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

    @FormUrlEncoded
    @POST("pelatihan-kerja/api/post.php")
    suspend fun postDaftarPelatihan(
        @Field("post_daftar_pelatihan") post_daftar_pelatihan:String,
        @Field("id_daftar_pelatihan") id_daftar_pelatihan: Int,
        @Field("id_user") id_user: Int,
    ): ResponseModel

    @FormUrlEncoded
    @POST("pelatihan-kerja/api/post.php")
    suspend fun postRegistrasiPembayaran(
        @Field("post_register_pembayaran") post_register_pembayaran:String,
        @Field("kode_unik") kode_unik: String,
        @Field("id_user") id_user: Int,
        @Field("id_daftar_pelatihan") id_daftar_pelatihan: Int,
    ): ResponseModel

}