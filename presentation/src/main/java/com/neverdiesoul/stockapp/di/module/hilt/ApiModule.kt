package com.neverdiesoul.stockapp.di.module.hilt

import com.neverdiesoul.data.repository.StockRepositoryImpl
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

@InstallIn(ViewModelComponent::class)
@Module
object ApiModuleProvides {

}

@InstallIn(ViewModelComponent::class)
@Module
abstract class ApiModuleBinds {
    @Binds
    @ViewModelScoped
    abstract fun bindStockRepository(impl: StockRepositoryImpl): StockRepository
    @Binds
    @ViewModelScoped
    abstract fun bindStockRemoteDataSource(impl: StockRemoteDataSourceImpl): StockRemoteDataSource
}