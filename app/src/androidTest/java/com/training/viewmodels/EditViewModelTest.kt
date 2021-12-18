package com.training.viewmodels

import android.util.Log
import com.training.model.*
import com.training.repository.FakeEditRepository
import com.training.repository.FakeRegisterRepository
import com.training.rules.TestCoroutineRule
import com.training.states.AppDataState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class EditViewModelTest {

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

    val stadium = StadiumModel(
        "id",
        "name",
        "location",
        Pair(0.0, 0.0),
        InventoryModel(HashMap()),
    )


    private lateinit var viewmodel: EditViewModel
    private lateinit var repository: FakeEditRepository


    @Before
    fun setUp() {
        repository = FakeEditRepository()
        viewmodel = EditViewModel(repository)
    }

    @Test
    fun test_updateUser_success(){
        repository.throwError  = false
        runBlockingTest {
            viewmodel.updateUserData_suspend(user, true)
            delay(1000)
            Log.d("test", "tested")

        }
        assert(viewmodel.updateState.value?.javaClass == AppDataState.OperationSuccess::class.java)
    }

    @Test
    fun test_updateUser_fail(){
        repository.throwError  = true
        runBlockingTest {
            viewmodel.updateUserData_suspend(user, true)
            delay(1000)
            Log.d("test", "tested")
        }
        assert(viewmodel.updateState.value?.javaClass == AppDataState.Error::class.java)
    }

    @Test
    fun test_updateStadium_success(){
        repository.throwError  = false
        runBlockingTest {
            viewmodel.updateStadiumData_suspend(stadium)
            delay(1000)
            Log.d("test", "tested")

        }
        assert(viewmodel.updateState.value?.javaClass == AppDataState.OperationSuccess::class.java)
    }

    @Test
    fun test_updateStadium_fail(){
        repository.throwError  = true
        runBlockingTest {
            viewmodel.updateStadiumData_suspend(stadium)
            delay(1000)
            Log.d("test", "tested")

        }
        assert(viewmodel.updateState.value?.javaClass == AppDataState.Error::class.java)
    }

    @Test
    fun test_removeReservation_success(){
        repository.throwError  = false
        runBlockingTest {
            viewmodel.removeReservation_suspend(ReservationModel())
            delay(1000)
            Log.d("test", "tested")

        }
        assert(viewmodel.updateState.value?.javaClass == AppDataState.OperationSuccess::class.java)
    }

    @Test
    fun test_removeReservation_fail(){
        repository.throwError  = true
        runBlockingTest {
            viewmodel.removeReservation_suspend(ReservationModel())
            delay(1000)
            Log.d("test", "tested")

        }
        assert(viewmodel.updateState.value?.javaClass == AppDataState.Error::class.java)
    }

    @Test
    fun test_removeField_success(){
        repository.throwError  = false
        runBlockingTest {
            viewmodel.removeStadiumField_suspend(FieldModel())
            delay(1000)
            Log.d("test", "tested")

        }
        assert(viewmodel.updateState.value?.javaClass == AppDataState.OperationSuccess::class.java)
    }

    @Test
    fun test_removeField_fail(){
        repository.throwError  = true
        runBlockingTest {
            viewmodel.removeStadiumField_suspend(FieldModel())
            delay(1000)
            Log.d("test", "tested")

        }
        assert(viewmodel.updateState.value?.javaClass == AppDataState.Error::class.java)
    }

    @Test
    fun test_updateField_success(){
        repository.throwError  = false
        runBlockingTest {
            viewmodel.updateStadiumField_suspend(FieldModel())
            delay(1000)
            Log.d("test", "tested")

        }
        assert(viewmodel.updateState.value?.javaClass == AppDataState.OperationSuccess::class.java)
    }

    @Test
    fun test_updateField_fail(){
        repository.throwError  = true
        runBlockingTest {
            viewmodel.updateStadiumField_suspend(FieldModel())
            delay(1000)
            Log.d("test", "tested")

        }
        assert(viewmodel.updateState.value?.javaClass == AppDataState.Error::class.java)
    }

    @Test
    fun test_updateReservation_success(){
        repository.throwError  = false
        runBlockingTest {
            viewmodel.updateReservation_suspend(ReservationModel())
            delay(1000)
            Log.d("test", "tested")

        }
        assert(viewmodel.updateState.value?.javaClass == AppDataState.OperationSuccess::class.java)
    }

    @Test
    fun test_updateReservation_fail(){
        repository.throwError  = true
        runBlockingTest {
            viewmodel.updateReservation_suspend(ReservationModel())
            delay(1000)
            Log.d("test", "tested")

        }
        assert(viewmodel.updateState.value?.javaClass == AppDataState.Error::class.java)
    }
}