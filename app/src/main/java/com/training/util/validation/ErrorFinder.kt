package com.training.util.validation

import com.training.model.LoginModel
import com.training.model.UserModel
import com.training.util.constants.ErrorMsg
import com.training.util.constants.SignInDataError

class ErrorFinder {
    companion object {
        fun getError(user: LoginModel): Int{
            if (!(user.email.length > 0)) {
                return SignInDataError.ERROR_EMPTY_EMAIL
            } else if (!user.email.contains('@')) {
                return SignInDataError.ERROR_INVALID_EMAIL
            } else if ((user.password.length == 0)) {
                return SignInDataError.ERROR_EMPTY_PASSWORD
            } else{
                return SignInDataError.SUCCESSFULL
            }
        }

        fun getError(user: UserModel): Int{
            if (!(user.email.length > 0)) {
                return SignInDataError.ERROR_EMPTY_EMAIL
            } else if (!user.email.contains('@')) {
                return SignInDataError.ERROR_INVALID_EMAIL

            } else if (!(user.password.length > 0)) {
                return SignInDataError.ERROR_EMPTY_PASSWORD

            } else if (!(user.first_name.length > 0)) {
                return SignInDataError.ERROR_EMPTY_FNAME

            } else if (!(user.last_name.length > 0)) {
                return SignInDataError.ERROR_EMPTY_LNAME

            } else if (!(user.phone.length > 0)) {
                return SignInDataError.ERROR_EMPTY_PHONE

            } else if (!(user.phone.length == 11)) {
                return SignInDataError.ERROR_INVALID_PHONE
            } else {
                return SignInDataError.SUCCESSFULL
            }
        }

        fun getErrorMsg(err: Int): String {
            if (err == SignInDataError.EMAIL_ALREADY_EXISTS) {
                return ErrorMsg.EMAIL_ALREADY_EXISTS_MSG
            }
            else if (err == SignInDataError.ERROR_EMAIL_NOT_FOUND) {
                return ErrorMsg.ERROR_EMAIL_NOT_FOUND_MSG
            }
            else if (err == SignInDataError.ERROR_INVALID_EMAIL) {
                return ErrorMsg.ERROR_EMAIL_NOT_VALID_MSG
            }
            else if (err == SignInDataError.ERROR_EMPTY_EMAIL) {
                return ErrorMsg.ERROR_EMPTY_EMAIL_MSG
            }
            else if (err == SignInDataError.ERROR_EMPTY_FNAME) {
                return ErrorMsg.ERROR_EMPTY_FNAME_MSG
            }
            else if (err == SignInDataError.ERROR_EMPTY_LNAME) {
                return ErrorMsg.ERROR_EMPTY_LNAME_MSG
            }
            else if (err == SignInDataError.ERROR_EMPTY_PASSWORD) {
                return ErrorMsg.ERROR_EMPTY_PASSWORD_MSG
            }
            else if (err == SignInDataError.ERROR_EMPTY_PHONE) {
                return ErrorMsg.ERROR_EMPTY_PHONE_MSG
            }
            else if (err == SignInDataError.ERROR_EMPTY_USERNAME) {
                return ErrorMsg.ERROR_EMPTY_USERNAME_MSG
            }
            else if (err == SignInDataError.ERROR_INVALID_PHONE) {
                return ErrorMsg.ERROR_INVALID_PHONE_MSG
            }
            else if (err == SignInDataError.ERROR_PASSWORD_WRONG) {
                return ErrorMsg.ERROR_PASSWORD_WRONG_MSG
            }
            else if (err == SignInDataError.PHONE_ALREADY_EXISTS) {
                return ErrorMsg.PHONE_ALREADY_EXISTS_MSG
            }
            else {
                return ErrorMsg.UNEXPECTED_ERROR_MSG
            }
        }
    }
}