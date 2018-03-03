package at.sunilson.wiki_quiz.repository.retrofitGsonEntities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by linus on 24.02.2018.
 */

public class WikipediaQuery {

    @SerializedName("pages")
    @Expose
    private Map<String, WikipediaArticle> pages;

    public Map<String, WikipediaArticle> getPages() {
        return pages;
    }

    public void setPages(Map<String, WikipediaArticle> pages) {
        this.pages = pages;
    }

}
