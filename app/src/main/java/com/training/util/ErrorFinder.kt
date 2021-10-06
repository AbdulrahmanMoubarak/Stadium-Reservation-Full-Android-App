package com.training.util

import com.training.util.constants.ErrorMsg
import com.training.util.constants.SignInDataError

class ErrorFinder {
    companion object {
        fun getError(err: Int): String {
            if (err == SignInDataError.EMAIL_ALREADY_EXISTS) {
                return ErrorMsg.EMAIL_ALREADY_EXISTS_MSG
            }
            if (err == SignInDataError.ERROR_EMAIL_NOT_FOUND) {
                return ErrorMsg.ERROR_EMAIL_NOT_FOUND_MSG
            }
            if (err == SignInDataError.ERROR_EMAIL_NOT_VALID) {
                return ErrorMsg.ERROR_EMAIL_NOT_VALID_MSG
            }
            if (err == SignInDataError.ERROR_EMPTY_EMAIL) {
                return ErrorMsg.ERROR_EMPTY_EMAIL_MSG
            }
            if (err == SignInDataError.ERROR_EMPTY_FNAME) {
                return ErrorMsg.ERROR_EMPTY_FNAME_MSG
            }
            if (err == SignInDataError.ERROR_EMPTY_LNAME) {
                return ErrorMsg.ERROR_EMPTY_LNAME_MSG
            }
            if (err == SignInDataError.ERROR_EMPTY_PASSWORD) {
                return ErrorMsg.ERROR_EMPTY_PASSWORD_MSG
            }
            if (err == SignInDataError.ERROR_EMPTY_PHONE) {
                return ErrorMsg.ERROR_EMPTY_PHONE_MSG
            }
            if (err == SignInDataError.ERROR_EMPTY_USERNAME) {
                return ErrorMsg.ERROR_EMPTY_USERNAME_MSG
            }
            if (err == SignInDataError.ERROR_INVALID_PHONE) {
                return ErrorMsg.ERROR_INVALID_PHONE_MSG
            }
            if (err == SignInDataError.ERROR_PASSWORD_WRONG) {
                return ErrorMsg.ERROR_PASSWORD_WRONG_MSG
            }
            if (err == SignInDataError.PHONE_ALREADY_EXISTS) {
                return ErrorMsg.PHONE_ALREADY_EXISTS_MSG
            }
            return ErrorMsg.UNEXPECTED_ERROR_MSG
        }
    }
}