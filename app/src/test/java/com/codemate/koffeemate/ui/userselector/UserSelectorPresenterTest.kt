package com.codemate.koffeemate.ui.userselector

import android.graphics.Bitmap
import com.codemate.koffeemate.common.AwardBadgeCreator
import com.codemate.koffeemate.data.local.CoffeeEventRepository
import com.codemate.koffeemate.data.local.CoffeePreferences
import com.codemate.koffeemate.data.local.UserRepository
import com.codemate.koffeemate.data.models.UserListResponse
import com.codemate.koffeemate.data.network.SlackApi
import com.codemate.koffeemate.testutils.fakeUser
import com.codemate.koffeemate.testutils.getResourceFile
import com.codemate.koffeemate.usecases.LoadUsersUseCase
import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import rx.Observable
import rx.schedulers.Schedulers

class UserSelectorPresenterTest {
    val FAKE_USERS = listOf(
            fakeUser(),
            fakeUser(),
            fakeUser()
    )

    @Mock
    lateinit var mockSlackApi: SlackApi

    @Mock
    lateinit var view: UserSelectorView

    @Mock
    lateinit var mockCoffeePreferences: CoffeePreferences

    @Mock
    lateinit var mockAwardBadgeCreator: AwardBadgeCreator

    @Mock
    lateinit var mockBitmap: Bitmap

    lateinit var presenter: UserSelectorPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        val loadUsersUseCase = LoadUsersUseCase(
                mock<UserRepository>(),
                mock<CoffeeEventRepository>(),
                mockSlackApi,
                Schedulers.immediate(),
                Schedulers.immediate()
        )

        whenever(mockCoffeePreferences.getAccidentChannel())
                .thenReturn("")
        whenever(mockAwardBadgeCreator.createBitmapFileWithAward(any(), any()))
                .thenReturn(getResourceFile("images/empty.png"))

        presenter = UserSelectorPresenter(loadUsersUseCase)
        presenter.attachView(view)
    }

    @Test
    fun loadUsers_OnSuccess_ShowsUsersOnUI() {
        whenever(mockSlackApi.getUsers(any())).thenReturn(
                Observable.just(UserListResponse().apply { members = FAKE_USERS })
        )

        presenter.loadUsers()

        inOrder(view) {
            verify(view).showProgress()
            verify(view).showUsers(FAKE_USERS)
            verify(view).hideProgress()
        }

        verifyNoMoreInteractions(view)
    }

    @Test
    fun loadUsers_OnError_ShowsErrorOnUI() {
        whenever(mockSlackApi.getUsers(any()))
                .thenReturn(Observable.error(Throwable()))

        presenter.loadUsers()

        inOrder(view) {
            verify(view).showProgress()
            verify(view).showError()
        }

        verifyNoMoreInteractions(view)
    }
}