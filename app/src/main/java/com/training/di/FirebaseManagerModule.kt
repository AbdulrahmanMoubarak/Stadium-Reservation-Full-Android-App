package com.training.di

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.training.firebase.FirebaseInitialScriptRunner
import com.training.firebase.FirebaseManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseManagerModule {

    @Singleton
    @Provides
    fun provideFirebaseManager(firebaseDatabase: FirebaseFirestore, firebaseScriptRunner: FirebaseInitialScriptRunner): FirebaseManager {
        return FirebaseManager(firebaseDatabase, firebaseScriptRunner)
    }
}