package com.android.domain.usecase

import com.android.base.BaseUseCase
import com.android.data.model.MovieDetailModel
import com.android.domain.qualifier.IoDispatcher
import com.android.domain.repository.MovieDetailRepository
import com.caner.common.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class MovieDetailUseCase @Inject constructor(
    private val apiRepository: MovieDetailRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseUseCase<MovieDetailModel, Int?>() {

    override fun buildRequest(params: Int?): Flow<Resource<MovieDetailModel>> {
        return apiRepository.getMovieDetail(params)
            .onStart { emit(Resource.Loading(true)) }
            .onCompletion { emit(Resource.Loading(false)) }
            .flowOn(dispatcher)
    }
}
