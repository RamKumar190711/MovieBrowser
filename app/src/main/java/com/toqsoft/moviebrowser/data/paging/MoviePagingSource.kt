package com.toqsoft.moviebrowser.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.toqsoft.moviebrowser.data.api.MovieApi
import com.toqsoft.moviebrowser.data.model.Movie
import retrofit2.HttpException
import java.io.IOException

class MoviePagingSource(
    private val api: MovieApi
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1
        return try {
            val response = api.getPopularMovies(page)
            val movies = response.results

            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (movies.isEmpty()) null else page + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchor ->
            val anchorPage = state.closestPageToPosition(anchor)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
