package com.example.staffrotaapp

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