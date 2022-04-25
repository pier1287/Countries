package com.caruso.countries.domain

import java.io.File

interface CacheDirProvider {
    val cacheDir: File?
}
