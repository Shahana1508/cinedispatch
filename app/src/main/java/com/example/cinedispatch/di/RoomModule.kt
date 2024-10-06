package com.example.cinedispatch.di

import android.content.Context
import androidx.room.Room
import com.example.cinedispatch.local.MovieDAO
import com.example.cinedispatch.local.MovieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun createRoom(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(context, MovieDatabase::class.java,"local_db").fallbackToDestructiveMigration().build()
    }
    @Provides
    @Singleton
    fun provideDao(movieDatabase: MovieDatabase): MovieDAO {
        return movieDatabase.getDAO()
    }
}