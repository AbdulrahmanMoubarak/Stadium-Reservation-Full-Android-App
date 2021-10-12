package com.training.model

import com.training.util.encryption.ItemHasherSHA256

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

    fun getFirebaseFormat(): LoginModel{
        val email_format = email.replace('.', ',')
        val password_format = ItemHasherSHA256.hashItem(password)
        val user = LoginModel(
            email_format,
            password_format,
        )
        return user
    }

    fun getViewFormat():LoginModel{
        val email_format = email.replace(',', '.')
        val user = LoginModel(
            email_format,
            password,
        )
        return user
    }
}