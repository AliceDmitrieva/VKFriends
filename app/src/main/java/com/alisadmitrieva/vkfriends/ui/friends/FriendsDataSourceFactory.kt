package com.alisadmitrieva.vkfriends.ui.friends

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import com.alisadmitrieva.vkfriends.api.APIService
import com.alisadmitrieva.vkfriends.models.FriendInfo
import io.reactivex.disposables.CompositeDisposable

class FriendsDataSourceFactory(
    private val compositeDisposable: CompositeDisposable,
    private val APIService: APIService
) : DataSource.Factory<Int, FriendInfo>() {

    val friendsInfoDataSourceLiveData = MutableLiveData<FriendsDataSource>()

    override fun create(): DataSource<Int, FriendInfo> {
        val friendsInfoDataSource =
            FriendsDataSource(
                APIService,
                compositeDisposable
            )
        friendsInfoDataSourceLiveData.postValue(friendsInfoDataSource)
        return friendsInfoDataSource
    }

}
