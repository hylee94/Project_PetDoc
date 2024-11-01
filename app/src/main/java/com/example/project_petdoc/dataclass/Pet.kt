package com.example.project_petdoc.dataclass


data class Pet(
    val petid:Int,
    val memberid:Member,
    val type: String,
    val name: String,
    val gender: String,
    val age: Int,
    val hospital: String
)