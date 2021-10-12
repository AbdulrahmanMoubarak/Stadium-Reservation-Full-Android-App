package com.training.model

import com.training.util.constants.Encryption
import com.training.util.encryption.ItemEncryptorASE
import com.training.util.encryption.ItemHasherSHA256
import java.io.Serializable

class UserModel(
) : Serializable{

    lateinit var email: String
    lateinit var password: String
    lateinit var first_name:String
    lateinit var last_name:String
    lateinit var phone: String
    var id: Int = 0
    var first_usage = true
    lateinit var access_privilege: String

    constructor(
        email: String,
        password: String,
        first_name:String,
        last_name:String,
        phone: String,
        id: Int,
        access_privilege: String,
        first_usage: Boolean

    ) : this(){
        this.email = email
        this.password = password
        this.first_name = first_name
        this.last_name = last_name
        this.phone = phone
        this.id = id
        this.access_privilege = access_privilege
        this.first_usage = first_usage
    }

    fun getFirebaseFormat(): UserModel{
        val email_format = email.replace('.', ',')
        val password_format = ItemHasherSHA256.hashItem(password)
        var phone_format = ItemEncryptorASE().encrypt(phone, Encryption.KEY)

        val user = UserModel(
            email_format,
            password_format,
            first_name,
            last_name,
            phone_format,
            id,
            access_privilege,
            first_usage
        )
        return user
    }

    fun getViewFormat(): UserModel{
        var phone_format = ItemEncryptorASE().decrypt(phone, Encryption.KEY)
        val email_format = email.replace(',', '.')
        //val password_format = ItemHasherSHA256.hashItem("password")
        val user = UserModel(
            email_format,
            password,
            first_name,
            last_name,
            phone_format,
            id,
            access_privilege,
            first_usage
        )
        return user
    }
}