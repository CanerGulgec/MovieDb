package com.android.domain.repository

import com.android.base.Mapper
import com.android.base.Resource
import com.android.base.ext.filterMapperResponse
import com.android.data.model.MovieDetailModel
import com.android.data.model.remote.MovieDetailResponse
import com.android.domain.api.MovieDetailApi
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieDetailRepositoryImp @Inject constructor(
    private val apiService: MovieDetailApi,
    private val movieDetailMapper: Mapper<MovieDetailResponse,MovieDetailModel>,
) : MovieDetailRepository {

    override fun getMovieDetail(movieId: Int?) = flow {
        emit(Resource.Loading)
        val data = apiService.getMovieDetail(movieId)
        emit(data.filterMapperResponse(movieDetailMapper))
    }
}
