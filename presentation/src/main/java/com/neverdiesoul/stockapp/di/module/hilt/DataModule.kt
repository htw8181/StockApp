package com.neverdiesoul.stockapp.di.module.hilt

import android.content.Context
import androidx.room.Room
import com.neverdiesoul.data.db.StockDataBase
import com.neverdiesoul.data.db.dao.StockDao
import com.neverdiesoul.data.repository.StockRepositoryImpl
import com.neverdiesoul.data.repository.local.StockLocalDataSource
import com.neverdiesoul.data.repository.local.StockLocalDataSourceImpl
import com.neverdiesoul.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataModuleProvides {
    /**
     * 앱 실행중에만 사용하도록 inMemoryDatabase 사용함. 앱 종료시 DB 삭제됨.
     */
    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context): StockDataBase = Room.inMemoryDatabaseBuilder(context,StockDataBase::class.java).build()

    @Singleton
    @Provides
    fun provideDao(stockDataBase: StockDataBase): StockDao = stockDataBase.stockDao()
}

@InstallIn(ViewModelComponent::class)
@Module
abstract class DataModuleBinds {
    @Binds
    @ViewModelScoped
    abstract fun bindStockRepository(impl: StockRepositoryImpl): StockRepository

    @Binds
    @ViewModelScoped
    abstract fun bindStockLocalDataSource(impl: StockLocalDataSourceImpl): StockLocalDataSource
}