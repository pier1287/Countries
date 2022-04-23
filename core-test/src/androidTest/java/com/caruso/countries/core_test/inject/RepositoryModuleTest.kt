package com.caruso.countries.core_test.inject

import com.caruso.countries.repository.inject.RepositoryModule
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [RepositoryModule::class])
class RepositoryModuleTest {
}