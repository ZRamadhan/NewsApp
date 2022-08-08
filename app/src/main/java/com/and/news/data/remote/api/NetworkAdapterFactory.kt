package com.and.news.data.remote.api

import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class NetworkAdapterFactory: CallAdapter.Factory() {

    override fun get(returnType: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): CallAdapter<NetworkCall<Any>, NetworkCall<Any>>? {
        returnType?.let {
            try {
                val enclosingType = it as ParameterizedType
                if(enclosingType.rawType != NetworkCall::class.java) {
                    return null
                } else {
                    return NetworkCallAdapter()
                }
            } catch(error: ClassCastException) {
                return null
            }
        }
        return null
    }

    companion object {
        @JvmStatic
        fun create() = NetworkAdapterFactory()
    }
}