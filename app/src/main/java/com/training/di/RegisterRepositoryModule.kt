package com.training.di

import com.training.firebase.FirebaseManager
import com.training.repository.LoginRepository
import com.training.repository.LoginRepositoryInterface
import com.training.repository.RegisterRepository
import com.training.repository.RegisterRepositoryInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RegisterRepositoryModule {

    @Singleton
    @Provides
    fun provideRegisterRepository(
        firebaseManager: FirebaseManager,
    ): RegisterRepositoryInterface {
        return RegisterRepository(firebaseManager)
    }
}