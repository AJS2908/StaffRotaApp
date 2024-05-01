package com.example.staffrotaapp


// this is the data class for the admin in the database
data class Admin(
    var adminId: Int = 0,
    var username: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var email: String = "",
    var password: String = "",
    var nINumber: String = "",
    var ownerAcc: Boolean = false
)