package com.example.gamelens.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamelens.data.model.Game
import com.example.gamelens.data.model.GameRepository
import com.example.gamelens.data.model.SearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainViewModel : ViewModel() {
    val dataList = MutableStateFlow(emptyList<PictureBean>())
    val runInProgress = MutableStateFlow(false)
    val errorMessage = MutableStateFlow("")
    val previous = MutableStateFlow<String?>(null)
    val next = MutableStateFlow<String?>(null)


    fun loadGames(page:Int){
        runInProgress.value = true
        errorMessage.value = ""
        viewModelScope.launch(Dispatchers.IO){
            try {
                val searchResponse: SearchResponse = GameRepository.loadGames(page)
                previous.value = searchResponse.previous
                next.value = searchResponse.next
                dataList.value = searchResponse.results.map { game ->
                    PictureBean(
                        game.id,
                        game.background_image ?: "https://empowher.org/wp-content/uploads/2021/03/image-placeholder-350x350-1.png",
                        game.name,
                        game.released,
                        game.rating,
                        game.playtime,
                        game.metacritic,
                        game.parent_platforms.map { it.platform.slug }
                    )
                }
            } catch (e: Exception) {
                errorMessage.value = e.message.toString()
            } finally {
                runInProgress.value = false
            }
        }
    }

    fun searchGames(query: String,page: Int) {
        runInProgress.value = true
        errorMessage.value = ""
        dataList.value = emptyList() // Clear previous results
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val searchResponse: SearchResponse = GameRepository.loadGamesBySearch(query,page)
                previous.value = searchResponse.previous
                next.value = searchResponse.next
                dataList.value = searchResponse.results.map { game ->
                    PictureBean(
                        game.id,
                        game.background_image ?: "https://empowher.org/wp-content/uploads/2021/03/image-placeholder-350x350-1.png",
                        game.name,
                        game.released,
                        game.rating,
                        game.playtime,
                        game.metacritic,
                        game.parent_platforms.map { it.platform.name }
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

data class PictureBean(
    val id:Int,
    val url: String,
    val title: String,
    val released: String,
    val rating: Double,
    val playtime: Int,
    val metacritic: Int? = null,
    val platforms: List<String> = emptyList(),
)