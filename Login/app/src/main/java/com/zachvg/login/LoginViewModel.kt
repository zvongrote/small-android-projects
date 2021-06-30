package com.zachvg.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    // Fake login credentials. This is obviously not secure and wouldn't
    // be done like this in a production app. This is only for simplicity
    // since the focus of this app is on UI design.
    private val loginCredentials = mutableMapOf("android" to "abc123")

    private val _loginSuccessful = SingleLiveEvent<Boolean>()
    val loginSuccessful: LiveData<Boolean> = _loginSuccessful

    fun onLoginClick(username: String, password: String) {
        _loginSuccessful.value = password == loginCredentials[username]
    }

}