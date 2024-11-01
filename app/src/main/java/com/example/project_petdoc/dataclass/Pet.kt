package com.example.project_petdoc.dataclass

//data class Pet(
//    val petid:Int,
//    val type: String?,
//    val name: String?,
//    val gender: String?,
//    val age: String?,
//    val hospital: String?
//)

class Pet {


//    }
//    constructor(  petid:Int,
//     type: String?,
//     name: String?,
//     gender: String?,
//     age: String?,
//     hospital: String?)
//}

    var petid: Int = 0
    var type: String = ""
    var name: String = ""
    var gender: String = ""
    var age: String = ""
    var hospital: String = ""


    constructor(petid: Int) {
        this.petid = petid
    }

    constructor(
        petid: Int,
        type: String,
        name: String,
        gender: String,
        age: String,
        hospital: String
    ) {
        this.petid = petid
        this.type = type
        this.name = name
        this.gender = gender
        this.age = age
        this.hospital = hospital
    }

}