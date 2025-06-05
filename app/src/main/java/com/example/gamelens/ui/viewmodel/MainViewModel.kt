package com.example.gamelens.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamelens.data.model.Game
import com.example.gamelens.data.model.GameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainViewModel : ViewModel() {
    val dataList = MutableStateFlow(emptyList<PictureBean>())
    val runInProgress = MutableStateFlow(false)
    val errorMessage = MutableStateFlow("")

    fun loadGames(page:Int){
        runInProgress.value = true
        errorMessage.value = ""
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

    fun searchGames(query: String) {
        runInProgress.value = true
        errorMessage.value = ""
        dataList.value = emptyList() // Clear previous results
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val games: List<Game> = GameRepository.loadGamesBySearch(query)
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

    init {
        loadGames(1)
    }
}

data class PictureBean(val id:Int, val url: String, val title: String, val longText: String)