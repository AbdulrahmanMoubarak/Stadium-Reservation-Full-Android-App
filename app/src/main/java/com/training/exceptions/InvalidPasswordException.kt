package com.training.exceptions

import com.training.util.constants.DataError.Companion.ERROR_PASSWORD_WRONG

class InvalidPasswordException: Exception() {
    var id = ERROR_PASSWORD_WRONG
}