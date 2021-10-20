package com.training.exceptions

import com.training.util.constants.DataError

class NetworkException: Exception(){
    val id = DataError.NETWORK_ERROR
}