package com.training.exceptions

import com.training.util.constants.SignInDataError

class UnknownErrorException: Exception() {
    val id = SignInDataError.UNEXPECTED_ERROR
}