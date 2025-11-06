package com.toqsoft.moviebrowser.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toqsoft.moviebrowser.data.model.MovieDetail
import com.toqsoft.moviebrowser.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MovieDetailUiState(
    val isLoading: Boolean = false,
    val movie: MovieDetail? = null,
    val error: String? = null
)

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _movieDetail = MutableStateFlow(MovieDetailUiState())
    val movieDetail = _movieDetail.asStateFlow()

    fun getMovieDetail(movieId: Int) {
        viewModelScope.launch {
            _movieDetail.value = MovieDetailUiState(isLoading = true)
            try {
                val result = repository.getMovieDetail(movieId)
                _movieDetail.value = MovieDetailUiState(movie = result)
            } catch (e: Exception) {
                _movieDetail.value = MovieDetailUiState(error = e.message)
            }
        }
    }
}
