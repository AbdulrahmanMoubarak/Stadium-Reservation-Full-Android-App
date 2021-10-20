package com.training.factory

import com.training.exceptions.KeyAlreadyExistsException
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
                return UnknownErrorException()
            }

            "InvalidPasswordException".lowercase()->{
                return InvalidPasswordException()
            }

            "EmailAlreadyExistsException".lowercase()->{
                return EmailAlreadyExistsException()
            }

            "KeyAlreadyExistsException".lowercase()->{
                return KeyAlreadyExistsException()
            }

            "NoDataException".lowercase()->{
                return NoDataException()
            }

            else ->{
                return UnknownErrorException()
            }
        }
    }
}