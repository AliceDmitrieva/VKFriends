package com.alisadmitrieva.vkfriends.ui.friends

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.alisadmitrieva.vkfriends.State
import com.alisadmitrieva.vkfriends.api.APIService
import com.alisadmitrieva.vkfriends.models.FriendInfo
import io.reactivex.disposables.CompositeDisposable

class FriendsListViewModel : ViewModel() {

    var friendsList: LiveData<PagedList<FriendInfo>>

    private val networkService = APIService.getService()
    private val compositeDisposable = CompositeDisposable()
    private val pageSize = 15
    private val friendsDataSourceFactory: FriendsDataSourceFactory

    init {
        friendsDataSourceFactory =
            FriendsDataSourceFactory(
                compositeDisposable,
                networkService
            )
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize)
            .setEnablePlaceholders(false)
            .build()
        friendsList = LivePagedListBuilder(friendsDataSourceFactory, config).build()
    }


    fun getState(): LiveData<State> = Transformations.switchMap(
        friendsDataSourceFactory.friendsInfoDataSourceLiveData,
        FriendsDataSource::state
    )

    fun retry() {
        friendsDataSourceFactory.friendsInfoDataSourceLiveData.value?.retry()
    }

    fun listIsEmpty(): Boolean {
        return friendsList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}
