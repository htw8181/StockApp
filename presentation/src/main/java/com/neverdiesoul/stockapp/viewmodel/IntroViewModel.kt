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
    val TAG = this::class.simpleName
    fun getCoinMarketCodeAll() {
        viewModelScope.launch {
            getCoinMarketCodeAllUseCase()
                .onStart { Log.d(TAG,"onStart")  }
                .onCompletion { Log.d(TAG,"onCompletion") }
                .catch { Log.d(TAG,"Error!! $it") }
                .collect {
                    when(it) {
                        true-> {
                            Log.d(TAG,"마켓 코드 DB 저장 완료")
                        }
                        false -> {
                            Log.d(TAG,"마켓 코드 DB 저장 실패")
                        }
                    }

                }

        }
    }
}