package com.vp.vpassignmentweek7soal2.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vp.vpassignmentweek7soal2.model.Album
import com.vp.vpassignmentweek7soal2.model.Artist
import com.vp.vpassignmentweek7soal2.model.Track
import com.vp.vpassignmentweek7soal2.repository.MusicRepository
import kotlinx.coroutines.launch

sealed class UiState<out T> {
    object Idle : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

class MusicViewModel(private val repository: MusicRepository) : ViewModel() {
    var searchQuery by mutableStateOf("Coldplay")
        private set

    var artistState by mutableStateOf<UiState<Artist?>>(UiState.Idle)
        private set

    var albumsState by mutableStateOf<UiState<List<Album>>>(UiState.Idle)
        private set

    var selectedAlbum by mutableStateOf<Album?>(null)
        private set

    var tracksState by mutableStateOf<UiState<List<Track>>>(UiState.Idle)
        private set

    fun updateSearchQuery(query: String) {
        searchQuery = query
    }

    fun searchArtistData(artistName: String) {
        artistState = UiState.Loading
        albumsState = UiState.Loading

        viewModelScope.launch {
            val artistResult = repository.getArtistInfo(artistName)
            artistState = artistResult.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Terjadi kesalahan") }
            )

            val albumsResult = repository.getArtistAlbums(artistName)
            albumsState = albumsResult.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Terjadi kesalahan") }
            )
        }
    }

    fun selectAlbum(album: Album) {
        selectedAlbum = album
        tracksState = UiState.Loading

        viewModelScope.launch {
            val tracksResult = repository.getAlbumTracks(album.idAlbum.orEmpty())
            tracksState = tracksResult.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Gagal memuat track") }
            )
        }
    }

    fun clearSelectedAlbum() {
        selectedAlbum = null
        tracksState = UiState.Idle
    }
}