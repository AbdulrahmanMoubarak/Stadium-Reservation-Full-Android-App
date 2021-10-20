package com.training.exceptions

import com.training.util.constants.DataError.Companion.KEY_ALREADY_EXISTS

class KeyAlreadyExistsException :Exception(){
    val id = KEY_ALREADY_EXISTS
}