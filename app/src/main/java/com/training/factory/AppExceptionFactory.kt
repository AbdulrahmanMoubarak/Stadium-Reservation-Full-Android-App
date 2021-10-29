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

            "GameAlreadyExistsException".lowercase()->{
                return GameAlreadyExistsException()
            }

            "NoDataException".lowercase()->{
                return NoDataException()
            }

            "KeyDoesnotExistException".lowercase() ->{
                return KeyDoesnotExistException()
            }

            else ->{
                return UnknownErrorException()
            }
        }
    }
}