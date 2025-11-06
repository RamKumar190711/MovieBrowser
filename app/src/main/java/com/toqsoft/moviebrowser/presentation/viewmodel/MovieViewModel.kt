package com.toqsoft.moviebrowser.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.toqsoft.moviebrowser.data.model.Movie
import com.toqsoft.moviebrowser.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    val movies = repository.getPopularMovies().flow.cachedIn(viewModelScope)

    private val _searchResults = MutableStateFlow<List<Movie>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    fun search(query: String) {
        viewModelScope.launch {
            _searchResults.value = repository.searchMovies(query)
        }
    }
}
