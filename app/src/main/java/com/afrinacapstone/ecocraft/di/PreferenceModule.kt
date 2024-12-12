package com.afrinacapstone.ecocraft.di

import android.content.Context
import com.afrinacapstone.ecocraft.preferences.UserPreference
import com.afrinacapstone.ecocraft.preferences.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PreferenceModule {

    @Provides
    @Singleton
    fun provideUserPreference(@ApplicationContext context: Context): UserPreference {
        return UserPreference(context.dataStore)
    }

}