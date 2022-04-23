package com.caruso.countries.core.widget

import com.caruso.countries.core.databinding.ProgressLoaderWidgetBinding
import com.caruso.countries.core.extensions.gone
import com.caruso.countries.core.extensions.visible

fun ProgressLoaderWidgetBinding.visible() {
    root.visible()
}

fun ProgressLoaderWidgetBinding.gone() {
    root.gone()
}
