@file:Suppress("PackageName")

package com.afsar.sectionrecycler.Network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class Utils {
    companion object {
        private var BASE_URL: String = "https://www.json-generator.com/api/json/get/"
        var retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}