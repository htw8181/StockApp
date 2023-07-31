package com.neverdiesoul.stockapp.di.module.hilt

import com.neverdiesoul.data.repository.StockRepositoryImpl
import com.neverdiesoul.data.api.ApiClient
import com.neverdiesoul.data.repository.remote.StockRemoteDataSource
import com.neverdiesoul.data.repository.remote.StockRemoteDataSourceImpl
import com.neverdiesoul.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApiModuleProvides {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiClient.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}

@InstallIn(ViewModelComponent::class)
@Module
abstract class ApiModuleBinds {
    @Binds
    @ViewModelScoped
    abstract fun bindStockRemoteDataSource(impl: StockRemoteDataSourceImpl): StockRemoteDataSource
}