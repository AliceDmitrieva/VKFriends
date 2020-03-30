package com.alisadmitrieva.vkfriends.ui.friends

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PositionalDataSource
import com.alisadmitrieva.vkfriends.State
import com.alisadmitrieva.vkfriends.api.APIService
import com.alisadmitrieva.vkfriends.models.FriendInfo
import com.alisadmitrieva.vkfriends.repository.Repository
import com.alisadmitrieva.vkfriends.utils.Config
import com.alisadmitrieva.vkfriends.utils.getToken
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

class FriendsDataSource(
    private val APIService: APIService,
    private val compositeDisposable: CompositeDisposable
) : PositionalDataSource<FriendInfo>() {

    var state: MutableLiveData<State> = MutableLiveData()
    private var LOAD_COUNT = 15
    private var retryCompletable: Completable? = null
    private var repository = Repository(APIService)

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<FriendInfo>) {
        updateState(State.LOADING)

        val totalCount = LOAD_COUNT
        val position = computeInitialLoadPosition(params, totalCount)
        val loadSize = computeInitialLoadSize(params, position, totalCount)

        compositeDisposable.add(
            repository.showDataFromAPI(position, loadSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->
                        updateState(State.DONE)
                        callback.onResult(response.response.items, 0)
                    },
                    {
                        updateState(State.ERROR)
                        setRetry(Action { loadInitial(params, callback) })
                    }
                )
        )
    }


    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<FriendInfo>) {
        updateState(State.LOADING)
        compositeDisposable.add(
            repository.showDataFromAPI(params.startPosition, params.loadSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->
                        updateState(State.DONE)
                        callback.onResult(response.response.items)
                    },
                    {
                        updateState(State.ERROR)
                    }
                )
        )
    }

    private fun updateState(state: State) {
        this.state.postValue(state)
    }

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(
                retryCompletable!!
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            )
        }
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }

}
