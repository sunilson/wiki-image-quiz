package at.sunilson.wiki_quiz.game

import at.sunilson.wiki_quiz.game.models.Article
import at.sunilson.wiki_quiz.game.models.Question

/**
 * Created by linus on 23.02.2018.
 */

interface GamePresenter {

    fun answerClicked(question: Question, duration: Long, article: Article? = null)
    fun cancelGame()
    fun startGame()
    fun onCreate(gameView: GameView)
    fun onDestroy()

}