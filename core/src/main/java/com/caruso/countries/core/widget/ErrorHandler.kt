package com.caruso.countries.core.widget

import android.content.Context
import android.view.View
import com.caruso.countries.core.R
import com.caruso.countries.repository.ErrorType
import com.caruso.countries.repository.NotFound
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class ErrorHandler @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    fun handleError(view: View, errorType: ErrorType) {
        val errorString = when (errorType) {
            is NotFound -> R.string.not_found_error
            else -> R.string.generic_error
        }.run { context.getString(this) }

        Snackbar.make(view, errorString, Snackbar.LENGTH_LONG).show()
    }
}
