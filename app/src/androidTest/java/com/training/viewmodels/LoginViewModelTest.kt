package com.training.viewmodels

import com.training.model.LoginModel
import com.training.repository.FakeLoginRepository
import com.training.states.SignInState
import com.training.util.constants.AppAdmin
import com.training.util.constants.DataError
import com.training.util.encryption.ItemHasherSHA256
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
            viewmodel.validate_login_suspend(
                LoginModel(
                    AppAdmin.EMAIL.replace('.',','),
                    ItemHasherSHA256.hashItem(AppAdmin.PASSWORD)
                )
            )
        }
        assert(viewmodel.loginState.value?.javaClass == SignInState.Success::class.java)
    }

    @Test fun test_viewModel_invalidUser(){
        repository.invalidUserFlag = true
        runBlockingTest {
            viewmodel.validate_login_suspend(LoginModel("email@", "Pass"))
        }
        assert(viewmodel.loginState.value?.javaClass == SignInState.Error::class.java)
        val err = viewmodel.loginState.value as SignInState.Error
        assert(err.type == DataError.INVALID_USER)
    }

    @Test fun test_viewModel_wrongPassword(){
        repository.wrongPasswordFlag = true
        runBlockingTest {
            viewmodel.validate_login_suspend(LoginModel("email@", "Pass"))
        }
        assert(viewmodel.loginState.value?.javaClass == SignInState.Error::class.java)
        val err = viewmodel.loginState.value as SignInState.Error
        assert(err.type == DataError.ERROR_PASSWORD_WRONG)
    }

    @Test fun test_viewModel_networkError(){
        repository.networkErrorFlag = true
        runBlockingTest {
            viewmodel.validate_login_suspend(LoginModel("email@", "Pass"))
        }
        assert(viewmodel.loginState.value?.javaClass == SignInState.Error::class.java)
        val err = viewmodel.loginState.value as SignInState.Error
        assert(err.type == DataError.NETWORK_ERROR)
    }

    @Test fun test_viewModel_unknownkError(){
        repository.unknowErrorFlag = true
        runBlockingTest {
            viewmodel.validate_login_suspend(LoginModel("email@", "Pass"))
        }
        assert(viewmodel.loginState.value?.javaClass == SignInState.Error::class.java)
        val err = viewmodel.loginState.value as SignInState.Error
        assert(err.type == DataError.UNEXPECTED_ERROR)
    }
}