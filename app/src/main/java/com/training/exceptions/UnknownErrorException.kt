package com.training.exceptions

import com.training.util.constants.DataError

class UnknownErrorException: Exception() {
    val id = DataError.UNEXPECTED_ERROR
}