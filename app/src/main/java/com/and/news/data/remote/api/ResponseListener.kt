package com.and.news.data.remote.api

/**
 * Created by michael on 2019-06-25.
 * Senior Android Developer
 */

interface ResponseListener<T>: ResponseListenerImpl<T> {

    override fun onSuccessData(data: T) = Unit

    override fun onError(message: String, code: Int) = Unit

}

interface ResponseListenerImpl<T> {

    fun onSuccessData(data: T)

    fun onError(message: String, code: Int = 0)
}