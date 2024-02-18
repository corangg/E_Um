package com.example.appointment.`object`

import com.example.appointment.repository.ProfileRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.appointment.data.Utils
import com.example.appointment.repository.AlarmRepository
import com.example.appointment.repository.ChatFragmentRepository
import com.example.appointment.repository.ChatRepository
import com.example.appointment.repository.FriendAddRepository
import com.example.appointment.repository.FriendAlarmRepository
import com.example.appointment.repository.FriendFragmnetRepository
import com.example.appointment.repository.FriendProfileFagmentRepository
import com.example.appointment.repository.LoginRepository
import com.example.appointment.repository.PasswordEditRepository
import com.example.appointment.repository.ScheduleFragmentRepository
import com.example.appointment.repository.ScheduleMapRepository
import com.example.appointment.repository.ScheduleSetFragmentRepository
import com.example.appointment.repository.SignUpRepository

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

    @Singleton
    @Provides
    fun providerScheduleSetFragmentRepository(): ScheduleSetFragmentRepository {
        return ScheduleSetFragmentRepository(Utils())
    }

    @Singleton
    @Provides
    fun providerScheduleFragmentRepository(): ScheduleFragmentRepository {
        return ScheduleFragmentRepository(Utils())
    }

    @Singleton
    @Provides
    fun providerPasswordEditRepository(): PasswordEditRepository {
        return PasswordEditRepository(Utils())
    }

    @Singleton
    @Provides
    fun providerFriendAddRepository(): FriendAddRepository {
        return FriendAddRepository()
    }

    @Singleton
    @Provides
    fun providerFriendAlarmRepository(): FriendAlarmRepository {
        return FriendAlarmRepository(Utils())
    }

    @Singleton
    @Provides
    fun providerScheduleMapRepository(): ScheduleMapRepository {
        return ScheduleMapRepository()
    }

    @Singleton
    @Provides
    fun providerChatRepository(): ChatRepository {
        return ChatRepository(Utils())
    }

    @Singleton
    @Provides
    fun providerLoginRepository(): LoginRepository {
        return LoginRepository()
    }

    @Singleton
    @Provides
    fun providerSignUpRepository(): SignUpRepository {
        return SignUpRepository()
    }

    @Singleton
    @Provides
    fun providerAlarmRepository(): AlarmRepository {
        return AlarmRepository(Utils())
    }


}