package com.example.dairyman.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.dairyman.Data.Model.userdataModel.SignInResult
import com.example.dairyman.Data.Model.userdataModel.SignInState
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
        Log.d("vaibhav","clicked")

    }

    fun resetState(){
        _state.update { SignInState() }
    }
}