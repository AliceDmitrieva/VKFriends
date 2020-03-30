package com.alisadmitrieva.vkfriends.utils

import java.util.regex.Pattern

private var accessToken: String? = null

fun saveToken(token: String) {
    accessToken = token
}

fun getToken(): String? {
    return accessToken
}

fun parseRedirectUrl(url: String): Array<String> {
    val access_token = extractPattern(url, "access_token=(.*?)&")
    val user_id = extractPattern(url, "user_id=(\\d*)")
    if (user_id == null || user_id.length == 0 || access_token == null || access_token.length == 0) {
        throw Exception("Failed to parse redirect url $url")
    }
    return arrayOf(access_token, user_id)
}

fun extractPattern(string: String, pattern: String): String? {
    val p = Pattern.compile(pattern)
    val m = p.matcher(string)
    return if (!m.find()) null else m.toMatchResult().group(1)

}
