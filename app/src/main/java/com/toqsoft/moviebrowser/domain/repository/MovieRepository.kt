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

    fun getPopularMovies(): Pager<Int, Movie> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { MoviePagingSource(api) }
    )

    suspend fun searchMovies(query: String): List<Movie> = api.searchMovies(query, 1).results

    suspend fun getMovieDetail(id: Int) = api.getMovieDetail(id)

    suspend fun saveFavorite(movie: Movie) = dao.insert(movie)
    fun getFavorites() = dao.getAllMovies()
}