package com.example.staffrotaapp

class Employee {
    var firstName: String? = null
    var lastName: String? = null
    var nINumber: String? = null
    var email: String? = null

    constructor() {}

    constructor(firstName: String?, lastName: String?, nINumber: String?, email: String?) {
        this.firstName = firstName
        this.lastName = lastName
        this.nINumber = nINumber
        this.email = email
    }

    fun getFirstName(): String? {
        return firstName
    }

    fun setFirstName(firstName: String?) {
        this.firstName = firstName
    }

    fun getLastName(): String? {
        return lastName
    }

    fun setLastName(lastName: String?) {
        this.lastName = lastName
    }

    fun getNINumber(): String? {
        return nINumber
    }

    fun setNINumber(age: String?) {
        this.nINumber = nINumber
    }

    fun getUserName(): String? {
        return email
    }

    fun setUserName(userName: String?) {
        this.email = email
    }
}