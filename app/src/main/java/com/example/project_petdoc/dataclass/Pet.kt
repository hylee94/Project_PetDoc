package com.example.project_petdoc.dataclass

data class Pet(
    val petid:String,
    val memberid:String,
    val type: String?,
    val name: String?,
    val gender: String?,
    val age: String?,
    val hospital: String?
)