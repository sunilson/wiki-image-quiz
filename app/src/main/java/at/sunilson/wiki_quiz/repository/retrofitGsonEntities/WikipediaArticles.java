package at.sunilson.wiki_quiz.repository.retrofitGsonEntities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Linus Weiss
 */

public class WikipediaArticles {

    @SerializedName("query")
    @Expose
    private WikipediaQuery query;


    public WikipediaQuery getQuery() {
        return query;
    }

    public void setQuery(WikipediaQuery query) {
        this.query = query;
    }


}
