package com.neverdiesoul.stockapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neverdiesoul.domain.usecase.GetCoinMarketCodeAllUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(private val getCoinMarketCodeAllUseCase: GetCoinMarketCodeAllUseCase) : ViewModel() {
    private val tag = this::class.simpleName
    fun getCoinMarketCodeAll() {
        viewModelScope.launch {
            getCoinMarketCodeAllUseCase()
                .onStart { Log.d(tag,"onStart")  }
                .onCompletion { Log.d(tag,"onCompletion") }
                .catch { Log.d(tag,"Error!! $it") }
                .collect {
                    when(it) {
                        true-> {
                            Log.d(tag,"마켓 코드 DB 저장 완료")
                        }
                        false -> {
                            Log.d(tag,"마켓 코드 DB 저장 실패")
                        }
                    }
                }
        }
    }
}