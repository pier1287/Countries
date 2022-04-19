package com.caruso.countries.repository

sealed class ResultOf<out T> {
    data class Success<T>(val value: T) : ResultOf<T>()
    data class Error(val type: ErrorType) : ResultOf<Nothing>()
}

abstract class ErrorType
object GenericError : ErrorType()
object NetworkUnavailable : ErrorType()
object Unauthorized : ErrorType()
object Forbidden : ErrorType()
object NotFound : ErrorType()
data class BadRequest(val message: String) : ErrorType()

fun <T> T.success(): ResultOf<T> = ResultOf.Success(this)

fun ErrorType.error(): ResultOf<Nothing> = ResultOf.Error(this)

inline fun <T, R> ResultOf<T>.map(f: (T) -> R): ResultOf<R> = flatMap { f(it).success() }

inline fun <T, R> ResultOf<T>.flatMap(f: (T) -> ResultOf<R>): ResultOf<R> =
    when (this) {
        is ResultOf.Success -> f(this.value)
        is ResultOf.Error -> this
    }

inline fun <T, R> ResultOf<T>.fold(success: (T) -> R, error: (ErrorType) -> R): R =
    when (this) {
        is ResultOf.Success -> success(this.value)
        is ResultOf.Error -> error(this.type)
    }

inline fun <T> ResultOf<T>.success(f: (T) -> Unit) = fold(f, {})

inline fun <T> ResultOf<T>.error(f: (ErrorType) -> Unit) = fold({}, f)

inline fun <T> ResultOf<T>.getOrElse(fallback: (ResultOf.Error) -> T): T = when (this) {
    is ResultOf.Success -> value
    is ResultOf.Error -> fallback(this)
}

fun <T> ResultOf<T>.getOrNull(): T? = when (this) {
    is ResultOf.Success -> value
    is ResultOf.Error -> null
}

inline fun <T, E : ErrorType> ResultOf<T>.mapError(transform: (ErrorType) -> E) = when (this) {
    is ResultOf.Success -> ResultOf.Success(value)
    is ResultOf.Error -> ResultOf.Error(transform(this.type))
}

inline fun <T> ResultOf<T>.onError(f: (ErrorType) -> Unit): ResultOf<T> = when (this) {
    is ResultOf.Success -> this
    is ResultOf.Error -> {
        f(type)
        this
    }
}

inline fun <T> ResultOf<T>.onSuccess(f: (T) -> Unit): ResultOf<T> = when (this) {
    is ResultOf.Success -> {
        f(value)
        this
    }
    is ResultOf.Error -> this
}

fun <T> T?.toResultOf(errorType: ErrorType = GenericError): ResultOf<T> =
    this?.let { ResultOf.Success(it) } ?: errorType.error()

fun <T> ResultOf<T>.filter(
    errorType: ErrorType = GenericError,
    predicate: (T) -> Boolean
): ResultOf<T> =
    flatMap { s -> if (predicate(s)) s.success() else errorType.error() }

inline fun <T, E : ErrorType> ResultOf<T>.mapBadRequestError(transform: (BadRequest) -> E?) =
    mapError { if (it is BadRequest) transform(it) ?: GenericError else it }
