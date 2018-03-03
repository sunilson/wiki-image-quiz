package at.sunilson.wiki_quiz.game

import at.sunilson.wiki_quiz.game.models.Article
import at.sunilson.wiki_quiz.game.models.Highscore
import at.sunilson.wiki_quiz.game.models.Question
import at.sunilson.wiki_quiz.repository.GameRepository
import at.sunilson.wiki_quiz.shared.ANSWER_POSSIBILITIES
import at.sunilson.wiki_quiz.shared.EXTRA_IMAGE_THRESHOLD
import at.sunilson.wiki_quiz.shared.QUESTION_AMOUNT
import java.util.*
import javax.inject.Inject

/**
 * @author Linus Weiss
 */


class GameController @Inject constructor() {

    val currentQuestions: List<Question>
        get() = questions.toList()

    val currentHighscore: Highscore
        get() = calculateHighscore()

    @Inject
    lateinit var gameRepository: GameRepository

    private val possibleSolutions: MutableList<Article> = mutableListOf()
    private val questions: MutableList<Question> = mutableListOf()
    private lateinit var mutableArticles: MutableList<Article>
    private val random = Random()
    private var highscore = 0
    private var currentDuration: Long = 0
    private var rank = -1

    fun setupAndStartGame(articles: List<Article>): List<Question> {

        mutableArticles = articles.toMutableList()

        //Search for all possible solutions
        mutableArticles.forEach {
            if (it.imageURL != null && (it.imageURL.endsWith("jpg", true) || it.imageURL.endsWith("png", true))) possibleSolutions.add(it)
        }

        //Define questions with answers and randomly take solutions that don't have an answer (up until the threshold defined in the constants)
        var extraImageThresholdCounter = 0
        for (i in 1..QUESTION_AMOUNT) {
            if (rand(1, 10) == rand(1, 10) && extraImageThresholdCounter < EXTRA_IMAGE_THRESHOLD && (possibleSolutions.size > QUESTION_AMOUNT - questions.size)) {
                extraImageThresholdCounter++
                questions.add(Question(possibleSolutions[0]))
                mutableArticles.removeAt(mutableArticles.indexOf(possibleSolutions[0]))
                possibleSolutions.removeAt(0)

                addArticlesToQuestion(ANSWER_POSSIBILITIES)
            } else {
                questions.add(Question(possibleSolutions[0]))
                questions.last().options.add(mutableArticles[mutableArticles.indexOf(possibleSolutions[0])])
                mutableArticles.removeAt(mutableArticles.indexOf(possibleSolutions[0]))
                possibleSolutions.removeAt(0)

                addArticlesToQuestion(ANSWER_POSSIBILITIES - 1)
            }
        }

        Collections.shuffle(questions)
        questions.forEach {
            Collections.shuffle(it.options)
        }

        return questions
    }

    /** Check if the question was the last one and check if the given answer was correct
     *
     * @return A pair of booleans where the first one defines if the answer was correct and the second one if the game is over
     */
    fun checkWinState(question: Question, duration: Long, answer: Article? = null): Pair<Boolean, Boolean> {
        var answeredCorrect = false
        var gameOver = false

        currentDuration += duration

        if ((answer != null && answer == question.solution) || (answer == null && !questionContainsSolution(question))) {
            highscore++
            answeredCorrect = true
        }

        if (questions.size == 1) {
            gameOver = true
            //Game is over, store the new highscore and get the rank
            rank = gameRepository.storeHighscore(calculateHighscore())
        } else {
            questions.removeAt(0)
        }

        return Pair(answeredCorrect, gameOver)
    }

    fun setSolutionExtracts(extractsWithID: Map<String, String>) {
        questions.forEach {
            it.solution.extract = extractsWithID[it.solution.pageID]
        }
    }

    /**
     * Calculate the current highscore
     */
    private fun calculateHighscore(): Highscore {
        val nowDate = Date().time
        val duration: Int = (currentDuration / 1000).toInt()
        val points = ((highscore * 1000) - (duration * 30))
        return Highscore(duration, if (points < 0) 0 else points, nowDate, highscore, if (rank >= 0) rank else null)
    }

    private fun questionContainsSolution(question: Question): Boolean {
        question.options.forEach {
            if (it == question.solution) return true
        }
        return false
    }

    private fun addArticlesToQuestion(loopLength: Int) {
        for (j in 1..loopLength) {
            if (possibleSolutions.contains(mutableArticles[0])) {
                temploop@ for (k in 0 until mutableArticles.size) {
                    if (!possibleSolutions.contains(mutableArticles[k]) || possibleSolutions.size > QUESTION_AMOUNT - questions.size) {
                        questions.last().options.add(mutableArticles[k])
                        possibleSolutions.remove(mutableArticles[k])
                        mutableArticles.removeAt(k)
                        break@temploop
                    }
                }
            } else {
                questions.last().options.add(mutableArticles[0])
                mutableArticles.removeAt(0)
            }
        }
    }


    private fun rand(from: Int, to: Int): Int {
        return random.nextInt(to - from) + from
    }
}