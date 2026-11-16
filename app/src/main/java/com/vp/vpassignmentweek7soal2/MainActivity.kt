package com.vp.vpassignmentweek7soal2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.vp.vpassignmentweek7soal2.repository.MusicRepository
import com.vp.vpassignmentweek7soal2.service.MusicApiService
import com.vp.vpassignmentweek7soal2.view.MusicScreen
import com.vp.vpassignmentweek7soal2.viewmodel.MusicViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.jvm.java

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.theaudiodb.com/api/v1/json/123/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(MusicApiService::class.java)
        val repository = MusicRepository(apiService)
        val viewModel = MusicViewModel(repository)

        viewModel.searchArtistData("Coldplay")

        setContent {
            MusicScreen(viewModel = viewModel)
        }
    }
}