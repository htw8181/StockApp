package com.neverdiesoul.domain.usecase

import com.neverdiesoul.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 서버로부터 마켓코드를 수신받아 RoomDB에 저장한다.
 */
class GetCoinMarketCodeAllFromRemoteUseCase @Inject constructor(private val repository: StockRepository) {
    operator fun invoke(): Flow<Boolean> = repository.getCoinMarketCodeAllFromRemote()
}