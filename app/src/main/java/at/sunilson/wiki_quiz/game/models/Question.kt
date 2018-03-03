package at.sunilson.wiki_quiz.game.models

/**
 * @author Linus Weiss
 */

class Question(val solution: Article) {
    val options: MutableList<Article> = mutableListOf()
}