package com.training.util.constants

class SignInDataError {
    companion object{
        val ERROR_EMAIL_NOT_VALID = -1 //offline
        val ERROR_PASSWORD_WRONG = -2 //online
        val ERROR_EMAIL_NOT_FOUND = -3 //online
        val ERROR_EMPTY_EMAIL = -4 //offline
        val ERROR_EMPTY_PASSWORD = -5 //offline
        val ERROR_EMPTY_PHONE = -6 //offline
        val ERROR_EMPTY_FNAME = -7 //offline
        val ERROR_EMPTY_LNAME = -8 //offline
        val ERROR_INVALID_PHONE = -9 //offline
        val EMAIL_ALREADY_EXISTS = -10 //online
        val PHONE_ALREADY_EXISTS = -11 //online
        val ERROR_EMPTY_USERNAME = -12 //offline
    }
}