package com.example.gamelens.data.model
import com.example.gamelens.util.Constants
import com.example.gamelens.util.Request
import com.google.gson.Gson


fun main() {
    GameRepository.loadGames(1).forEach { game ->
        println(game)
    }
}

object GameRepository {
    private val gson = Gson()

    fun loadGames(page:Int):  List<Game> {
        val json : String = Request.sendGet("https://api.rawg.io/api/games?key=${Constants.API_KEY}&page=$page")
        val weatherResponse : RAWGResponse = gson.fromJson(json, RAWGResponse::class.java)
        return  weatherResponse.results
    }

    fun loadGame(id:Int): Game {
        val json : String = Request.sendGet("https://api.rawg.io/api/games/$id?key=${Constants.API_KEY}")
        return gson.fromJson(json, Game::class.java)
    }

    fun loadGamesBySearch(search:String): List<Game> {
        val json : String = Request.sendGet("https://api.rawg.io/api/games?key=${Constants.API_KEY}&page=1&search=$search")
        val weatherResponse : RAWGResponse = gson.fromJson(json, RAWGResponse::class.java)
        return  weatherResponse.results
    }
}

data class RAWGResponse(
    val count: Int,
    val next: String,
    val previous: String,
    val results: List<Game>,
)

data class Game(
    val id: Int,
    val name: String,
    val slug: String,
    val released: String,
    val background_image: String,
    val rating: Double,
    val ratings_count: Int,
    val platforms: List<Platform>,
) {

    override fun toString(): String {
        return """
            ================
            Name: $name
            Released: $released
            BG image: $background_image
            Rating: $rating
            Rating count: $ratings_count
        """.trimIndent()
    }
}

data class Platform(
    val platform: PlatformBean,
)

data class PlatformBean(
    val id: Int,
    val name: String,
    val slug: String,
) {
    override fun toString(): String {
        return """
            $name
            $slug
        """.trimIndent()
    }
}

