package com.training.exceptions

import com.training.util.constants.SignInDataError

class NetworkException: Exception(){
    val id = SignInDataError.NETWORK_ERROR
}