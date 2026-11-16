package com.vp.vpassignmentweek7soal2.repository

import com.vp.vpassignmentweek7soal2.model.Album
import com.vp.vpassignmentweek7soal2.model.Artist
import com.vp.vpassignmentweek7soal2.model.Track
import com.vp.vpassignmentweek7soal2.service.MusicApiService

class MusicRepository(private val apiService: MusicApiService) {
    suspend fun getArtistInfo(artistName: String): Result<Artist?> {
        return try {
            val response = apiService.searchArtist(artistName)
            if (response.isSuccessful) {
                val artist = response.body()?.artists?.firstOrNull()
                Result.success(artist)
            } else {
                Result.failure(Exception("Gagal memuat data artis"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getArtistAlbums(artistName: String): Result<List<Album>> {
        return try {
            val response = apiService.searchAlbums(artistName)
            if (response.isSuccessful) {
                val albums = response.body()?.album ?: emptyList()
                Result.success(albums)
            } else {
                Result.failure(Exception("Gagal memuat diskografi album"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAlbumTracks(albumId: String): Result<List<Track>> {
        return try {
            val response = apiService.getAlbumTracks(albumId)
            if (response.isSuccessful) {
                val tracks = response.body()?.track ?: emptyList()
                Result.success(tracks)
            } else {
                Result.failure(Exception("Gagal memuat daftar lagu"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}