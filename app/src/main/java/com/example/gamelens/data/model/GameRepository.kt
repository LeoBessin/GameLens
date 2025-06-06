package com.example.gamelens.data.model
import com.example.gamelens.util.Constants
import com.example.gamelens.util.Request
import com.google.gson.Gson

object GameRepository {
    private val gson = Gson()

    fun loadGames(page:Int):  SearchResponse {
        val json : String = Request.sendGet("${Constants.BASE_URL}?key=${Constants.API_KEY}&page=$page")
        val rawgResponse : RAWGResponse = gson.fromJson(json, RAWGResponse::class.java)
        return SearchResponse(
            next = rawgResponse.next,
            previous = rawgResponse.previous,
            results = rawgResponse.results
        )
    }

    fun loadGame(id:Int): Game {
        val json : String = Request.sendGet("${Constants.BASE_URL}/$id?key=${Constants.API_KEY}")
        return gson.fromJson(json, Game::class.java)
    }

    fun loadGamesBySearch(search:String,page:Int): SearchResponse {
        if (search.isBlank()) {
            return loadGames(1)
        }
        val json : String = Request.sendGet("${Constants.BASE_URL}?key=${Constants.API_KEY}&page=$page&search=$search")
        val rawgResponse : RAWGResponse = gson.fromJson(json, RAWGResponse::class.java)
        return SearchResponse(
            next = rawgResponse.next,
            previous = rawgResponse.previous,
            results = rawgResponse.results
        )
    }
}

data class SearchResponse(
    val next: String?,
    val previous: String?,
    val results: List<Game>,
)

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
    val background_image: String? = null,
    val rating: Double,
    val ratings_count: Int,
    val platforms: List<Platform>,
    val parent_platforms: List<Platform>,
    val playtime: Int,
    val metacritic: Int? = null,
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

