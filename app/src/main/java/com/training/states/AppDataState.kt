package com.training.states

sealed class AppDataState <out R>{
    data class Success<out T>(val data:T):AppDataState<T>()
    data class Error(val type: Int):AppDataState<Nothing>()
    object Loading:AppDataState<Nothing>()
    object Filling:AppDataState<Nothing>()
    object OperationSuccess:AppDataState<Nothing>()
}