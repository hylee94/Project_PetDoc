package com.example.project_petdoc.dataclass

import java.util.Date

data class Record(val no:Int,
                  val pet:Pet?,
                  val date:String,
                  val disease:String,
                  val doctor_op:String,
                  val medicine:String,
                  val fee:String,
                  val hospital:String,
                  val memo:String
)
