package com.example.gamelens.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamelens.data.model.Game
import com.example.gamelens.data.model.GameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val viewModel = MainViewModel()

    val job = launch {
        viewModel.runInProgress.collect { inProgress ->
            if (inProgress) {
                println("Loading...")
            }
        }
    }

    viewModel.loadGames(1)

    viewModel.runInProgress.first { !it }

    job.cancel()

    println("List : ${viewModel.dataList.value}")
    println("ErrorMessage : ${viewModel.errorMessage.value}" )
}

class MainViewModel : ViewModel() {
    val dataList = MutableStateFlow(emptyList<PictureBean>())
    val runInProgress = MutableStateFlow(false)
    val errorMessage = MutableStateFlow("")

    fun loadGames(page:Int){
        runInProgress.value = true
        viewModelScope.launch(Dispatchers.IO){
            try {
                val games: List<Game> = GameRepository.loadGames(page)
                dataList.value = games.map { game ->
                    PictureBean(
                        game.id,
                        game.background_image,
                        game.name,
                        game.slug
                    )
                }
            } catch (e: Exception) {
                errorMessage.value = e.message.toString()
            } finally {
                runInProgress.value = false
            }
        }
    }
}

data class PictureBean(val id:Int, val url: String, val title: String, val longText: String)