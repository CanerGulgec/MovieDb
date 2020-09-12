package com.android.presentation.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.base.BaseViewModel
import com.android.base.NetworkState
import com.android.base.Resource
import com.android.data.model.MovieDetailModel
import com.android.data.model.remote.BackdropItem
import com.android.data.model.remote.MovieImagesResponse
import com.android.data.model.remote.MovieVideosResponse
import com.android.data.model.remote.VideoItem
import com.android.domain.usecase.MovieDetailUseCase
import com.android.domain.usecase.MovieGalleryUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MovieDetailViewModel @ViewModelInject constructor(
    private val movieDetailUseCase: MovieDetailUseCase,
    private val movieGalleryUseCase: MovieGalleryUseCase
) : BaseViewModel() {

    val movieDetailLiveData: MutableLiveData<MovieDetailModel> = MutableLiveData()
    val movieImageListLiveData: MutableLiveData<List<BackdropItem>> = MutableLiveData()
    val movieVideoListLiveData: MutableLiveData<List<VideoItem>> = MutableLiveData()

    fun getMovieDetail(movieId: Int?) {
        setNetworkStatus(NetworkState.Loading)

        viewModelScope.launch {
            movieDetailUseCase.execute(movieId).collect {
                when (it) {
                    is Resource.Loading -> setNetworkStatus(NetworkState.Loading)
                    is Resource.Success -> {
                        setNetworkStatus(NetworkState.Success)
                        movieDetailLiveData.value = it.data
                    }
                    is Resource.Error -> setError(it.apiError)
                }
            }
        }
    }

    fun getMovieGallery(movieId: Int?) {
        setNetworkStatus(NetworkState.Loading)

        viewModelScope.launch {
            movieGalleryUseCase.execute(movieId).collect {
                when (it) {
                    is Resource.Loading -> setNetworkStatus(NetworkState.Loading)
                    is Resource.Success -> {
                        setNetworkStatus(NetworkState.Success)
                        apartMovieGalleryList(it.data)
                    }
                    is Resource.Error -> setError(it.apiError)
                }
            }
        }
    }

    private fun apartMovieGalleryList(movieGalleryItem: Any) {
        when (movieGalleryItem) {
            is MovieImagesResponse -> movieImageListLiveData.value = movieGalleryItem.backdrops
            is MovieVideosResponse -> movieVideoListLiveData.value = movieGalleryItem.results
        }
    }
}