package com.phantom.smartspend.di

import com.phantom.smartspend.data.local.AuthTokenProvider
import com.phantom.smartspend.data.repository.AuthRepository
import com.phantom.smartspend.data.repository.TransactionRepository
import com.phantom.smartspend.data.repository.UserRepository
import com.phantom.smartspend.network.ApiService
import com.phantom.smartspend.network.AuthInterceptor
import com.phantom.smartspend.network.TokenApi
import com.phantom.smartspend.viewmodels.AuthViewModel
import com.phantom.smartspend.viewmodels.TransactionViewModel
import com.phantom.smartspend.viewmodels.UserViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    single { AuthTokenProvider(get()) }

    single { AuthInterceptor(get(), get()) }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<AuthInterceptor>())
            .build()
    }


    // Retrofit instance
    single {
        val BASE_URL = "https://00639cfc2501.ngrok-free.app" + "/api/"
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    single<TokenApi> {
        val BASE_URL = "https://00639cfc2501.ngrok-free.app" + "/api/"
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
            .create(TokenApi::class.java)
    }


    // API
    single<ApiService> { get<Retrofit>().create(ApiService::class.java) }



    // Repos
    single { AuthRepository(androidContext(), get(), get()) }
    single { UserRepository(get()) }
    single { TransactionRepository(get()) }


    // ViewModels
    single { AuthViewModel(get(), get(), get(), get()) }
    single { UserViewModel(get()) }
    single { TransactionViewModel(get()) }
}