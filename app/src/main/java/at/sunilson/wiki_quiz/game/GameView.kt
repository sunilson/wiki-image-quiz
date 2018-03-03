package at.sunilson.wiki_quiz.game

import at.sunilson.wiki_quiz.game.models.Highscore
import at.sunilson.wiki_quiz.game.models.Question

/**
 * Created by linus on 23.02.2018.
 */

interface GameView {

    fun displayNextQuestion(nextQuestion: Question? = null)
    fun showLoading(show : Boolean)
    fun showAnswer(correct: Boolean, nextQuestion: Question)
    fun updateHighscoreValue(highscore: Int)
    fun gameOver(correct: Boolean, highscore: Highscore)
    fun gameError(e: Throwable)

}