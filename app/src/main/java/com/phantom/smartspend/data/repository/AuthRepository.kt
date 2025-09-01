package com.phantom.smartspend.data.repository


import android.content.Context
import android.credentials.GetCredentialException
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.phantom.smartspend.data.local.AuthPreferences
import com.phantom.smartspend.network.ApiService
import com.phantom.smartspend.network.request_models.SignInRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.util.UUID
import kotlin.math.acos


class AuthRepository(
    private val context: Context,
    private val apiService: ApiService
) {

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    suspend fun signInWithGoogleNative() = withContext(Dispatchers.IO) {

        val rawNonce = UUID.randomUUID().toString()
        val digest = MessageDigest.getInstance("SHA-256").digest(rawNonce.toByteArray())
        val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId("861988830364-96domjn5b230uo2ue1p59lrnvc1ggdjk.apps.googleusercontent.com")
            .setNonce(hashedNonce)
            .setAutoSelectEnabled(false)
            .setFilterByAuthorizedAccounts(false)
            .build()

        val request = androidx.credentials.GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val credentialManager = androidx.credentials.CredentialManager.create(context)

        try {
            val result = credentialManager.getCredential(request = request, context = context)

            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
            val googleIdToken = googleIdTokenCredential.idToken

            val backendResponse = apiService.signIn(SignInRequest(googleIdToken))


            AuthPreferences.saveTokens(
                context = context,
                access = backendResponse.accessToken,
                refresh = backendResponse.refreshToken,
            )

            return@withContext backendResponse


//            SupabaseClient.client.auth.signInWith(IDToken) {
//                idToken = googleIdToken
//                provider = Google
//                nonce = rawNonce
//            }

        } catch (e: GetCredentialException) {
            throw e
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun logout() {
        val accessToken = getAccessToken() ?: return
        try {
            apiService.logout("Bearer $accessToken")
            //TODO: check if !ok, so you can refresh it and logout..
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getAccessToken(): String? {
        return AuthPreferences.getAccessToken(context)
    }

    suspend fun getRefreshToken(): String? {
        return AuthPreferences.getRefreshToken(context)
    }

    suspend fun clearTokens() {
        AuthPreferences.clearTokens(context)
    }

}