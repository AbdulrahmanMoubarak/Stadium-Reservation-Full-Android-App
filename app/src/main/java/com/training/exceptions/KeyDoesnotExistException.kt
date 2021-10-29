package com.training.exceptions

import com.training.util.constants.DataError.Companion.KEY_DOES_NOT_EXIST

class KeyDoesnotExistException :Exception() {
    val id = KEY_DOES_NOT_EXIST
}