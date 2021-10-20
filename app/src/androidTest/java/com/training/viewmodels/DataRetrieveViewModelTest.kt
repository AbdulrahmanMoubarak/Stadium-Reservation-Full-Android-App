package com.training.viewmodels

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.training.repository.FakeGetDataRepository
import com.training.repository.FakeLoginRepository
import com.training.rules.TestCoroutineRule
import com.training.states.SignInState
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class DataRetrieveViewModelTest{

    private lateinit var viewmodel: DataRetrieveViewModel
    private lateinit var repository: FakeGetDataRepository

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
        }
        assert(viewmodel.retrieveState.value?.javaClass == SignInState.Success::class.java)
    }

    @Test fun test_getAllUsers_noData(){
        repository.isListEmpty = true
        runBlockingTest {
            viewmodel.getAllUsers_suspend()
        }
        assert(viewmodel.retrieveState.value?.javaClass == SignInState.Error::class.java)
    }

    @Test fun test_getUsersUnlinked_success(){
        repository.isListEmpty = false
        runBlockingTest {
            viewmodel.getUsersUnlinked_suspend()
        }
        assert(viewmodel.retrieveState.value?.javaClass == SignInState.Success::class.java)
    }

    @Test fun test_getUsersUnlinked_noData(){
        repository.isListEmpty = true
        runBlockingTest {
            viewmodel.getUsersUnlinked_suspend()
        }
        assert(viewmodel.retrieveState.value?.javaClass == SignInState.Error::class.java)
    }

    @Test fun test_getStadiums_success(){
        repository.isListEmpty = false
        runBlockingTest {
            viewmodel.getStadiumsList_suspend("all")
        }
        assert(viewmodel.retrieveState.value?.javaClass == SignInState.Success::class.java)
    }

    @Test fun test_getStadiums_noData(){
        repository.isListEmpty = true
        runBlockingTest {
            viewmodel.getStadiumsList_suspend("all")
        }
        assert(viewmodel.retrieveState.value?.javaClass == SignInState.Error::class.java)
    }

    @Test fun test_getUserByEmail_success(){
        repository.isUserInvalid = false
        runBlockingTest {
            viewmodel.getUserByEmail_suspend("email@")
        }
        assert(viewmodel.retrieveState.value?.javaClass == SignInState.Success::class.java)
    }

    @Test fun test_getUserByEmail_invalid(){
        repository.isUserInvalid = true
        runBlockingTest {
            viewmodel.getUserByEmail_suspend("email@")
        }
        assert(viewmodel.retrieveState.value?.javaClass == SignInState.Error::class.java)
    }
}