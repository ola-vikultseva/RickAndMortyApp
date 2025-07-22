package com.example.rickandmortyapp.di

import android.content.Context
import com.example.rickandmortyapp.data.CharacterRepositoryImpl
import com.example.rickandmortyapp.data.db.RickAndMortyDatabase
import com.example.rickandmortyapp.data.db.dao.CharacterDao
import com.example.rickandmortyapp.data.db.dao.RemoteKeysDao
import com.example.rickandmortyapp.data.remote.api.RickAndMortyApiService
import com.example.rickandmortyapp.domain.CharacterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "https://rickandmortyapi.com/api/"

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideRickAndMortyApiService(
        retrofit: Retrofit
    ): RickAndMortyApiService = retrofit.create(RickAndMortyApiService::class.java)

    @Provides
    @Singleton
    fun provideRickAndMortyDatabase(@ApplicationContext context: Context): RickAndMortyDatabase =
        RickAndMortyDatabase.buildDatabase(context)

    @Provides
    @Singleton
    fun provideCharacterDao(database: RickAndMortyDatabase): CharacterDao =
        database.characterDao()

    @Provides
    @Singleton
    fun provideRemoteKeysDao(database: RickAndMortyDatabase): RemoteKeysDao =
        database.remoteKeysDao()

    @Provides
    @Singleton
    fun provideCharacterRepository(
        apiService: RickAndMortyApiService,
        database: RickAndMortyDatabase
    ): CharacterRepository =
        CharacterRepositoryImpl(apiService, database)
}