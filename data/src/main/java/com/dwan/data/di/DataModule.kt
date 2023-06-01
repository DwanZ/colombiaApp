package com.dwan.data.di

import com.dwan.data.network.ColombiaApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object AppModule {

    @Provides
    fun provideBaseUrl() = "https://api-colombia.com/"

    @Singleton
    @Provides
    fun provideOkHttpClient() =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit =
        Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL)
            .client(okHttpClient).build()

    @Provides
    @Singleton
    fun provideColombiaApi(retrofit: Retrofit) = retrofit.create(ColombiaApi::class.java)



}