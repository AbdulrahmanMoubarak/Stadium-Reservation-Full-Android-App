package com.training.di

import com.training.firebase.FirebaseManager
import com.training.repository.EditRepository
import com.training.repository.GetDataRepository
import com.training.repository.GetDataRepositoryInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GetDataRepositoryModule {

    @Singleton
    @Provides
    fun provideGetDataRepository(
        firebaseManager: FirebaseManager,
    ): GetDataRepositoryInterface {
        return GetDataRepository(firebaseManager)
    }
}