package at.sunilson.wiki_quiz.repository

import android.content.Context
import at.sunilson.wiki_quiz.game.models.Article
import at.sunilson.wiki_quiz.game.models.Highscore
import at.sunilson.wiki_quiz.repository.retrofitGsonEntities.WikipediaArticles
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Linus Weiss
 */

@Singleton
class GameRepository @Inject constructor() {

    @Inject
    lateinit var wikipediaService: WikipediaService

    @Inject
    lateinit var context: Context


    /**
     * Gets all highscores, sorted by points
     *
     * @return A immutable list of Highscore objects
     */
    fun getHighscores(): List<Highscore> {
        val prefs = context.getSharedPreferences("highscores", Context.MODE_PRIVATE)
        val highscoreString = prefs.getString("highscores", "")
        var highscores: List<Highscore> = listOf()
        if (!highscoreString.isEmpty()) highscores = Gson().fromJson(highscoreString)
        highscores = highscores.sortedWith(compareBy({ it.points })).asReversed()
        return highscores
    }

    /**
     * Gets an immutable Map of random questions from Wikipedia. Questions consist of 5 possible solutions and one guess text
     */
    fun getRandomWikipediaArticlesForGame(amount: Int): Observable<List<Article>> {
        return wikipediaService.getRandomArticlesForGame(amount).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).map {
            val wikipediaArticles: WikipediaArticles = Gson().fromJson(it.string(), WikipediaArticles::class.java)
            val results: MutableList<Article> = mutableListOf()

            wikipediaArticles.query.pages.forEach {
                results.add(Article(it.value.title, it.value.pageid.toString(), it.value.extract, if (it.value.original != null) it.value.original.source else null))
            }

            results
        }
    }

    /**
     * Gets the extracts of the given wikipedia pageIDs and returns them together with the IDs in a map
     */
    fun getExtractsForArticles(pageids: String): Observable<Map<String, String>> {

        return wikipediaService.getExtractsForArticles(pageids).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).map {
            val wikipediaArticles: WikipediaArticles = Gson().fromJson(it.string(), WikipediaArticles::class.java)
            val results: MutableMap<String, String> = mutableMapOf()

            wikipediaArticles.query.pages.forEach {
                if(it.value.extract != null) {
                    results[it.value.pageid.toString()] = it.value.extract
                }
            }

            results
        }
    }

    /**
     *  Stores a given highscore to the shared preferences and returns the index + 1 where the highscore is
     */
    fun storeHighscore(highscore: Highscore) : Int {
        val prefs = context.getSharedPreferences("highscores", Context.MODE_PRIVATE)
        val highscoreString = prefs.getString("highscores", "")
        var highscores: MutableList<Highscore> = mutableListOf()
        if (!highscoreString.isEmpty()) highscores = Gson().fromJson(highscoreString)
        highscores.add(highscore)
        highscores = highscores.sortedWith(compareBy({ it.points })).toMutableList().asReversed()
        val editor = prefs.edit()
        editor.putString("highscores", Gson().toJson(highscores))
        editor.apply()
        return highscores.indexOf(highscore) + 1
    }

    inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object : TypeToken<T>() {}.type)
}