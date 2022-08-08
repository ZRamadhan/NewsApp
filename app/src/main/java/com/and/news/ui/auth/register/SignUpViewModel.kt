package com.and.news.ui.auth.register

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.and.news.data.Event
import com.and.news.data.remote.api.ApiConfig
import com.and.news.data.remote.api.ResponseListener
import com.and.news.data.remote.model.AuthResponse
import com.and.news.data.remote.model.Data
import com.and.news.data.remote.model.SignUpRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModel : ViewModel() {

    val dataSuccess: MutableLiveData<String> = MutableLiveData()
    val dataError: MutableLiveData<String> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()

    fun signUpUser(signUpRequest: SignUpRequest, context: Context) {

        if (signUpRequest.username.isEmpty()
            || signUpRequest.email.isEmpty()
            || signUpRequest.password.isEmpty()
        ) {
            dataError.value = "Please Fill All Field"
            return
        }

        isLoading.value = true
        val client = ApiConfig.getUserService(context).registerUser(signUpRequest)

        client.newProcess(object : ResponseListener<Data> {
            override fun onSuccessData(data: Data) {
                super.onSuccessData(data)
                isLoading.value = false
                dataSuccess.value = "Register Success"
            }

            override fun onError(message: String, code: Int) {
                super.onError(message, code)
                isLoading.value = false
                dataSuccess.value = message
            }
        })
    }
}