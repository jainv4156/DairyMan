package com.example.dairyman.Data.Model.userdataModel

data class userDataModels(
    val userId:String?,
    val userName:String?
)

data class SignInResult(
    val data:userDataModels?,
    val errorMessage:String?
)
