package com.android.presentation.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.base.BaseViewModel
import com.android.base.Resource
import com.android.data.model.MovieDetailModel
import com.android.data.model.remote.BackdropItem
import com.android.data.model.remote.MovieImagesResponse
import com.android.data.model.remote.MovieVideosResponse
import com.android.data.model.remote.VideoItem
import com.android.domain.usecase.MovieDetailUseCase
import com.android.domain.usecase.MovieGalleryUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
class MovieDetailViewModel @ViewModelInject constructor(
    private val movieDetailUseCase: MovieDetailUseCase,
    private val movieGalleryUseCase: MovieGalleryUseCase
) : BaseViewModel() {

    val movieDetailLiveData: MutableLiveData<MovieDetailModel> = MutableLiveData()
    val movieImageListLiveData: MutableLiveData<List<BackdropItem>> = MutableLiveData()
    val movieVideoListLiveData: MutableLiveData<List<VideoItem>> = MutableLiveData()

    fun getMovieDetail(movieId: Int?) {
        movieDetailUseCase.execute(movieId).onEach {
            when (it) {
                is Resource.Loading -> setLoadingStatus(true)
                is Resource.Success -> {
                    setLoadingStatus(false)
                    movieDetailLiveData.value = it.data
                }
                is Resource.Error -> setError(it.apiError)
            }
        }.launchIn(viewModelScope)
    }

    fun getMovieGallery(movieId: Int?) {
        movieGalleryUseCase.execute(movieId).onEach {
            when (it) {
                is Resource.Loading -> setLoadingStatus(true)
                is Resource.Success -> {
                    setLoadingStatus(false)
                    apartMovieGalleryList(it.data)
                }
                is Resource.Error -> setError(it.apiError)
            }
        }.launchIn(viewModelScope)
    }

    private fun apartMovieGalleryList(movieGalleryItem: Any) {
        when (movieGalleryItem) {
            is MovieImagesResponse -> movieImageListLiveData.value = movieGalleryItem.backdrops
            is MovieVideosResponse -> movieVideoListLiveData.value = movieGalleryItem.results
        }
    }
}