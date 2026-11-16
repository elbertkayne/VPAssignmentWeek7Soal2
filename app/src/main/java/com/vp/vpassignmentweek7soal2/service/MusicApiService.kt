package com.vp.vpassignmentweek7soal2.service

import com.vp.vpassignmentweek7soal2.model.AlbumResponse
import com.vp.vpassignmentweek7soal2.model.ArtistResponse
import com.vp.vpassignmentweek7soal2.model.TrackResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicApiService {
    @GET("search.php")
    suspend fun searchArtist(
        @Query("s") artistName: String
    ): Response<ArtistResponse>

    @GET("searchalbum.php")
    suspend fun searchAlbums(
        @Query("s") artistName: String
    ): Response<AlbumResponse>

    @GET("album.php")
    suspend fun getAlbumDetail(
        @Query("m") albumId: String
    ): Response<AlbumResponse>

    @GET("track.php")
    suspend fun getAlbumTracks(
        @Query("m") albumId: String
    ): Response<TrackResponse>
}