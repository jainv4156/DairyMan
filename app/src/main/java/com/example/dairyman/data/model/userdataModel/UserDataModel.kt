package com.example.dairyman.data.model.userdataModel

data class UserDataModel(
    val userId:String?,
    val userName:String?
)

data class SignInResult(
    val data:UserDataModel?,
    val errorMessage:String?
)
