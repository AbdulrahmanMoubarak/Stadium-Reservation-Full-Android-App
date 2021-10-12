package com.training.di

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.training.firebase.FirebaseInitialScriptRunner
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseScriptRunnerModule {

    @Singleton
    @Provides
    fun provideFirebaseInitialScriptRunner(firebaseDatabase: FirebaseFirestore): FirebaseInitialScriptRunner {
        return FirebaseInitialScriptRunner(firebaseDatabase)
    }

}