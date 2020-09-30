@file:Suppress("PackageName")

package com.afsar.sectionrecycler.Network

data class ApiResponse<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T): ApiResponse<T> =
            ApiResponse(status = Status.SUCCESS, data = data, message = null)

        fun <T> error(data: T?, message: String): ApiResponse<T> =
            ApiResponse(status = Status.ERROR, data = data, message = message)

        fun <T> loading(data: T?): ApiResponse<T> =
            ApiResponse(status = Status.LOADING, data = data, message = null)
    }
}