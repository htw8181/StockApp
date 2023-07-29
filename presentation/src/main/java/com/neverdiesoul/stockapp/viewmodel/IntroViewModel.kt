package com.neverdiesoul.stockapp.viewmodel

import androidx.lifecycle.ViewModel
import com.neverdiesoul.domain.usecase.GetCoinMarketCodeAllUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(private val getCoinMarketCodeAllUseCase: GetCoinMarketCodeAllUseCase) : ViewModel() {

    fun getCoinMarketCodeAll() {
        getCoinMarketCodeAllUseCase()
    }
}