package com.training.exceptions

import com.training.util.constants.DataError.Companion.GAME_ALREADY_EXIST
import java.lang.Exception

class GameAlreadyExistsException: Exception() {
    val id = GAME_ALREADY_EXIST
}