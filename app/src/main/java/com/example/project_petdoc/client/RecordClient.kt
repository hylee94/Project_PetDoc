package com.example.project_petdoc.client

import com.example.project_petdoc.interface1.RecordInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RecordClient {

    val retrofit: RecordInterface = Retrofit.Builder()
        .baseUrl("http://10.100.103.38:8899/record/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RecordInterface::class.java)
}