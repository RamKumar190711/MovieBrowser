package com.toqsoft.moviebrowser.data.model


import com.google.gson.annotations.SerializedName

data class MovieDetail(
    val id: Int,
    val title: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    val overview: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("vote_average")
    val rating: Double,
    val runtime: Int?,
    val genres: List<Genre>?,
    val tagline: String?,
    @SerializedName("backdrop_path")
    val backdropPath: String?
)

data class Genre(
    val id: Int,
    val name: String
)
