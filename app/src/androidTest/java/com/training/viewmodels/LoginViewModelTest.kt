package com.training.viewmodels

import com.training.model.LoginModel
import com.training.repository.FakeLoginRepository
import com.training.states.SignInState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginViewModelTest{



    private lateinit var viewmodel: LoginViewModel
    private lateinit var repository: FakeLoginRepository

    @Before
    fun setup(){
        repository = FakeLoginRepository()
        viewmodel = LoginViewModel(repository)
    }

    @Test fun test_viewModel_success(){
        runBlockingTest {
            var x = async {  viewmodel.validateLogin(LoginModel("email@", "Pass"))}
            x.await()
            assert(viewmodel.loginState.value?.javaClass == SignInState.Success::class.java)
        }

    }

    @Test fun test_viewModel_invalidUser(){
        repository.networkErrorFlag = true
        runBlockingTest {
            viewmodel.validateLogin(LoginModel("email@", "Pass"))
        }
        val state = viewmodel.loginState
        assert(state.value?.javaClass == SignInState.Loading::class.java)

    }
}