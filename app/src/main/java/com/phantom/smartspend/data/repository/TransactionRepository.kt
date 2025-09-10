package com.phantom.smartspend.data.repository

import android.content.Context
import android.net.Uri
import com.phantom.smartspend.network.ApiService
import com.phantom.smartspend.network.model.response.UploadImageResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class TransactionRepository(
    private val api: ApiService
) {
//    suspend fun getTransactions() = api.getTransactions()

//    suspend fun addTransaction(transaction: TransactionRequest) =
//        api.addTransaction(transaction)


    suspend fun uploadImage(context: Context, uri: Uri): UploadImageResponse {
        val file = uriToFile(context, uri)
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("image", file.name, requestBody)
        return api.uploadImage(part)
    }

    private suspend fun uriToFile(context: Context, uri: Uri): File = withContext(Dispatchers.IO) {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "upload.jpg")
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        file
    }

}