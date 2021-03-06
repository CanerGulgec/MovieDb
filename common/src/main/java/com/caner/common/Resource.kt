package com.caner.common

sealed class Resource<out T> {
    class Success<T>(val data: T) : Resource<T>()
    class Error(val apiError: ApiError) : Resource<Nothing>()
    class Loading(val status: Boolean) : Resource<Nothing>()
    object Empty : Resource<Nothing>()
}
