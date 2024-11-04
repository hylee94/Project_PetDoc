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

    // Record 추가
    @POST("insert")
    fun saveRecord(@Body record: Record): Call<Record>

    // 특정 반려동물의 진료 기록 가져오기
    @GET("/list/pet/{petId}")
    fun getRecordsByPetId(@Path("petId") petId: Int): Call<List<Record>>


    // Record 삭제 (예시, 필요한 경우 추가)
    @DELETE("delete/{no}")
    fun deleteRecord(@Path("no") recordId: Long): Call<Void>

    // Record 업데이트 (예시, 필요한 경우 추가)
    @PUT("update")
    fun updateRecord(@Body record: Record): Call<Record>
}
