package com.caruso.countries.repository.remote.adapter

import com.caruso.countries.repository.BadRequest
import com.caruso.countries.repository.ErrorType
import com.caruso.countries.repository.Forbidden
import com.caruso.countries.repository.GenericError
import com.caruso.countries.repository.NetworkUnavailable
import com.caruso.countries.repository.NotFound
import com.caruso.countries.repository.ResultOf
import com.caruso.countries.repository.Unauthorized
import com.caruso.countries.repository.error
import com.caruso.countries.repository.remote.ErrorDto
import com.caruso.countries.repository.success
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.logging.Level
import java.util.logging.Logger

class CountriesCall<T>(
    private val rawCall: Call<T>,
) : Call<ResultOf<T>> {
    override fun clone(): Call<ResultOf<T>> = CountriesCall(rawCall.clone())

    override fun execute(): Response<ResultOf<T>> =
        // I'm using suspend methods. On retrofit they are implemented wrapping the callback api
        throw NotImplementedError()

    override fun enqueue(callback: Callback<ResultOf<T>>) {
        rawCall.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                callback.onResponse(this@CountriesCall, Response.success(response.toResultOf()))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val logger = Logger.getLogger(CountriesCall::class.java.name)
                logger.log(Level.SEVERE, "onCallFailure", t)
                val result = NetworkUnavailable.error()
                callback.onResponse(this@CountriesCall, Response.success(result))
            }
        })
    }

    override fun isExecuted() = rawCall.isExecuted

    override fun cancel() = rawCall.cancel()

    override fun isCanceled() = rawCall.isCanceled

    override fun request(): Request = rawCall.request()

    override fun timeout(): Timeout = rawCall.timeout()
}

private fun <T> Response<T>.toResultOf(): ResultOf<T> =
    if (isSuccessful) {
        body()!!.success()
    } else {
        when (code()) {
            in 400 until 500 -> this.toClientError().error()
            else -> GenericError.error()
        }
    }

fun <T> Response<T>.toClientError(): ErrorType =
    when (code()) {
        400 -> mapClientError()
        401 -> Unauthorized
        403 -> Forbidden
        404 -> NotFound
        409 -> mapClientError()
        else -> GenericError
    }

fun <T> Response<T>.mapClientError(): ErrorType {
    val errorString = errorBody()?.string()
    if (errorString != null) {
        val errorModel = runCatching { Json.decodeFromString<ErrorDto>(errorString) }.getOrNull()
        if (errorModel != null) {
            return BadRequest(
                message = errorModel.message.orEmpty()
            )
        }
    }
    return GenericError
}
