package com.training.repository

import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.training.firebase.FirebaseManager
import com.training.model.LoginModel
import com.training.model.UserModel
import com.training.util.validation.ErrorFinder
import com.training.states.SignInState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class SignInRepository
@Inject
constructor(
    var firebaseManager: FirebaseManager,

){
    private var state: SignInState<UserModel>? = null
    // returns int > 1 if entry data is valid
    fun validateLoginData(user: LoginModel): Flow<SignInState<UserModel>> = flow {
        emit(SignInState.Loading)
        val error = ErrorFinder.getError(user)
        if (error <= -1)
            emit(SignInState.Error(error))
        else {
            validateLoginData(user)

            state?.let { s->
                emit(s)
            }
        }
    }.launchIn()

    // returns user with privilege if entry data is valid
    private suspend fun validateLogin_Database(
        user: LoginModel
    ){
        checkInitialSeeding()
        firebaseManager.validateUser(user).onEach {
            state = it
        }.launchIn(CoroutineScope(Dispatchers.IO))
    }

    // returns int > 1 if entry data is valid
     fun validateRegisterData(user: UserModel): Flow<SignInState<UserModel>> = flow {

        emit(SignInState.Loading)
        val error = ErrorFinder.getError(user)
        if (error > -1)
            emit(SignInState.Error(error))
        else
            emit(validateRegister_Database(user))
    }

    // returns user with privilege if entry data is valid
    private fun validateRegister_Database(user_: UserModel): SignInState<UserModel> {
        //make sure user data does'nt exist
        //Under construction
        //dummy user
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

    private fun checkInitialSeeding(){
        firebaseManager.checkAdminSeeding()
    }
}