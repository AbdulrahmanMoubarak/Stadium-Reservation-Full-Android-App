package com.training.util.validation

import com.training.model.LoginModel
import com.training.model.UserModel
import com.training.util.constants.ErrorMsg
import com.training.util.constants.DataError

class ErrorFinder {
    companion object {
        fun getPasswordError(password: String): Int {
            if ((password.length == 0)) {
                return DataError.ERROR_EMPTY_PASSWORD
            } else {
                return DataError.SUCCESSFULL
            }
        }

        fun getError(user: LoginModel): Int {
            if (!(user.email.length > 0)) {
                return DataError.ERROR_EMPTY_EMAIL
            } else if (!user.email.contains('@')) {
                return DataError.ERROR_INVALID_EMAIL
            } else if ((user.password.length == 0)) {
                return DataError.ERROR_EMPTY_PASSWORD
            } else {
                return DataError.SUCCESSFULL
            }
        }

        fun getError(user: UserModel): Int {
            if (!(user.email.length > 0)) {
                return DataError.ERROR_EMPTY_EMAIL
            } else if (!user.email.contains('@')) {
                return DataError.ERROR_INVALID_EMAIL

            } else if (!(user.password.length > 0)) {
                return DataError.ERROR_EMPTY_PASSWORD

            } else if (!(user.first_name.length > 0)) {
                return DataError.ERROR_EMPTY_FNAME

            } else if (!(user.last_name.length > 0)) {
                return DataError.ERROR_EMPTY_LNAME

            } else if (!(user.phone.length > 0)) {
                return DataError.ERROR_EMPTY_PHONE

            } else if (!(user.phone.length == 11)) {
                return DataError.ERROR_INVALID_PHONE
            } else {
                return DataError.SUCCESSFULL
            }
        }

        fun getError(key: String, name: String, location: String): Int {
            if (key.length == 0) {
                return DataError.STADIUM_KEY_EMPTY
            } else if (key.contains('$') || key.contains('#') || key.contains('!') || key.contains(
                    '.'
                ) || key.contains('*') || key.contains('/') || key.contains('+') || key.contains('-')
            ) {
                return DataError.STADIUM_KEY_INVALID
            } else if (name.length == 0) {
                return DataError.STADIUM_NAME_EMPTY
            } else if (location.length == 0) {
                return DataError.STADIUM_LOC_EMPTY
            } else
                return DataError.SUCCESSFULL
        }

        fun getErrorMsg(err: Int): String {
            if (err == DataError.EMAIL_ALREADY_EXISTS) {
                return ErrorMsg.EMAIL_ALREADY_EXISTS_MSG
            } else if (err == DataError.ERROR_EMAIL_NOT_FOUND) {
                return ErrorMsg.ERROR_EMAIL_NOT_FOUND_MSG
            } else if (err == DataError.ERROR_INVALID_EMAIL) {
                return ErrorMsg.ERROR_EMAIL_NOT_VALID_MSG
            } else if (err == DataError.ERROR_EMPTY_EMAIL) {
                return ErrorMsg.ERROR_EMPTY_EMAIL_MSG
            } else if (err == DataError.ERROR_EMPTY_FNAME) {
                return ErrorMsg.ERROR_EMPTY_FNAME_MSG
            } else if (err == DataError.ERROR_EMPTY_LNAME) {
                return ErrorMsg.ERROR_EMPTY_LNAME_MSG
            } else if (err == DataError.ERROR_EMPTY_PASSWORD) {
                return ErrorMsg.ERROR_EMPTY_PASSWORD_MSG
            } else if (err == DataError.ERROR_EMPTY_PHONE) {
                return ErrorMsg.ERROR_EMPTY_PHONE_MSG
            } else if (err == DataError.ERROR_EMPTY_USERNAME) {
                return ErrorMsg.ERROR_EMPTY_USERNAME_MSG
            } else if (err == DataError.ERROR_INVALID_PHONE) {
                return ErrorMsg.ERROR_INVALID_PHONE_MSG
            } else if (err == DataError.ERROR_PASSWORD_WRONG) {
                return ErrorMsg.ERROR_PASSWORD_WRONG_MSG
            } else if (err == DataError.PHONE_ALREADY_EXISTS) {
                return ErrorMsg.PHONE_ALREADY_EXISTS_MSG
            } else if (err == DataError.NETWORK_ERROR) {
                return ErrorMsg.NETWORK_ERROR_MSG
            } else if (err == DataError.INVALID_USER) {
                return ErrorMsg.INVALID_USER_MSG
            } else if (err == DataError.STADIUM_KEY_INVALID) {
                return ErrorMsg.STADIUM_KEY_INVALID_MSG
            } else if (err == DataError.STADIUM_KEY_EMPTY) {
                return ErrorMsg.STADIUM_KEY_EMPTY_MSG
            } else if (err == DataError.STADIUM_LOC_EMPTY) {
                return ErrorMsg.STADIUM_LOC_EMPTY_MSG
            } else if (err == DataError.STADIUM_NAME_EMPTY) {
                return ErrorMsg.STADIUM_NAME_EMPTY_MSG
            } else if (err == DataError.KEY_ALREADY_EXISTS) {
                return ErrorMsg.KEY_ALREADY_EXISTS_MSG
            } else if (err == DataError.NO_DATA) {
                return ErrorMsg.NO_DATA_MSG
            } else {
                return ErrorMsg.UNEXPECTED_ERROR_MSG
            }
        }
    }
}