package at.sunilson.wiki_quiz.repository

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author Linus Weiss
 */

interface WikipediaService {
    @GET("api.php?format=json&action=query&generator=random&grnnamespace=0&prop=pageimages&pithumbsize=1024")
    fun getRandomArticlesForGame(@Query("grnlimit") amount : Int) : Observable<ResponseBody>

    @GET("api.php?format=json&action=query&prop=extracts&exintro=&explaintext=")
    fun getExtractsForArticles(@Query("pageids") pageids: String) : Observable<ResponseBody>
}