package com.caruso.countries.repository

import java.io.File

interface CacheDirProvider {
    val cacheDir: File?
}