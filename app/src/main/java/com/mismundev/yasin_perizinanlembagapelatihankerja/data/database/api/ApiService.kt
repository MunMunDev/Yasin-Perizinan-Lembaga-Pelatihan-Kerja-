package com.mismundev.yasin_perizinanlembagapelatihankerja.data.database.api

import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.DaftarPelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.JenisPelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.PelatihanModel
import com.mismundev.yasin_perizinanlembagapelatihankerja.data.model.PembayaranModel
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

    @GET("pelatihan-kerja/api/get.php")
    suspend fun getRiwayat(
        @Query("get_riwayat") get_riwayat: String,
        @Query("id_user") id_user: Int,
    ): ArrayList<DaftarPelatihanModel>

    @GET("pelatihan-kerja/api/get.php")
    suspend fun getAllJenisPelatihan(
        @Query("get_all_jenis_pelatihan") get_all_jenis_pelatihan: String,
    ): ArrayList<JenisPelatihanModel>

    @GET("pelatihan-kerja/api/get.php")
    suspend fun getAllPelatihan(
        @Query("get_all_pelatihan") get_all_pelatihan: String,
    ): ArrayList<PelatihanModel>

    @GET("pelatihan-kerja/api/get.php")
    suspend fun getAllDaftarPelatihan(
        @Query("get_all_daftar_pelatihan") get_all_pelatihan: String,
    ): ArrayList<DaftarPelatihanModel>

    @GET("pelatihan-kerja/api/get.php")
    suspend fun getAllPembayaran(
        @Query("get_log_pembayaran") get_log_pembayaran: String,
    ): ArrayList<PembayaranModel>

    @GET("pelatihan-kerja/api/get.php")
    suspend fun getPendaftar(
        @Query("get_pendaftar") get_pendaftar: String,
    ): ArrayList<PendaftarModel>



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
    ): ResponseModel

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

    @FormUrlEncoded
    @POST("pelatihan-kerja/api/post.php")
    suspend fun postAddJenisPelatihan(
        @Field("post_add_jenis_pelatihan") post_add_jenis_pelatihan:String,
        @Field("jenis_pelatihan") jenis_pelatihan: String,
    ): ResponseModel

    @FormUrlEncoded
    @POST("pelatihan-kerja/api/post.php")
    suspend fun postUpdateJenisPelatihan(
        @Field("post_update_jenis_pelatihan") post_update_jenis_pelatihan:String,
        @Field("id_jenis_pelatihan") id_jenis_pelatihan: Int,
        @Field("jenis_pelatihan") jenis_pelatihan: String,
    ): ResponseModel

    @Multipart
    @POST("pelatihan-kerja/api/post.php")
    suspend fun postAddPelatihan(
        @Part("post_add_pelatihan") post_add_pelatihan:RequestBody,
        @Part("id_jenis_pelatihan") id_jenis_pelatihan: RequestBody,
        @Part("nama_pelatihan") nama_pelatihan: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part gambar: MultipartBody.Part,
    ): ResponseModel

    @Multipart
    @POST("pelatihan-kerja/api/post.php")
    suspend fun postUpdatePelatihanAddImage(
        @Part("post_update_pelatihan_add_image") post_update_pelatihan_add_image:RequestBody,
        @Part("id_pelatihan") id_pelatihan: RequestBody,
        @Part("id_jenis_pelatihan") id_jenis_pelatihan: RequestBody,
        @Part("nama_pelatihan") nama_pelatihan: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part gambar: MultipartBody.Part
    ): ResponseModel

    @FormUrlEncoded
    @POST("pelatihan-kerja/api/post.php")
    suspend fun postUpdatePelatihan(
        @Field("post_update_pelatihan") post_update_pelatihan: String,
        @Field("id_pelatihan") id_pelatihan: Int,
        @Field("id_jenis_pelatihan") id_jenis_pelatihan: Int,
        @Field("nama_pelatihan") nama_pelatihan: String,
        @Field("deskripsi") deskripsi: String,
    ): ResponseModel

    @Multipart
    @POST("pelatihan-kerja/api/post.php")
    suspend fun postAddDaftarPelatihan(
        @Part("post_add_daftar_pelatihan") post_add_daftar_pelatihan:RequestBody,
        @Part("id_pelatihan") id_pelatihan: RequestBody,
        @Part("kuota") kuota: RequestBody,
        @Part("biaya") biaya: RequestBody,
        @Part("tgl_pelaksanaan") tgl_pelaksanaan: RequestBody,
        @Part("tgl_mulai") tgl_mulai: RequestBody,
        @Part("tgl_berakhir") tgl_berakhir: RequestBody,
        @Part("lokasi") lokasi: RequestBody,
        @Part gambar: MultipartBody.Part,
    ): ResponseModel

    @Multipart
    @POST("pelatihan-kerja/api/post.php")
    suspend fun postUpdateDaftarPelatihanAddImage(
        @Part("post_update_daftar_pelatihan_add_image") post_update_daftar_pelatihan_add_image:RequestBody,
        @Part("id_daftar_pelatihan") id_daftar_pelatihan: RequestBody,
        @Part("id_pelatihan") id_pelatihan: RequestBody,
        @Part("kuota") kuota: RequestBody,
        @Part("biaya") biaya: RequestBody,
        @Part("tgl_pelaksanaan") tgl_pelaksanaan: RequestBody,
        @Part("tgl_mulai") tgl_mulai: RequestBody,
        @Part("tgl_berakhir") tgl_berakhir: RequestBody,
        @Part("lokasi") lokasi: RequestBody,
        @Part gambar: MultipartBody.Part,
    ): ResponseModel

    @FormUrlEncoded
    @POST("pelatihan-kerja/api/post.php")
    suspend fun postUpdateDaftarPelatihan(
        @Field("post_update_daftar_pelatihan") post_update_daftar_pelatihan: String,
        @Field("id_daftar_pelatihan") id_daftar_pelatihan: Int,
        @Field("id_pelatihan") id_pelatihan: Int,
        @Field("kuota") kuota: Int,
        @Field("biaya") biaya: Int,
        @Field("tgl_pelaksanaan") tgl_pelaksanaan: String,
        @Field("tgl_mulai") tgl_mulai: String,
        @Field("tgl_berakhir") tgl_berakhir: String,
        @Field("lokasi") lokasi: String,
    ): ResponseModel

    @FormUrlEncoded
    @POST("marketplace-wo/api/post.php")
    suspend fun postUpdateAdminAkunUser(
        @Field("update_admin_akun_user") update_admin_akun_user: String,
        @Field("id_user") id_user: Int,
        @Field("nama") nama: String,
        @Field("alamat") alamat: String,
        @Field("nomor_hp") nomor_hp: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("username_lama") username_lama: String,
    ): ResponseModel

    @FormUrlEncoded
    @POST("marketplace-wo/api/post.php")
    suspend fun postAddAdminPendaftar(
        @Field("add_admin_pendaftar") add_admin_pendaftar: String,
        @Field("id_user") id_user: Int,
        @Field("id_daftar_pelatihan") id_daftar_pelatihan: Int,
        @Field("ket") ket: String,
    ): ResponseModel

    @FormUrlEncoded
    @POST("marketplace-wo/api/post.php")
    suspend fun postUpdateAdminPendaftar(
        @Field("update_admin_pendaftar") update_admin_pendaftar: String,
        @Field("id_pendaftar") id_pendaftar: Int,
        @Field("id_user") id_user: Int,
        @Field("id_daftar_pelatihan") id_daftar_pelatihan: Int,
        @Field("ket") ket: String,
    ): ResponseModel

    @FormUrlEncoded
    @POST("marketplace-wo/api/post.php")
    suspend fun postDeleteAdminPendaftar(
        @Field("delete_admin_pendaftar") delete_admin_pendaftar: String,
        @Field("id_pendaftar") id_pendaftar: Int,
    ): ResponseModel


}