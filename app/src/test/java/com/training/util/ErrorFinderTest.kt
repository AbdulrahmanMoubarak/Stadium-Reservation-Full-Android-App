package com.training.util

import com.google.common.truth.Truth.assertThat
import com.training.util.constants.ErrorMsg
import com.training.util.constants.SignInDataError
import org.junit.Test

class ErrorFinderTest{

    @Test
    fun test_errorFinderReturnsTheRightMessage(){

        assertThat(
            ErrorFinder.getError(SignInDataError.EMAIL_ALREADY_EXISTS)
        ).matches(ErrorMsg.EMAIL_ALREADY_EXISTS_MSG)

        assertThat(
            ErrorFinder.getError(SignInDataError.ERROR_EMAIL_NOT_FOUND)
        ).matches(ErrorMsg.ERROR_EMAIL_NOT_FOUND_MSG)

        assertThat(
            ErrorFinder.getError(SignInDataError.ERROR_EMAIL_NOT_VALID)
        ).matches(ErrorMsg.ERROR_EMAIL_NOT_VALID_MSG)

        assertThat(
            ErrorFinder.getError(SignInDataError.ERROR_EMPTY_EMAIL)
        ).matches(ErrorMsg.ERROR_EMPTY_EMAIL_MSG)

        assertThat(
            ErrorFinder.getError(SignInDataError.ERROR_EMPTY_FNAME)
        ).matches(ErrorMsg.ERROR_EMPTY_FNAME_MSG)

        assertThat(
            ErrorFinder.getError(SignInDataError.ERROR_EMPTY_LNAME)
        ).matches(ErrorMsg.ERROR_EMPTY_LNAME_MSG)

        assertThat(
            ErrorFinder.getError(SignInDataError.ERROR_EMPTY_PASSWORD)
        ).matches(ErrorMsg.ERROR_EMPTY_PASSWORD_MSG)

        assertThat(
            ErrorFinder.getError(SignInDataError.ERROR_EMPTY_PHONE)
        ).matches(ErrorMsg.ERROR_EMPTY_PHONE_MSG)

        assertThat(
            ErrorFinder.getError(SignInDataError.ERROR_EMPTY_USERNAME)
        ).matches(ErrorMsg.ERROR_EMPTY_USERNAME_MSG)

        assertThat(
            ErrorFinder.getError(SignInDataError.ERROR_INVALID_PHONE)
        ).matches(ErrorMsg.ERROR_INVALID_PHONE_MSG)

        assertThat(
            ErrorFinder.getError(SignInDataError.ERROR_PASSWORD_WRONG)
        ).matches(ErrorMsg.ERROR_PASSWORD_WRONG_MSG)

        assertThat(
            ErrorFinder.getError(SignInDataError.PHONE_ALREADY_EXISTS)
        ).matches(ErrorMsg.PHONE_ALREADY_EXISTS_MSG)

    }

}