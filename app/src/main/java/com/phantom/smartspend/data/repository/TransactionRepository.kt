package com.phantom.smartspend.data.repository

import android.content.Context
import android.net.Uri
import com.phantom.smartspend.data.model.Category
import com.phantom.smartspend.network.ApiService
import com.phantom.smartspend.network.model.request.AddTransactionRequest
import com.phantom.smartspend.network.model.request.EditTransactionRequest
import com.phantom.smartspend.network.model.response.GetCategoriesResponse
import com.phantom.smartspend.network.model.response.TransactionResponse
import com.phantom.smartspend.network.model.response.UpdateUserResponse
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

    suspend fun getTransactions(): TransactionResponse = api.getTransactions()

    suspend fun addTransaction(transaction: AddTransactionRequest): UpdateUserResponse =
        api.addTransaction(transaction)

    suspend fun editTransaction(transactionId: Int, editTransaction: EditTransactionRequest): UpdateUserResponse =
        api.editTransaction(transactionId, editTransaction)

    suspend fun deleteTransaction(id: Int): UpdateUserResponse =
        api.deleteTransaction(id)

    suspend fun getCategories(): GetCategoriesResponse = api.getCategories()

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