package com.toqsoft.moviebrowser.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.toqsoft.moviebrowser.data.api.MovieApi
import com.toqsoft.moviebrowser.data.dao.MovieDao
import com.toqsoft.moviebrowser.data.model.Movie
import com.toqsoft.moviebrowser.data.paging.MoviePagingSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val api: MovieApi,
    private val dao: MovieDao
) {

    // Get popular movies (or filtered by original language)
    fun getPopularMovies(language: String? = null): Pager<Int, Movie> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { MoviePagingSource(api, language) }
    )

    // Search movies by query and optional language
    suspend fun searchMovies(query: String, language: String? = null): List<Movie> =
        api.searchMovies(query, 1, language).results

    // Discover movies by original language (optional, can be used for non-paging cases)
    suspend fun discoverMovies(language: String? = null, page: Int = 1): List<Movie> =
        api.discoverMovies(page, language).results

    // Get movie details
    suspend fun getMovieDetail(id: Int) = api.getMovieDetail(id)

    // Favorite movies (Room DB)
    suspend fun saveFavorite(movie: Movie) = dao.insert(movie)
    fun getFavorites() = dao.getAllMovies()
}
