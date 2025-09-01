package com.phantom.smartspend.di

import com.phantom.smartspend.data.repository.AuthRepository
import com.phantom.smartspend.data.repository.TransactionRepository
import com.phantom.smartspend.network.ApiService
import com.phantom.smartspend.viewmodels.AuthViewModel
import com.phantom.smartspend.viewmodels.TransactionViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    single {

        //    var authToken = "Bearer 1|kIKGcvv6iDzBKksYnxiPyU4OdLCQIjhdI42SVJz899949e2c"


        //    private val okHttpClient = OkHttpClient.Builder()
        //        .addInterceptor { chain ->
        //            val original = chain.request()
        //            val requestBuilder = original.newBuilder()
        //                .header("Authorization", authToken)
        //                .method(original.method, original.body)
        //            val request = requestBuilder.build()
        //            chain.proceed(request)
        //        }
        //        .build()

        OkHttpClient.Builder()
            .build()
    }

    // Retrofit instance
    single {
        val BASE_URL = "https://1b477fc6b263.ngrok-free.app/api/"
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
//            .client(okHttpClient) ???
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    // API
    single<ApiService> { get<Retrofit>().create(ApiService::class.java) }



    // Repos
    single { AuthRepository(androidContext(), get()) }
    single { TransactionRepository(get()) }


    // ViewModels
    viewModel { AuthViewModel(get()) }
    viewModel { TransactionViewModel(get()) }
}