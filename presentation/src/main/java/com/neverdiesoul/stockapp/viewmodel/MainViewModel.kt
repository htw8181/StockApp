package com.neverdiesoul.stockapp.viewmodel

import androidx.lifecycle.ViewModel
import com.neverdiesoul.domain.usecase.GetRealTimeStockUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val getRealTimeStockUseCase: GetRealTimeStockUseCase) : ViewModel() {
}