package com.training.exceptions

import com.training.util.constants.SignInDataError

class InvalidUserException: Exception(){
    val id = SignInDataError.INVALID_USER
}