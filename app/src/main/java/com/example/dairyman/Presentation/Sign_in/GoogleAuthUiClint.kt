package com.example.dairyman.Presentation.Sign_in

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.example.dairyman.Data.Model.userdataModel.SignInResult
import com.example.dairyman.Data.Model.userdataModel.userDataModels
import com.example.dairyman.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class GoogleAuthUiClint(
    private val context: Context,
    private val oneTapClient: SignInClient) {
    private val auth=Firebase.auth
    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException  )
                throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }
    suspend fun signInWithIntent(intent: Intent):SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try{
            Log.d("vaibhav","clicked")

            val user=auth.signInWithCredential(googleCredentials).await().user
            SignInResult(
                data= user?.run {
                    userDataModels(
                        userId=uid,
                        userName = displayName,
                    )
                },
                errorMessage = null
            )
        }catch (e:Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e
            SignInResult(data = null, errorMessage = e.message)
        }

    }
    suspend fun signOut(){
        try{
            oneTapClient.signOut().await()
            auth.signOut()
        }catch (e:Exception){
            e.printStackTrace()
            if( e is CancellationException) throw  e

    }}
    fun getSignInUser():userDataModels?=auth.currentUser?.run {
        userDataModels(
            userId = uid,
            userName = displayName,
        )
    }
        private fun buildSignInRequest(): BeginSignInRequest {
            return BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                    GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setFilterByAuthorizedAccounts(false)
                        .setServerClientId(context.getString(R.string.web_clint_id))
                        .build()
                )
                .setAutoSelectEnabled(true)
                .build()

        }
    }
