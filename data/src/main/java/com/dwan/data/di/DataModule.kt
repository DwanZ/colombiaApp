package com.dwan.data.di

import com.dwan.data.network.ColombiaApi
import com.dwan.data.repository.AttractionRepositoryImpl
import com.dwan.data.repository.CountryRepositoryImpl
import com.dwan.data.repository.PresidentRepositoryImpl
import com.dwan.data.source.AttractionDataSource
import com.dwan.data.source.CountryDataSource
import com.dwan.data.source.PresidentDataSource
import com.dwan.data.source.remote.source.AttractionRemoteDataSource
import com.dwan.data.source.remote.source.CountryRemoteDataSource
import com.dwan.data.source.remote.source.PresidentRemoteDataSource
import com.dwan.domain.repository.AttractionRepository
import com.dwan.domain.repository.CountryRepository
import com.dwan.domain.repository.PresidentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideBaseUrl() = "https://api-colombia.com/api/v1/"

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

    @Provides
    @Singleton
    fun provideCountryDataSource(api: ColombiaApi): CountryDataSource = CountryRemoteDataSource(api)

    @Provides
    @Singleton
    fun providePresidentDataSource(api: ColombiaApi): PresidentDataSource = PresidentRemoteDataSource(api)

    @Provides
    @Singleton
    fun provideAttractionDataSource(api: ColombiaApi): AttractionDataSource = AttractionRemoteDataSource(api)

    @Provides
    @Singleton
    fun provideCountryRepository(dataSource: CountryDataSource): CountryRepository {
        return CountryRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun providePresidentRepository(dataSource: PresidentDataSource): PresidentRepository {
        return PresidentRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun provideAttractionRepository(dataSource: AttractionDataSource): AttractionRepository {
        return AttractionRepositoryImpl(dataSource)
    }
}