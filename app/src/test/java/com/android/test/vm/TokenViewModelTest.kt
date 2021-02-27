package com.android.test.vm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.android.data.model.remote.TokenResponse
import com.caner.common.utils.PrefStore
import com.caner.common.utils.SharedPreferencesUtils
import com.android.domain.repository.NewTokenRepository
import com.android.domain.usecase.NewTokenUseCase
import com.android.presentation.vm.ProfileViewModel
import com.android.test.utils.MainCoroutineScopeRule
import com.android.test.utils.getOrAwaitValue
import com.caner.common.Resource
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
class TokenViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()

    private val newTokenRepository: NewTokenRepository = mock()

    private val sharedPref: SharedPreferencesUtils = mock()

    private val prefStore: PrefStore = mock()

    private val useCase by lazy {
        NewTokenUseCase(
            newTokenRepository,
            coroutineScope.dispatcher
        )
    }

    private val viewModel by lazy { ProfileViewModel(useCase, sharedPref, prefStore) }

    private val newSessionObserver: Observer<String> = mock()

    @Test
    fun newTokenFlowEmitsSuccessfullyWithArgumentCaptor() = runBlocking {
        //Given
        val userDetails = TokenResponse(true, "1234567")
        val flow = flow {
            emit(Resource.Loading(true))
            emit(Resource.Success(userDetails))
        }

        //When
        whenever(useCase.execute()).thenReturn(flow)
        val captor = argumentCaptor<String>()
        viewModel.newSessionLiveData.observeForever(newSessionObserver)

        //Then
        viewModel.getNewToken()

        verify(newSessionObserver, times(1)).onChanged(captor.capture())
        assertEquals(userDetails.requestToken, captor.firstValue)
    }

    @Test
    fun newTokenFlowEmitsSuccessfullyWithLiveDataUtil() = runBlocking {
        //Given
        val userDetails = TokenResponse(true, "1234567")
        val flow = flow {
            emit(Resource.Loading(true))
            emit(Resource.Success(userDetails))
        }

        //When
        whenever(useCase.execute()).thenReturn(flow)

        //Then
        viewModel.getNewToken()

        assertEquals(userDetails.requestToken, viewModel.newSessionLiveData.getOrAwaitValue())
    }
}