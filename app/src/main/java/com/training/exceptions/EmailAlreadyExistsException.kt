package com.training.exceptions

import com.training.util.constants.DataError.Companion.EMAIL_ALREADY_EXISTS
import java.lang.Exception

class EmailAlreadyExistsException: Exception() {
    var id = EMAIL_ALREADY_EXISTS
}