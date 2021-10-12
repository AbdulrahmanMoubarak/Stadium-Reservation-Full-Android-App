package com.training.exceptions

import com.training.util.constants.SignInDataError.Companion.ERROR_PASSWORD_WRONG

class InvalidPasswordException: Exception() {
    var id = ERROR_PASSWORD_WRONG
}