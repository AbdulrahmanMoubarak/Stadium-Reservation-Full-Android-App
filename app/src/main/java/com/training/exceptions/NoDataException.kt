package com.training.exceptions

import com.training.util.constants.DataError.Companion.NO_DATA

class NoDataException: Exception() {
    val id = NO_DATA
}