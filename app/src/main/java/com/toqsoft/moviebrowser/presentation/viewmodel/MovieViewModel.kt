package com.toqsoft.moviebrowser.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.toqsoft.moviebrowser.data.model.Movie
import com.toqsoft.moviebrowser.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _selectedLanguage = MutableStateFlow<String?>(null)
    val selectedLanguage = _selectedLanguage.asStateFlow()

    val movies = _selectedLanguage
        .flatMapLatest { lang -> repository.getPopularMovies(lang).flow }
        .cachedIn(viewModelScope)

    private val _searchResults = MutableStateFlow<List<Movie>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    private val _favorites = MutableStateFlow<Set<Int>>(emptySet())
    val favorites: StateFlow<Set<Int>> = _favorites

    fun selectLanguage(languageCode: String?) {
        _selectedLanguage.value = languageCode
    }

    fun search(query: String, language: String? = null) {
        viewModelScope.launch {
            _searchResults.value = repository.searchMovies(query, language)
        }
    }

    fun toggleFavorite(movie: Movie) {
        _favorites.update { current ->
            if (current.contains(movie.id)) current - movie.id else current + movie.id
        }
    }
}
