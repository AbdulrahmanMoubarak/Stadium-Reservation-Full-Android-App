package com.training.viewmodels

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.training.model.UserModel
import com.training.repository.FakeGetDataRepository
import com.training.rules.TestCoroutineRule
import com.training.states.AppDataState
import com.training.util.constants.Encryption
import com.training.util.encryption.ItemEncryptorASE
import com.training.util.encryption.ItemHasherSHA256
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class DataRetrieveViewModelTest{

    private lateinit var viewmodel: DataRetrieveViewModel
    private lateinit var repository: FakeGetDataRepository
    private val user = UserModel(
        "email@gmail.com",
        "pass",
        "Ahmad",
        "Salah",
        "01012151369",
        "customer",
        false
    )

    @get:Rule
    val rule = TestCoroutineRule()

    @Before
    fun setup(){
        repository = FakeGetDataRepository()
        viewmodel = DataRetrieveViewModel(repository)
    }

    @Test fun test_getAllUsers_success(){
        repository.isListEmpty = false
        runBlockingTest {
            viewmodel.getAllUsers_suspend()
            delay(1500)
        }
        assert(viewmodel.retrieveState.value?.javaClass == AppDataState.Success::class.java)
    }

    @Test fun test_getAllUsers_noData(){
        repository.isListEmpty = true
        runBlockingTest {
            viewmodel.getAllUsers_suspend()
            delay(1500)
        }
        assert(viewmodel.retrieveState.value?.javaClass == AppDataState.Error::class.java)
    }

    @Test fun test_getUsersUnlinked_success(){
        repository.isListEmpty = false
        runBlockingTest {
            viewmodel.getUsersUnlinked_suspend()
            delay(1500)
        }
        assert(viewmodel.retrieveState.value?.javaClass == AppDataState.Success::class.java)
    }

    @Test fun test_getUsersUnlinked_noData(){
        repository.isListEmpty = true
        runBlockingTest {
            viewmodel.getUsersUnlinked_suspend()
            delay(1500)
        }
        assert(viewmodel.retrieveState.value?.javaClass == AppDataState.Error::class.java)
    }

    @Test fun test_getStadiums_success(){
        repository.isListEmpty = false
        runBlockingTest {
            viewmodel.getStadiumsList_suspend("all")
            delay(1500)
        }
        assert(viewmodel.stadiums_retrieveState.value?.javaClass == AppDataState.Success::class.java)
    }

    @Test fun test_getStadiums_noData(){
        repository.isListEmpty = true
        runBlockingTest {
            viewmodel.getStadiumsList_suspend("all")
            delay(1500)
        }
        assert(viewmodel.stadiums_retrieveState.value?.javaClass == AppDataState.Error::class.java)
    }

    @Test fun test_getUserByEmail_success(){
        repository.isUserInvalid = false
        runBlockingTest {
            viewmodel.getUserByEmail_suspend("email@")
            delay(1500)
        }
        assert(viewmodel.user_retrieveState.value?.javaClass == AppDataState.Success::class.java)
    }

    @Test fun test_getUserByEmail_invalid(){
        repository.isUserInvalid = true
        runBlockingTest {
            viewmodel.getUserByEmail_suspend("email@")
            delay(1500)
        }
        assert(viewmodel.user_retrieveState.value?.javaClass == AppDataState.Error::class.java)
    }

    @Test fun test_stadiumByKey_success(){
        repository.keyExists = false
        runBlockingTest {
            viewmodel.getStadiumByKey_suspend("key")
            delay(1500)
        }
        assert(viewmodel.stadium_retrieveState.value?.javaClass == AppDataState.Success::class.java)
    }

    @Test fun test_stadiumByKey_invalid(){
        repository.keyExists= true
        runBlockingTest {
            viewmodel.getStadiumByKey_suspend("key")
            delay(1500)
        }
        assert(viewmodel.stadium_retrieveState.value?.javaClass == AppDataState.Error::class.java)
    }

    @Test fun test_stadiumFields_success(){
        repository.isListEmpty = false
        runBlockingTest {
            viewmodel.getStadiumFields_suspend("key")
            delay(1500)
        }
        assert(viewmodel.fields_retrieveState.value?.javaClass == AppDataState.Success::class.java)
    }

    @Test fun test_stadiumFields_fail(){
        repository.isListEmpty = true
        runBlockingTest {
            viewmodel.getStadiumFields_suspend("key")
            delay(1500)
        }
        assert(viewmodel.fields_retrieveState.value?.javaClass == AppDataState.Error::class.java)
    }

    @Test fun test_userReservations_success(){
        repository.isListEmpty = false
        runBlockingTest {
            viewmodel.getUserReservation_suspend(user, "all")
            delay(1500)
        }
        assert(viewmodel.reservations_retrieveState.value?.javaClass == AppDataState.Success::class.java)
    }

    @Test fun test_userReservations_fail(){
        repository.isListEmpty = true
        runBlockingTest {
            viewmodel.getUserReservation_suspend(user, "all")
            delay(1500)
        }
        assert(viewmodel.reservations_retrieveState.value?.javaClass == AppDataState.Error::class.java)
    }

    @Test fun test_dayUserReservations_success(){
        repository.isListEmpty = false
        runBlockingTest {
            viewmodel.getUserDailyReservations_suspend(user, "date")
            delay(1500)
        }
        assert(viewmodel.reservations_retrieveState.value?.javaClass == AppDataState.Success::class.java)
    }

    @Test fun test_dayUserReservations_fail(){
        repository.isListEmpty = true
        runBlockingTest {
            viewmodel.getUserDailyReservations_suspend(user, "date")
            delay(1500)
        }
        assert(viewmodel.reservations_retrieveState.value?.javaClass == AppDataState.Error::class.java)
    }

    @Test fun test_stadiumFieldReservations_success(){
        repository.isListEmpty = false
        runBlockingTest {
            viewmodel.getStadiumFieldReservations_suspend("key", "game")
            delay(1500)
        }
        assert(viewmodel.reservations_retrieveState.value?.javaClass == AppDataState.Success::class.java)
    }

    @Test fun test_stadiumFieldReservations_fail(){
        repository.isListEmpty = true
        runBlockingTest {
            viewmodel.getStadiumFieldReservations_suspend("key", "game")
            delay(1500)
        }
        assert(viewmodel.reservations_retrieveState.value?.javaClass == AppDataState.Error::class.java)
    }
}