package com.training.viewmodels

import com.training.model.*
import com.training.repository.FakeRegisterRepository
import com.training.rules.TestCoroutineRule
import com.training.states.AppDataState
import com.training.util.constants.DataError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RegisterViewModelTest{

    @get: Rule
    val rule = TestCoroutineRule()

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
            delay(1000)
        }
        assert(viewmodel.registerState.value?.javaClass == AppDataState.OperationSuccess::class.java)
    }

    @Test fun test_viewmodel_errorEmailExists(){
        repository.userExists = true
        runBlockingTest {
            viewmodel.addUser_suspend(user)
            delay(1000)
        }
        assert(viewmodel.registerState.value?.javaClass == AppDataState.Error::class.java)
        val err = viewmodel.registerState.value as AppDataState.Error
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
            delay(1000)
        }
        assert(viewmodel.registerState.value?.javaClass == AppDataState.Error::class.java)
        val err = viewmodel.registerState.value as AppDataState.Error
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
            delay(1000)
        }
        assert(viewmodel.registerState.value?.javaClass == AppDataState.OperationSuccess::class.java)
    }


    @Test fun test_viewModel_addFieldSuccess(){
        repository.gameExists = false
        runBlockingTest {
            viewmodel.addStadiumField_suspend("key" ,FieldModel())
            delay(1000)
        }
        assert(viewmodel.registerState.value?.javaClass == AppDataState.OperationSuccess::class.java)
    }

    @Test fun test_viewModel_addFieldFail(){
        repository.gameExists = true
        runBlockingTest {
            viewmodel.addStadiumField_suspend("key" ,FieldModel())
            delay(1000)
        }
        assert(viewmodel.registerState.value?.javaClass == AppDataState.Error::class.java)
        val err = viewmodel.registerState.value as AppDataState.Error
        assert(err.type == DataError.UNEXPECTED_ERROR)
    }

    @Test fun test_viewModel_addReservationSuccess(){
        repository.isError = false
        runBlockingTest {
            viewmodel.addReservation_suspend(ReservationModel())
            delay(1000)
        }
        assert(viewmodel.registerState.value?.javaClass == AppDataState.OperationSuccess::class.java)

    }

    @Test fun test_viewModel_addReservationFail(){
        repository.isError = true
        runBlockingTest {
            viewmodel.addReservation_suspend(ReservationModel())
            delay(1000)
        }
        assert(viewmodel.registerState.value?.javaClass == AppDataState.Error::class.java)
        val err = viewmodel.registerState.value as AppDataState.Error
        assert(err.type == DataError.UNEXPECTED_ERROR)
    }


}