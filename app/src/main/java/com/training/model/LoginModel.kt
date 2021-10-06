package com.training.model

class LoginModel(
    val email: String,
    val password: String,
){
    private var access_privilege: String? = null
    fun setAccessPrivilege(access: String){
        access_privilege = access
    }

    fun getAccessPrivilege(): String{
        var priv = ""
        access_privilege?.let { priv = it }
        return priv
    }
}