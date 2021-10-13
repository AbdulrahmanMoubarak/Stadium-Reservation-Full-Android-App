package com.training.states

sealed class SignInState <out R>{
    data class Success<out T>(val data:T):SignInState<T>()
    data class Error(val type: Int):SignInState<Nothing>()
    object Loading:SignInState<Nothing>()
    object Filling:SignInState<Nothing>()
    object OperationSuccess:SignInState<Nothing>()
}