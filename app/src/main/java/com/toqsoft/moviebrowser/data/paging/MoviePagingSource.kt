package com.toqsoft.moviebrowser.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.toqsoft.moviebrowser.data.api.MovieApi
import com.toqsoft.moviebrowser.data.model.Movie
import retrofit2.HttpException
import java.io.IOException

class MoviePagingSource(
    private val api: MovieApi,
    private val language: String? = null // original language code like "ta", "hi", "en"
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1
        return try {
            val response = if (language.isNullOrEmpty()) {
                api.getPopularMovies(page)
            } else {
                api.discoverMovies(page, originalLanguage = language)
            }

            LoadResult.Page(
                data = response.results,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.results.isEmpty()) null else page + 1
            )

        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }
}
