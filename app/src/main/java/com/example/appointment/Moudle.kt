package com.example.appointment

import com.example.appointment.repository.ProfileRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.appointment.data.Utils

@Module
@InstallIn(SingletonComponent::class)
object Moudle {
    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Singleton
    @Provides
    fun provideProfileRepository(): ProfileRepository {
        return ProfileRepository(Utils())
    }
}