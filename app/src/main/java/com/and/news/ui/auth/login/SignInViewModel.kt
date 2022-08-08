package com.and.news.ui.auth.login

import android.content.Context
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

class SignInViewModel : ViewModel() {

    val dataSuccess: MutableLiveData<Event<String>> = MutableLiveData()
    val dataError: MutableLiveData<Event<String>> = MutableLiveData()
    val isLoading: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val token: MutableLiveData<String> = MutableLiveData()

    fun signInUser(signInRequest: SignInRequest, context: Context) {

        if (signInRequest.email.isEmpty() || signInRequest.password.isEmpty()) {
            dataError.value = Event("Please Fill All Field")
            return
        }

        isLoading.value = Event(true)
        val client = ApiConfig.getUserService(context).loginUser(signInRequest)

        client.newProcess(object : ResponseListener<Data> {
            override fun onSuccessData(data: Data) {
                super.onSuccessData(data)
                isLoading.value = Event(false)
                dataSuccess.value = Event("Login Success ${data.username}")
            }

            override fun onError(message: String, code: Int) {
                super.onError(message, code)
                isLoading.value = Event(false)
                dataError.value = Event(message)
            }
        })
    }
}