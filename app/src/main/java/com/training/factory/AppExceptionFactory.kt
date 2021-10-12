package com.training.factory

import com.training.exceptions.InvalidPasswordException
import com.training.exceptions.InvalidUserException
import com.training.exceptions.NetworkException
import com.training.exceptions.UnknownErrorException

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

            else ->{
                return UnknownErrorException()
            }
        }
    }
}