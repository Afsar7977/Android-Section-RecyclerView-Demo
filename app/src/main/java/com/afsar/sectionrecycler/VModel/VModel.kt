@file:Suppress("PackageName")

package com.afsar.sectionrecycler.VModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.afsar.sectionrecycler.Network.ApiResponse
import com.afsar.sectionrecycler.Network.Utils
import com.afsar.sectionrecycler.Network.WebApi
import kotlinx.coroutines.Dispatchers

class VModel : ViewModel() {
    private lateinit var context: Context
    private val service = Utils.retrofit.create(WebApi::class.java)

    fun getDataResponse() = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = fetchData()))
        } catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message ?: "Error Occurred"))
        }
    }

    private suspend fun fetchData() = service.getResponse(
        "2"
    )
}