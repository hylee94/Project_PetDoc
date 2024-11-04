package com.example.project_petdoc.pets

import com.example.project_petdoc.dataclass.Pet
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PetInterface {

    @GET("list")
    fun findAll(): Call<List<Pet>>

// 특정 사용자 ID를 기반으로 펫 목록을 가져오기 위한 메서드 추가
    @GET("list/{id}")
    fun findByMemberid_Id(@Path("id") id: String): Call<List<Pet>>

    // 추가
    @POST("insert")
    fun save(@Body pet: Pet): Call<Pet>

    // 수정
    @PUT("update/{petid}")
    fun update(@Path("petid") id:String, @Body pet: Pet): Call<Pet>

    // 삭제
    @DELETE("delete/{petid}")
    fun deleteById(@Path("petid") id:String): Call<Void>

}