package com.example.project_petdoc.interface1

import com.example.project_petdoc.dataclass.Member
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MemberInterface {

    //전체보기
    @GET("list")
    fun findAll():Call<List<Member>>

    //추가
    @POST("insert")
    fun insert(@Body member: Member):Call<Member>

    //수정
    @PUT("update/{id}")
    fun update(@Body member: Member, @Path("id") id:String):Call<Member>

    //삭제
    @DELETE("delete/{id}")
    fun delete(@Path("id") id:String):Call<Void>
}