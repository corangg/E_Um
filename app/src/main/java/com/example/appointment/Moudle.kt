package com.example.appointment

import com.example.appointment.repository.ProfileRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.appointment.data.Utils
import com.example.appointment.repository.ChatFragmentRepository
import com.example.appointment.repository.FriendFragmnetRepository
import com.example.appointment.repository.FriendProfileFagmentRepository

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

    @Singleton
    @Provides
    fun providerFriendFragmentRepository(): FriendFragmnetRepository{
        return FriendFragmnetRepository(Utils())
    }

    @Singleton
    @Provides
    fun providerFriendProfileFagmentRepository(): FriendProfileFagmentRepository {
        return FriendProfileFagmentRepository(Utils())
    }

    @Singleton
    @Provides
    fun providerChatFragmentRepository(): ChatFragmentRepository {
        return ChatFragmentRepository(Utils())
    }


}