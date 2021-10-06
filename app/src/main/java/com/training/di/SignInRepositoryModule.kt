package com.training.di

import com.training.repository.SignInRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SignInRepositoryModule {

    @Singleton
    @Provides
    fun provideSignInRepository():SignInRepository{
        return SignInRepository()
    }
}