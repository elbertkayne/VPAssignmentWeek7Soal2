package com.vp.vpassignmentweek7soal2.model

import com.google.gson.annotations.SerializedName

data class ArtistResponse(
    @SerializedName("artists") val artists: List<Artist>?
)

data class Artist(
    @SerializedName("idArtist") val idArtist: String?,
    @SerializedName("strArtist") val strArtist: String?,
    @SerializedName("strGenre") val strGenre: String?,
    @SerializedName("strBiographyEN") val strBiographyEN: String?,
    @SerializedName("strArtistBanner") val strArtistBanner: String?,
    @SerializedName("strArtistThumb") val strArtistThumb: String?
)

data class AlbumResponse(
    @SerializedName("album") val album: List<Album>?
)

data class Album(
    @SerializedName("idAlbum") val idAlbum: String?,
    @SerializedName("strAlbum") val strAlbum: String?,
    @SerializedName("intYearReleased") val intYearReleased: String?,
    @SerializedName("strGenre") val strGenre: String?,
    @SerializedName("strDescriptionEN") val strDescriptionEN: String?,
    @SerializedName("strAlbumThumb") val strAlbumThumb: String?
)

data class TrackResponse(
    @SerializedName("track") val track: List<Track>?
)

data class Track(
    @SerializedName("idTrack") val idTrack: String?,
    @SerializedName("strTrack") val strTrack: String?,
    @SerializedName("intTrackNumber") val intTrackNumber: String?
)