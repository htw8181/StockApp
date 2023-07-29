package com.neverdiesoul.stockapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neverdiesoul.data.repository.remote.websocket.WebSocketConstants
import com.neverdiesoul.domain.usecase.GetCoinMarketCodeAllUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(private val getCoinMarketCodeAllUseCase: GetCoinMarketCodeAllUseCase) : ViewModel() {
    val TAG = IntroViewModel::class.simpleName
    fun getCoinMarketCodeAll() {
        viewModelScope.launch {
            getCoinMarketCodeAllUseCase()
                .onStart { Log.d(TAG,"onStart")  }
                .onCompletion { Log.d(TAG,"onCompletion") }
                .catch { Log.d(TAG,"Error!! $it") }
                .collect {
                    //Log.d(TAG,"코인 마켓 코드 ${it.size}개")
                    it.forEach { data -> Log.d(TAG,"코인 마켓 코드 $data") }
                }

        }
    }
}