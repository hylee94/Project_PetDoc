package com.example.project_petdoc.dataclass

data class Record(
    val no:Int,
    val petid: Pet?,
    val date:String,
    val disease:String,
    val doctor_op:String,
    val medicine:String,
    val fee:String,
    val hospital:String,
    val memo:String
)
