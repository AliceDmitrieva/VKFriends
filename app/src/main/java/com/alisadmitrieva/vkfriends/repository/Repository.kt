package com.alisadmitrieva.vkfriends.repository

import com.alisadmitrieva.vkfriends.api.APIService
import com.alisadmitrieva.vkfriends.utils.Config
import com.alisadmitrieva.vkfriends.utils.getToken

class Repository(private val apiService: APIService) {
    fun showDataFromAPI(position: Int, loadSize: Int) =
        apiService.getFriends(
            "name",
            "photo_100",
            position,
            loadSize,
            Config.VERSION,
            getToken()
        )

}
