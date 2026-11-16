package com.vp.vpassignmentweek7soal2.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.vp.vpassignmentweek7soal2.model.Album
import com.vp.vpassignmentweek7soal2.model.Artist
import com.vp.vpassignmentweek7soal2.model.Track
import com.vp.vpassignmentweek7soal2.viewmodel.MusicViewModel
import com.vp.vpassignmentweek7soal2.viewmodel.UiState

@Composable
fun MusicScreen(viewModel: MusicViewModel) {
    val selectedAlbum = viewModel.selectedAlbum

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF121214)
    ) {
        if (selectedAlbum == null) {
            MainArtistContent(viewModel = viewModel)
        } else {
            AlbumDetailScreen(
                album = selectedAlbum,
                tracksState = viewModel.tracksState,
                onBack = { viewModel.clearSelectedAlbum() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainArtistContent(viewModel: MusicViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(50.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("🎵", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Music Artist", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.searchQuery,
            onValueChange = { viewModel.updateSearchQuery(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Cari artis (mis: Coldplay)", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
            trailingIcon = {
                if (viewModel.searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.Gray)
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color(0xFF1E1E24),
                unfocusedContainerColor = Color(0xFF1E1E24),
                focusedBorderColor = Color(0xFF00C853),
                unfocusedBorderColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { viewModel.searchArtistData(viewModel.searchQuery) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Cari Artis", color = Color.Black, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                when (val state = viewModel.artistState) {
                    is UiState.Loading -> {
                        Box(modifier = Modifier.fillMaxWidth().height(120.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Color(0xFF00C853))
                        }
                    }
                    is UiState.Success -> {
                        state.data?.let { artist ->
                            ArtistInfoCard(artist = artist)
                        } ?: Text("Artis tidak ditemukan", color = Color.Gray)
                    }
                    is UiState.Error -> {
                        ErrorStateView(errorMessage = state.message) {
                            viewModel.searchArtistData(viewModel.searchQuery)
                        }
                    }
                    else -> {}
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Diskografi Album", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)

                    val albumState = viewModel.albumsState
                    if (albumState is UiState.Success) {
                        Surface(
                            color = Color(0xFF1E2E1E),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "${albumState.data.size} Album",
                                color = Color(0xFF00C853),
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            item {
                when (val state = viewModel.albumsState) {
                    is UiState.Loading -> {
                        Box(modifier = Modifier.fillMaxWidth().height(180.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Color(0xFF00C853))
                        }
                    }
                    is UiState.Success -> {
                        val albums = state.data
                        if (albums.isNotEmpty()) {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 1000.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                userScrollEnabled = false
                            ) {
                                items(albums) { album ->
                                    AlbumItem(album = album) {
                                        viewModel.selectAlbum(album)
                                    }
                                }
                            }
                        } else {
                            Text("Tidak ada album ditemukan", color = Color.Gray)
                        }
                    }
                    is UiState.Error -> {
                        Text(state.message, color = Color.Red, fontSize = 12.sp)
                    }
                    else -> {}
                }
            }
        }
    }
}

@Composable
fun ArtistInfoCard(artist: Artist) {
    Column(modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
            model = artist.strArtistBanner ?: artist.strArtistThumb,
            contentDescription = artist.strArtist,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(12.dp))
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(artist.strArtist ?: "", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("${artist.strGenre ?: "Genre Umum"} • London, England", color = Color.Gray, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(8.dp))
        ExpandableDescription(description = artist.strBiographyEN ?: "Tidak ada biografi tersedia.")
    }
}

@Composable
fun ExpandableDescription(description: String) {
    var isExpanded by remember { mutableStateOf(false) }

    Column {
        Text(
            text = description,
            color = Color.LightGray,
            fontSize = 12.sp,
            maxLines = if (isExpanded) Int.MAX_VALUE else 3
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = if (isExpanded) "Tutup ringkasan" else "Baca selengkapnya",
            color = Color(0xFF00C853),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { isExpanded = !isExpanded }
        )
    }
}

@Composable
fun AlbumItem(album: Album, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = album.strAlbumThumb,
            contentDescription = album.strAlbum,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = album.strAlbum ?: "",
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
        Text(
            text = "${album.intYearReleased ?: ""}  •  ${album.strGenre ?: ""}",
            color = Color.Gray,
            fontSize = 11.sp,
            maxLines = 1
        )
    }
}

@Composable
fun AlbumDetailScreen(album: Album, tracksState: UiState<List<Track>>, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text("Detail Album", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                AsyncImage(
                    model = album.strAlbumThumb,
                    contentDescription = album.strAlbum,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(album.strAlbum ?: "", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Tahun Rilis: ${album.intYearReleased ?: "-"}  |  Genre: ${album.strGenre ?: "-"}", color = Color.Gray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))

                ExpandableDescription(description = album.strDescriptionEN ?: "Deskripsi album tidak tersedia.")

                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = Color(0xFF2C2C35))
                Spacer(modifier = Modifier.height(8.dp))

                Text("🎵 Daftar Lagu (Tracks)", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            when (tracksState) {
                is UiState.Loading -> {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Color(0xFF00C853))
                        }
                    }
                }
                is UiState.Success -> {
                    val tracks = tracksState.data
                    if (tracks.isNotEmpty()) {
                        items(tracks) { track ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${track.intTrackNumber ?: "•"}. ${track.strTrack ?: "Unknown Track"}",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    } else {
                        item {
                            Text("Daftar lagu tidak tersedia", color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                }
                is UiState.Error -> {
                    item {
                        Text(tracksState.message, color = Color.Red, fontSize = 12.sp)
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
fun ErrorStateView(errorMessage: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(25.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("⚠️", fontSize = 28.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text("Terjadi Kendala", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = errorMessage,
            color = Color.Gray,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
            shape = RoundedCornerShape(50.dp)
        ) {
            Text("🔄  Coba Lagi", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}