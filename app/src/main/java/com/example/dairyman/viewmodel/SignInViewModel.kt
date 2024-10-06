package com.example.dairyman.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.ViewModel
import com.example.dairyman.data.model.userdataModel.SignInResult
import com.example.dairyman.data.model.userdataModel.SignInState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignInViewModel:ViewModel() {
    private val _state= MutableStateFlow( SignInState())
    val state=_state.asStateFlow()

    fun onSignInResult(result: SignInResult){
        _state.value=state.value.copy(
            isSignInSuccessful = result.data!=null,
            signInError = result.errorMessage
        )

    }

    fun resetState(){
        _state.update { SignInState() }
    }
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network= connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)

        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }


}