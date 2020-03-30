package com.alisadmitrieva.vkfriends.ui.friends.viewholders

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alisadmitrieva.vkfriends.R
import com.alisadmitrieva.vkfriends.models.FriendInfo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_friends_list_adapter.view.*

class FriendsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(friend: FriendInfo?) {
        if (friend != null) {
            itemView.friendName.text = "${friend.firstName} ${friend.lastName}"
            Picasso.get().load(friend.photo).into(itemView.friendPhoto)
        }
    }

    companion object {
        fun create(parent: ViewGroup): FriendsViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_friends_list_adapter, parent, false)
            return FriendsViewHolder(view)
        }
    }

}
