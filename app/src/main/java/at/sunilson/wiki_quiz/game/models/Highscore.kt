package at.sunilson.wiki_quiz.game.models

/**
 * Created by linus on 23.02.2018.
 */

data class Highscore(val time: Int, val points: Int, val date: Long, val correctAnswers: Int, val place: Int? = 0)