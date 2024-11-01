package com.example.project_petdoc.client

import com.example.project_petdoc.interface1.MemberInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MemberClient {
    val retrofit:MemberInterface = Retrofit.Builder()
        .baseUrl("http://10.100.103.31:8899/member/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MemberInterface::class.java)
}
