package com.example.paypointretailer.Utils

sealed class Resource<out R> {

    data class Success<out T>(val data: T): Resource<T>()

    data class Error(val message: String): Resource<Nothing>()

    object Loading: Resource<Nothing>()
}

/*
sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?): Resource<T>(data)
    class Error<T>(message: String, data: T? = null): Resource<T>(data, message)
    class Loading<T>(data: T?): Resource<T>(data)
}*/
