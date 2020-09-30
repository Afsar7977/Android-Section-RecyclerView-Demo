@file:Suppress("PackageName")

package com.afsar.sectionrecycler.Network

import com.afsar.sectionrecycler.Modal.DResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WebApi {

    @GET("cekLyuihNK?")
    suspend fun getResponse(
        @Query("indent") indent: String
    ): Response<List<DResponse>>
}