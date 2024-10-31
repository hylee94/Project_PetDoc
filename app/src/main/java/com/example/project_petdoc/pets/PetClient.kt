package com.example.project_petdoc.pets

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PetClient {
    val retrofit:PetInterface = Retrofit.Builder()
        .baseUrl("http://10.100.103.42:8899/pet/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PetInterface::class.java)
}