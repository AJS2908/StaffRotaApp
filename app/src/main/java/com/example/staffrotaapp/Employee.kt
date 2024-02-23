package com.example.staffrotaapp

class Employee {
    private var firstName: String? = null
    private var lastName: String? = null
    private var nINumber: String? = null
    private var password: String? = null

    constructor(
        firstName: String?,
        lastName: String?,
        nINumber: String?,
        password: String?
    ) {
        this.firstName = firstName
        this.lastName = lastName
        this.nINumber = nINumber
        this.password = password
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

    fun setNINumber(nINumber: String?) {
        this.nINumber = nINumber
    }


    fun getPassword(): String? {
        return password
    }

    fun setPassword(password: String?) {
        this.password = password
    }
}