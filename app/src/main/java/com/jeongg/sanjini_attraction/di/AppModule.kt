package com.jeongg.sanjini_attraction.di

import android.content.Context
import com.jeongg.sanjini_attraction.data.repository.BluetoothRepositoryImpl
import com.jeongg.sanjini_attraction.domain.repository.BluetoothRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBluetoothController(@ApplicationContext context: Context): BluetoothRepository {
        return BluetoothRepositoryImpl(context)
    }
}