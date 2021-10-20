package com.training.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.training.exceptions.InvalidUserException
import com.training.exceptions.NoDataException
import com.training.model.StadiumModel
import com.training.model.UserModel
import com.training.repository.GetDataRepositoryInterface
import com.training.states.SignInState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataRetrieveViewModel
@Inject
constructor(var repository: GetDataRepositoryInterface): ViewModel() {

    private val _user_retrieveState: MutableLiveData<SignInState<UserModel>> =
        MutableLiveData<SignInState<UserModel>>()

    val user_retrieveState: MutableLiveData<SignInState<UserModel>>
        get() = _user_retrieveState

    private val _retrieveState: MutableLiveData<SignInState<List<UserModel>>> =
        MutableLiveData<SignInState<List<UserModel>>>()

    val retrieveState: MutableLiveData<SignInState<List<UserModel>>>
        get() = _retrieveState



    private val _stadium_retrieveState: MutableLiveData<SignInState<List<StadiumModel>>> =
        MutableLiveData<SignInState<List<StadiumModel>>>()

    val stadium_retrieveState: MutableLiveData<SignInState<List<StadiumModel>>>
        get() = _stadium_retrieveState

    fun getAllUsers(){
        _retrieveState.postValue(SignInState.Loading)
        viewModelScope.launch {
            getAllUsers_suspend()
        }
    }

    suspend fun getAllUsers_suspend(){
        try{
            val users = repository.getOwners()
            _retrieveState.postValue(SignInState.Success(users))
        }catch (e: NoDataException){
            _retrieveState.postValue(SignInState.Error(e.id))
        }
    }

    fun getUsersUnlinked(){
        _retrieveState.postValue(SignInState.Loading)
        viewModelScope.launch {
            getUsersUnlinked_suspend()
        }
    }

    suspend fun getUsersUnlinked_suspend(){
        try {
            val users = repository.getOwnersUnlinked()
            _retrieveState.postValue(SignInState.Success(users))
        }catch (e: NoDataException){
            _retrieveState.postValue(SignInState.Error(e.id))
        }
    }

    fun getStadiumsList(filter: String){
        _stadium_retrieveState.postValue(SignInState.Loading)
        viewModelScope.launch {
            getStadiumsList_suspend(filter)
        }
    }

    suspend fun getStadiumsList_suspend(filter: String){
        try {
            var stadiumList: List<StadiumModel>
            when (filter.lowercase()) {
                "all" -> {
                    stadiumList=repository.getAllStadiums()
                }

                "linked" -> {
                    stadiumList=repository.getStadiumsLinked()
                }

                "unlinked" -> {
                    stadiumList=repository.getStadiumsUnlinked()
                }

                else ->{
                    stadiumList=repository.getAllStadiums()
                }
            }
            _stadium_retrieveState.postValue(SignInState.Success(stadiumList))
        }catch (e: NoDataException){
            _stadium_retrieveState.postValue(SignInState.Error(e.id))
        }
    }

    fun getUserByEmail(email: String){
        viewModelScope.launch {
            getUserByEmail_suspend(email)
        }
    }

    suspend fun getUserByEmail_suspend(email: String){
        try{
            val user = repository.getUserByEmail(email)
            _user_retrieveState.postValue(SignInState.Success(user.getViewFormat()))
        } catch (e: InvalidUserException) {
            _user_retrieveState.postValue(SignInState.Error(e.id))
            return
        }
    }

}