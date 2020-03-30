package com.alisadmitrieva.vkfriends.models

import com.google.gson.annotations.SerializedName

open class ApiResponseObject<T>(
    @SerializedName("response")
    val response: T
)