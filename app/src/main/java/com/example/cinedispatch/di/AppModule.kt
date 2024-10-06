package com.example.cinedispatch.di

import android.content.Context
import android.content.SharedPreferences
import com.example.cinedispatch.api.ApiKeyInterceptor
import com.example.cinedispatch.api.Constants.base_url
import com.example.cinedispatch.api.MovieService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideFirebase():FirebaseAuth{
        return FirebaseAuth.getInstance()
    }
    @Provides
    @Singleton
    fun provideFirestore():FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }
    @Provides
    @Singleton
    fun getSharedPreferences(@ApplicationContext context: Context):SharedPreferences{
        return context.getSharedPreferences("appInfo",Context.MODE_PRIVATE)
    }
    @Provides
    @Singleton
    fun provideApiKeyInterceptor():ApiKeyInterceptor{
        return ApiKeyInterceptor()
    }
    @Provides
    @Singleton
    fun provideOkhttp(apiKeyInterceptor: ApiKeyInterceptor):OkHttpClient{
        return OkHttpClient.Builder().addInterceptor(apiKeyInterceptor).readTimeout(60,TimeUnit.SECONDS).build()
    }
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient):Retrofit{
        return Retrofit.Builder().baseUrl(base_url).addConverterFactory(GsonConverterFactory.
        create()).client(okHttpClient).build()
    }
    @Provides
    @Singleton
    fun createApi(retrofit: Retrofit):MovieService{
        return retrofit.create(MovieService::class.java)
    }

}