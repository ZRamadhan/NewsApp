package com.and.news.data.remote.api

import com.google.gson.GsonBuilder
import org.json.JSONObject
import java.lang.reflect.Type

object NetworkCatchResponse {

    private val GSON = GsonBuilder()
        .create()


    suspend fun<T> handleSuccessResponse(response: JSONObject, type: Type,
                                         onDataSuccess: (T) -> Unit) {
        if(response.has("data")) {
            val messageObject = GSON.fromJson(response.getString("data"), type) as T
            onDataSuccess(messageObject)
        }else {
            onDataSuccess("" as T)
        }
    }



}