package com.example.gamelens.ui.theme

import androidx.lifecycle.ViewModel
import com.example.gamelens.data.model.Game
import com.example.gamelens.data.model.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow

fun main(){
    val viewModel = MainViewModel()
    viewModel.loadGames(1)
    println("List : ${viewModel.dataList.value}" )
}

class MainViewModel : ViewModel() {
    val dataList = MutableStateFlow(emptyList<PictureBean>())

    fun loadGames(page:Int){
        val games: List<Game> = GameRepository.loadGames(page)
        dataList.value = games.map { game ->
            PictureBean(
                game.id,
                game.background_image,
                game.name,
                game.slug
            )
        }
    }
}

data class PictureBean(val id:Int, val url: String, val title: String, val longText: String)