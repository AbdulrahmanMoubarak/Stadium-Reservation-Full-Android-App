package com.training.util.constants

import com.training.R
import com.training.application.MainApplication

class ErrorMsg {
    companion object{

        val ERROR_EMAIL_NOT_VALID_MSG = MainApplication.getApplication().getString(R.string.email_not_valid)
        val ERROR_PASSWORD_WRONG_MSG = MainApplication.getApplication().getString(R.string.password_wrong)
        val ERROR_EMAIL_NOT_FOUND_MSG = MainApplication.getApplication().getString(R.string.email_not_found)
        val ERROR_EMPTY_EMAIL_MSG  = MainApplication.getApplication().getString(R.string.fill_email)
        val ERROR_EMPTY_PASSWORD_MSG = MainApplication.getApplication().getString(R.string.enter_password)
        val ERROR_EMPTY_PHONE_MSG = MainApplication.getApplication().getString(R.string.empty_phone)
        val ERROR_EMPTY_FNAME_MSG = MainApplication.getApplication().getString(R.string.empty_fname)
        val ERROR_EMPTY_LNAME_MSG = MainApplication.getApplication().getString(R.string.lname_empty)
        val ERROR_EMPTY_USERNAME_MSG = MainApplication.getApplication().getString(R.string.empty_uname)
        val ERROR_INVALID_PHONE_MSG = MainApplication.getApplication().getString(R.string.invalid_phone)
        val EMAIL_ALREADY_EXISTS_MSG = MainApplication.getApplication().getString(R.string.email_exists)
        val PHONE_ALREADY_EXISTS_MSG = MainApplication.getApplication().getString(R.string.phone_exists)
        val UNEXPECTED_ERROR_MSG = MainApplication.getApplication().getString(R.string.unexpectedError)
        val NETWORK_ERROR_MSG = MainApplication.getApplication().getString(R.string.network_error)
        val INVALID_USER_MSG = MainApplication.getApplication().getString(R.string.invalid_user)
        val STADIUM_KEY_INVALID_MSG = MainApplication.getApplication().getString(R.string.key_contains)
        val STADIUM_KEY_EMPTY_MSG = MainApplication.getApplication().getString(R.string.enter_key)
        val STADIUM_NAME_EMPTY_MSG = MainApplication.getApplication().getString(R.string.enter_stad_name)
        val STADIUM_LOC_EMPTY_MSG = MainApplication.getApplication().getString(R.string.enter_stad_loc)
        val KEY_ALREADY_EXISTS_MSG = MainApplication.getApplication().getString(R.string.key_used)
        val NO_DATA_MSG = MainApplication.getApplication().getString(R.string.no_data_msg)
        val KEY_DOES_NOT_EXIST_MSG = MainApplication.getApplication().getString(R.string.key_not_exist)
        val GAME_ALREADY_EXIST_MSG = MainApplication.getApplication().getString(R.string.game_exists)
    }
}