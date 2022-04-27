package com.edwinderepuesto.jpclient.presentation.viewmodel

import com.edwinderepuesto.jpclient.data.repository.MainRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.*


@ExperimentalCoroutinesApi
class MainViewModelUnitTest {
    @Test
    fun fetchPostsCallsRepository() = runTest {

        // Set up mock repository...
        val mockRepository: MainRepository = mock {
            onBlocking { getPosts() } doReturn emptyList()
        }

        // Double-check it's idle at first:
        verifyNoInteractions(mockRepository)

        // Create test subject and count initial getPosts() call from init block
        val subject = MainViewModel(mockRepository)
        verify(mockRepository, times(1)).getPosts()

        // Call method to test:
        subject.fetchPosts()

        // A new call should have been done:
        verify(mockRepository, times(2)).getPosts()

        // Repository didn't carry out any other operation:
        verifyNoMoreInteractions(mockRepository)
    }
}