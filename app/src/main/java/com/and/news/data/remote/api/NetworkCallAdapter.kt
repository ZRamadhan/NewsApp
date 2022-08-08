package com.and.news.data.remote.api

import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class NetworkCallAdapter: CallAdapter<NetworkCall<Any>, NetworkCall<Any>> {

    override fun responseType(): Type = TypeToken.get(ResponseBody::class.java).type

    override fun adapt(call: Call<NetworkCall<Any>>): NetworkCall<Any> =  NetworkCall(call as Call<ResponseBody>)
}