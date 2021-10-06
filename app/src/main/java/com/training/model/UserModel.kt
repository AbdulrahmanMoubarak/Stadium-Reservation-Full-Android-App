package com.training.model

import java.io.Serializable

class UserModel(
    var email: String,
    var password: String,
    var first_name:String,
    var last_name:String,
    var phone: String
): Serializable{
    private var access_privilege: String? = null
    private var id: Int? = null

    fun setAccessPrivilege(access: String){
        access_privilege = access
    }

    fun setId(id: Int){
        this.id = id
    }

    fun getAccessPrivilege(): String{
        var priv = ""
        access_privilege?.let { priv = it }
        return priv
    }

    fun getId():Int{
        var x = 0
        id?.let{ x = it}
        return x
    }
}