package com.training.repository

import com.training.model.LoginModel
import com.training.model.UserModel
import com.training.util.constants.SignInDataError
import com.training.util.states.SignInState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SignInRepository {


    // returns int > 1 if entry data is valid
    suspend fun validateLoginData(user: LoginModel): Flow<SignInState<UserModel>> = flow {
        emit(SignInState.Loading)
        if (!(user.email.length > 0)) {
            emit(SignInState.Error(SignInDataError.ERROR_EMPTY_EMAIL))
        } else if (!user.email.contains('@')) {
            emit(SignInState.Error(SignInDataError.ERROR_EMAIL_NOT_VALID))
        } else if (!(user.password.length > 0)) {
            emit(SignInState.Error(SignInDataError.ERROR_EMPTY_PASSWORD))
        } else {
            emit(validateLogin_Database(user.email, user.password))
        }
    }

    // returns user with privilege if entry data is valid
    private suspend fun validateLogin_Database(
        email: String,
        password: String
    ): SignInState<UserModel> {
        //search by email
        //Under construction
        //dummy user
        delay(1000)
        var user = UserModel(
            "Salah@admin.stadium.com",
            "jqduatsidha8520351",
            "Salah",
            "Sayed",
            "01020568975"
        )
        user.setId(5)
        user.setAccessPrivilege("Admin")
        return SignInState.Success(user)
    }

    // returns int > 1 if entry data is valid
    suspend fun validateRegisterData(user: UserModel): Flow<SignInState<UserModel>> = flow {

        emit(SignInState.Loading)
        if (!(user.email.length > 0)) {
            emit(SignInState.Error(SignInDataError.ERROR_EMPTY_EMAIL))

        } else if (!user.email.contains('@')) {
            emit(SignInState.Error(SignInDataError.ERROR_EMAIL_NOT_VALID))

        } else if (!(user.password.length > 0)) {
            emit(SignInState.Error(SignInDataError.ERROR_EMPTY_PASSWORD))

        } else if (!(user.first_name.length > 0)) {
            emit(SignInState.Error(SignInDataError.ERROR_EMPTY_FNAME))

        } else if (!(user.last_name.length > 0)) {
            emit(SignInState.Error(SignInDataError.ERROR_EMPTY_LNAME))

        } else if (!(user.phone.length > 0)) {
            emit(SignInState.Error(SignInDataError.ERROR_EMPTY_PHONE))

        } else if (!(user.phone.length == 11)) {
            emit(SignInState.Error(SignInDataError.ERROR_INVALID_PHONE))

        } else {
            emit(validateRegister_Database(user))
        }
    }

    // returns user with privilege if entry data is valid
    private suspend fun validateRegister_Database(user_: UserModel): SignInState<UserModel> {
        //make sure user data does'nt exist
        //Under construction
        //dummy user
        delay(1000)
        var user = UserModel(
            "Salah@admin.stadium.com",
            "jqduatsidha8520351",
            "Salah",
            "Sayed",
            "01020568975"
        )
        user.setId(5)
        user.setAccessPrivilege("Admin")
        return SignInState.Success(user)
    }

}