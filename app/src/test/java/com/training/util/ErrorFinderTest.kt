package com.training.util

import com.google.common.truth.Truth.assertThat
import com.training.model.LoginModel
import com.training.util.constants.ErrorMsg
import com.training.util.constants.DataError
import com.training.util.validation.ErrorFinder
import org.junit.Test

class ErrorFinderTest{

    @Test fun test_errorFinderReturnsTheRightError(){
        var user = LoginModel("email@email.com", "myPass")
        assertThat(ErrorFinder.getError(user)).isEqualTo(DataError.SUCCESSFULL)

        user = LoginModel("", "myPass")
        assertThat(ErrorFinder.getError(user)).isEqualTo(DataError.ERROR_EMPTY_EMAIL)

        user = LoginModel("email@email.com", "")
        assertThat(ErrorFinder.getError(user)).isEqualTo(DataError.ERROR_EMPTY_PASSWORD)

        user = LoginModel("email.com", "lksjja")
        assertThat(ErrorFinder.getError(user)).isEqualTo(DataError.ERROR_INVALID_EMAIL)
    }

    @Test fun test_errorFinderReturnsTheRightMessage(){

        assertThat(
            ErrorFinder.getErrorMsg(DataError.EMAIL_ALREADY_EXISTS)
        ).matches(ErrorMsg.EMAIL_ALREADY_EXISTS_MSG)

        assertThat(
            ErrorFinder.getErrorMsg(DataError.ERROR_EMAIL_NOT_FOUND)
        ).matches(ErrorMsg.ERROR_EMAIL_NOT_FOUND_MSG)

        assertThat(
            ErrorFinder.getErrorMsg(DataError.ERROR_INVALID_EMAIL)
        ).matches(ErrorMsg.ERROR_EMAIL_NOT_VALID_MSG)

        assertThat(
            ErrorFinder.getErrorMsg(DataError.ERROR_EMPTY_EMAIL)
        ).matches(ErrorMsg.ERROR_EMPTY_EMAIL_MSG)

        assertThat(
            ErrorFinder.getErrorMsg(DataError.ERROR_EMPTY_FNAME)
        ).matches(ErrorMsg.ERROR_EMPTY_FNAME_MSG)

        assertThat(
            ErrorFinder.getErrorMsg(DataError.ERROR_EMPTY_LNAME)
        ).matches(ErrorMsg.ERROR_EMPTY_LNAME_MSG)

        assertThat(
            ErrorFinder.getErrorMsg(DataError.ERROR_EMPTY_PASSWORD)
        ).matches(ErrorMsg.ERROR_EMPTY_PASSWORD_MSG)

        assertThat(
            ErrorFinder.getErrorMsg(DataError.ERROR_EMPTY_PHONE)
        ).matches(ErrorMsg.ERROR_EMPTY_PHONE_MSG)

        assertThat(
            ErrorFinder.getErrorMsg(DataError.ERROR_EMPTY_USERNAME)
        ).matches(ErrorMsg.ERROR_EMPTY_USERNAME_MSG)

        assertThat(
            ErrorFinder.getErrorMsg(DataError.ERROR_INVALID_PHONE)
        ).matches(ErrorMsg.ERROR_INVALID_PHONE_MSG)

        assertThat(
            ErrorFinder.getErrorMsg(DataError.ERROR_PASSWORD_WRONG)
        ).matches(ErrorMsg.ERROR_PASSWORD_WRONG_MSG)

        assertThat(
            ErrorFinder.getErrorMsg(DataError.PHONE_ALREADY_EXISTS)
        ).matches(ErrorMsg.PHONE_ALREADY_EXISTS_MSG)

    }

}