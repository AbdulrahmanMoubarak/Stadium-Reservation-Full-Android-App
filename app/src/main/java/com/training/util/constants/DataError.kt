package com.training.util.constants

class DataError {
    companion object{
        val SUCCESSFULL = 0
        val ERROR_INVALID_EMAIL = -1 //offline
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
        val NETWORK_ERROR = -13 //online
        val UNEXPECTED_ERROR = -15
        val INVALID_USER = -16
        val STADIUM_KEY_INVALID = -17
        val STADIUM_KEY_EMPTY = -18
        val STADIUM_NAME_EMPTY = -19
        val STADIUM_LOC_EMPTY = -20
        val KEY_ALREADY_EXISTS = -21
        val NO_DATA = -22
        val KEY_DOES_NOT_EXIST = -23
        val GAME_ALREADY_EXIST = -24
    }
}