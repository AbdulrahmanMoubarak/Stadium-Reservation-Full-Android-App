package com.training.viewmodels

import com.training.model.InventoryModel
import com.training.model.StadiumModel
import com.training.model.UserModel
import com.training.repository.FakeRegisterRepository
import com.training.states.SignInState
import com.training.util.constants.DataError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RegisterViewModelTest{

    val user = UserModel(
        "email@gmail.com",
        "pass",
        "Ahmad",
        "Salah",
        "01012151369",
        "customer",
        false
    )

    private lateinit var viewmodel: RegisterViewModel
    private lateinit var repository: FakeRegisterRepository

    @Before fun setup(){
        repository = FakeRegisterRepository()
        viewmodel = RegisterViewModel(repository)
    }

    @Test fun test_viewmodel_success(){
        runBlockingTest {
            viewmodel.addUser_suspend(user)
        }
        assert(viewmodel.registerState.value?.javaClass == SignInState.OperationSuccess::class.java)
    }

    @Test fun test_viewmodel_errorEmailExists(){
        repository.userExists = true
        runBlockingTest {
            viewmodel.addUser_suspend(user)
        }
        assert(viewmodel.registerState.value?.javaClass == SignInState.Error::class.java)
        val err = viewmodel.registerState.value as SignInState.Error
        assert(err.type == DataError.EMAIL_ALREADY_EXISTS)
    }

    @Test fun test_viewModel_errorStadiumKeyExist(){
        repository.stadiumKeyExists = true
        runBlockingTest {
            viewmodel.addStadiumSuspend(StadiumModel(
                "id",
                "name",
                "location",
                Pair(0.0, 0.0),
                InventoryModel(HashMap()),
            ))
        }
        assert(viewmodel.registerState.value?.javaClass == SignInState.Error::class.java)
        val err = viewmodel.registerState.value as SignInState.Error
        assert(err.type == DataError.KEY_ALREADY_EXISTS)
    }

    @Test fun test_viewModel_addStadiumSuccess(){
        repository.stadiumKeyExists = false
        runBlockingTest {
            viewmodel.addStadiumSuspend(StadiumModel(
                "id",
                "name",
                "location",
                Pair(0.0, 0.0),
                InventoryModel(HashMap()),
            ))
        }
        assert(viewmodel.registerState.value?.javaClass == SignInState.OperationSuccess::class.java)
    }

}