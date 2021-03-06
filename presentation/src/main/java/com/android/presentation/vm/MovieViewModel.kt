package com.android.presentation.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.android.domain.pagingsource.MoviesPagingSource
import com.android.domain.repository.MovieRepository
import com.caner.common.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    movieRepository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var movieType: Int = Constants.NOW_PLAYING_MOVIES
    init {
        movieType =
            savedStateHandle.get<Int>(Constants.MOVIE_TYPE) ?: Constants.NOW_PLAYING_MOVIES
    }

    val moviePagingFlow = movieRepository.getMovies(movieType)
        .cachedIn(viewModelScope)
}
