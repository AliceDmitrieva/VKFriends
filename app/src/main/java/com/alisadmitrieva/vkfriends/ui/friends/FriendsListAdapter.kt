package com.alisadmitrieva.vkfriends.ui.friends

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.alisadmitrieva.vkfriends.State
import com.alisadmitrieva.vkfriends.models.FriendInfo
import com.alisadmitrieva.vkfriends.ui.friends.viewholders.FriendsViewHolder
import com.alisadmitrieva.vkfriends.ui.friends.viewholders.ListFooterViewHolder

class FriendsListAdapter(private val retry: () -> Unit) :
    PagedListAdapter<FriendInfo, RecyclerView.ViewHolder>(FriendsInfoDiffCallback) {

    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2
    private var state = State.LOADING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DATA_VIEW_TYPE) FriendsViewHolder.create(parent) else ListFooterViewHolder.create(
            retry,
            parent
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE)
            (holder as FriendsViewHolder).bind(getItem(position))
        else (holder as ListFooterViewHolder).bind(state)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) DATA_VIEW_TYPE else FOOTER_VIEW_TYPE
    }

    companion object {
        val FriendsInfoDiffCallback = object : DiffUtil.ItemCallback<FriendInfo>() {
            override fun areItemsTheSame(oldItem: FriendInfo, newItem: FriendInfo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: FriendInfo, newItem: FriendInfo): Boolean {
                return oldItem.id == newItem.id && oldItem.firstName == newItem.firstName && oldItem.lastName == newItem.lastName
            }
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state == State.LOADING || state == State.ERROR)
    }

    fun setState(state: State) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }

}
