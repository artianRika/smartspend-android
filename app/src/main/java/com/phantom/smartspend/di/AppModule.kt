package com.phantom.smartspend.di

import com.phantom.smartspend.data.local.AuthTokenProvider
import com.phantom.smartspend.data.repository.AuthRepository
import com.phantom.smartspend.data.repository.TransactionRepository
import com.phantom.smartspend.data.repository.UserRepository
import com.phantom.smartspend.network.ApiService
import com.phantom.smartspend.network.AuthInterceptor
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

//    single {
//
//            var authToken = "Bearer 1|kIKGcvv6iDzBKksYnxiPyU4OdLCQIjhdI42SVJz899949e2c"
//
//        var accessToken = AuthPreferences.getAccessToken()
//
//            private val okHttpClient = OkHttpClient.Builder()
//                .addInterceptor { chain ->
//                    val original = chain.request()
//                    val requestBuilder = original.newBuilder()
//                        .header("Authorization", authToken)
//                        .method(original.method, original.body)
//                    val request = requestBuilder.build()
//                    chain.proceed(request)
//                }
//                .build()
//
//        OkHttpClient.Builder()
//            .build()
//    }

    single { AuthTokenProvider(get()) }

    single { AuthInterceptor(get()) }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<AuthInterceptor>())
            .build()
    }


    // Retrofit instance
    single {
        val BASE_URL = "https://81ac60cc95d1.ngrok-free.app/" + "api/"
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

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