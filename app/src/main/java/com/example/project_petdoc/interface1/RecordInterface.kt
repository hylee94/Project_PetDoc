package com.example.project_petdoc.interface1


import com.example.project_petdoc.dataclass.Record
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RecordInterface {
    @GET("list")
    fun getRecords(): Call<List<Record>>
    // 추가
    @POST("insert")
    fun saveRecord(@Body record: Record): Call<Record>
//    // 수정
//    @PUT("update/{memberid}")
//    fun update(@Path("memberid") id:Long, @Body pet: Pet): Call<Pet>
//    // 삭제
//    @DELETE("delete/{memberid}")
//    fun deleteById(@Path("memberid") id:Long): Call<Void>

}