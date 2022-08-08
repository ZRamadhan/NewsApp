package com.and.news.ui.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.and.news.data.Event
import com.and.news.data.remote.api.ApiConfig
import com.and.news.data.remote.api.ResponseListener
import com.and.news.data.remote.model.AuthResponse
import com.and.news.data.remote.model.Data
import com.and.news.data.remote.model.SignInRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel : ViewModel() {

    val data: MutableLiveData<Data> = MutableLiveData()
    val dataError: MutableLiveData<Event<String>> = MutableLiveData()

    fun getUser(context: Context, signInRequest: SignInRequest) {
        val client = ApiConfig.getUserService(context).getUser()
        client.newProcess(object : ResponseListener<Data> {
            override fun onSuccessData(newData: Data) {
                super.onSuccessData(newData)
                data.value = newData
            }

            override fun onError(message: String, code: Int) {
                super.onError(message, code)

                if (code == 403) {
                    getToken(context, signInRequest)
                } else {
                    dataError.value = Event(message)
                }
            }
        })
    }

    private fun getToken(context: Context, signInRequest: SignInRequest) {
        val client = ApiConfig.getUserService(context).loginUser(signInRequest)
        client.newProcess(object : ResponseListener<Data> {
            override fun onSuccessData(newData: Data) {
                super.onSuccessData(newData)
                data.value = newData
            }

            override fun onError(message: String, code: Int) {
                super.onError(message, code)
                dataError.value = Event(message)
            }
        })
    }
}