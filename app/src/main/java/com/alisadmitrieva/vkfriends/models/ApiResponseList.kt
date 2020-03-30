package com.alisadmitrieva.vkfriends.models

import com.google.gson.annotations.SerializedName

open class ApiResponseList<T>(
    @SerializedName("count")
    val count: Int? = null,
    @SerializedName("items")
    val items: List<FriendInfo>
)