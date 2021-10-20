package com.training.exceptions

import com.training.util.constants.DataError

class InvalidUserException: Exception(){
    val id = DataError.INVALID_USER
}