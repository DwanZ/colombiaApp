package com.dwan.common

sealed class BaseViewState<out T> where T : Any? {
    object Loading : BaseViewState<Nothing>()

    data class Success<T>(val data: T) : BaseViewState<T>()

    data class Failure(val errorMessage: String) : BaseViewState<Nothing>()
}

infix fun <T> BaseViewState<T>.takeIfSuccess(onSuccess: BaseViewState.Success<T>.() -> Unit): BaseViewState<T> {
    return when (this) {
        is BaseViewState.Success -> {
            onSuccess(this)
            this
        }
        else -> {
            this
        }
    }
}

infix fun <T> BaseViewState<T>.takeIfError(onError: BaseViewState.Failure.() -> Unit): BaseViewState<T> {
    return when (this) {
        is BaseViewState.Failure -> {
            onError(this)
            this
        }
        else -> {
            this
        }
    }
}
