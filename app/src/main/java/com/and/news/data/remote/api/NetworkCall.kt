package com.and.news.data.remote.api

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.net.ConnectException
import java.net.SocketTimeoutException

open class NetworkCall<T>(private val call: Call<ResponseBody>) {

    fun newProcess(responseListener: ResponseListenerImpl<T>) {

        GlobalScope.launch(Dispatchers.IO) {
            val callback = object : Callback<ResponseBody> {

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    GlobalScope.launch(Dispatchers.Main) {
                        t.message?.let {
                            responseListener.onError(it)
                        }
                    }
                }

                override fun onResponse(call: Call<ResponseBody>, r: Response<ResponseBody>) =
                    handleResponse(r, responseListener)
            }

            call.enqueue(callback)
        }
    }

    private fun handleResponse(responseBody: Response<ResponseBody>?, responseListener: ResponseListenerImpl<T>) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                if (responseBody?.isSuccessful == true) {
                    if(responseBody.body() != null) {
                        val resBody = responseBody.body()
                        val body = resBody?.string()

                        if (body?.isNotEmpty() == true) {
                            val genericInterfaces = responseListener.javaClass.genericInterfaces
                            val generic: Type? = if (genericInterfaces.isNotEmpty()) {
                                genericInterfaces[0]
                            } else {
                                resBody.javaClass.genericSuperclass
                            }
                            val type = (generic as ParameterizedType)
                                .actualTypeArguments[0]

                            val response = JSONObject(body)
                            NetworkCatchResponse.handleSuccessResponse<T>(response, type = type,
                                onDataSuccess = { data ->
                                    responseListener.onSuccessData(data)
                                })

                        }
                    }
                } else {
                    val body = responseBody?.errorBody()?.string()
                    body?.let {
                        if (it.isNotEmpty()) {
                            val errorResponse = JSONObject(body)
                            val errorMessage = errorResponse.get("errors")
                            if(errorMessage is String)
                                responseListener.onError(errorMessage,responseBody.code())
                            else
                                responseListener.onError("Unknown Response Error",responseBody.code())
                        }
                    }
                }
            } catch (ex: Exception) {
                Log.e("tag","Error Network Call", ex)

                if (ex is SocketTimeoutException || ex is ConnectException) {
                    responseListener.onError("TIMEOUT")
                } else {
                    responseListener.onError("UNKNOWN ERROR")
                }
            }
        }
    }
}