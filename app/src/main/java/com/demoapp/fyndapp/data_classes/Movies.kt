package com.demoapp.fyndapp.data_classes

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName


@Keep
data class MoviesList(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<Movie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)

@Keep
@Entity(tableName = "movies_list", primaryKeys = ["id"])
data class Movie(
    @SerializedName("adult")
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String?=null,
    @SerializedName("genre_ids")
    @TypeConverters(GenreConverter::class)
    val genreIds: ArrayList<Int>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("popularity")
    val popularity: Double,
    @SerializedName("poster_path")
    val posterPath: String?=null,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("video")
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int
)