package com.toqsoft.moviebrowser.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class MovieResponse(
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int
)

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey val id: Int,
    val title: String,
    val poster_path: String?,
    val overview: String?,
    val vote_average: Double
)
