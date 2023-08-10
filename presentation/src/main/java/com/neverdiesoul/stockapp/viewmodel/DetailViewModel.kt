package com.neverdiesoul.stockapp.viewmodel

import com.neverdiesoul.domain.usecase.RequestRealTimeCoinDataUseCase
import com.neverdiesoul.domain.usecase.TryConnectionToGetRealTimeCoinDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import okio.ByteString
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    tryConnectionToGetRealTimeCoinDataUseCase: TryConnectionToGetRealTimeCoinDataUseCase,
    requestRealTimeCoinDataUseCase: RequestRealTimeCoinDataUseCase
) : BaseRealTimeViewModel(tryConnectionToGetRealTimeCoinDataUseCase,requestRealTimeCoinDataUseCase) {

    override fun sendRealTimeCoinDataToView(bytes: ByteString) {
        TODO("Not yet implemented")
    }

    override fun onCleared() {
        super.onCleared()
    }
}