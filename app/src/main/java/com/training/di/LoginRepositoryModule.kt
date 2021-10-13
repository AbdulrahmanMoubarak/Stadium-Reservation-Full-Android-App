package com.training.di

import com.training.firebase.FirebaseManager
import com.training.repository.LoginRepository
import com.training.repository.LoginRepositoryInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginRepositoryModule {

    @Singleton
    @Provides
    fun provideLoginRepository(
        firebaseManager: FirebaseManager,
    ): LoginRepositoryInterface {
        return LoginRepository(firebaseManager)
    }
}