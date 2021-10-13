package com.training.factory

import com.training.exceptions.*

class AppExceptionFactory(){

    fun getException(type: String): Exception{
        when(type.lowercase()){
            "InvalidUserException".lowercase()->{
                return InvalidUserException()
            }

            "NetworkException".lowercase()->{
                return NetworkException()
            }

            "UnknownError".lowercase()->{
                return NetworkException()
            }

            "InvalidPasswordException".lowercase()->{
                return InvalidPasswordException()
            }

            "EmailAlreadyExistsException".lowercase()->{
                return EmailAlreadyExistsException()
            }

            else ->{
                return UnknownErrorException()
            }
        }
    }
}