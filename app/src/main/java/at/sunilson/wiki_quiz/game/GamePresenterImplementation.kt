package at.sunilson.wiki_quiz.game

import at.sunilson.wiki_quiz.game.models.Article
import at.sunilson.wiki_quiz.game.models.Question
import at.sunilson.wiki_quiz.repository.GameRepository
import at.sunilson.wiki_quiz.shared.ANSWER_POSSIBILITIES
import at.sunilson.wiki_quiz.shared.EXTRA_IMAGE_THRESHOLD
import at.sunilson.wiki_quiz.shared.QUESTION_AMOUNT
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 *  @author Linus Weiss
 */


class GamePresenterImplementation @Inject constructor() : GamePresenter {


    @Inject
    lateinit var gameRepository: GameRepository

    @Inject
    lateinit var gameController: GameController

    private lateinit var gameView: GameView
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun answerClicked(question: Question, duration: Long, article: Article?) {

        val gameState: Pair<Boolean, Boolean> = gameController.checkWinState(question, duration, article)

        if (!gameState.second) {
            gameView.showAnswer(gameState.first, gameController.currentQuestions[0])
        } else {
            gameView.gameOver(gameState.first, gameController.currentHighscore)
        }
    }

    override fun cancelGame() {
    }

    override fun startGame() {
        gameView.showLoading(true)
        //Add one to the answer possibilities to increase the image count
        compositeDisposable.add(gameRepository.getRandomWikipediaArticlesForGame((10 * ANSWER_POSSIBILITIES) + EXTRA_IMAGE_THRESHOLD)
                .doOnNext {
                    var imageCount = 0
                    it.forEach {
                        if (it.imageURL != null && (it.imageURL.endsWith("jpg", true) || it.imageURL.endsWith("png", true))) imageCount++
                    }

                    if (imageCount < QUESTION_AMOUNT + EXTRA_IMAGE_THRESHOLD) throw IllegalArgumentException("Not enough images")
                }
                .retryWhen {
                    //If the condition above is not met, an IllegalArgumentException is thrown
                    it.flatMap {
                        //If the exception is an IllegalArgumentException retry the request (with a little delay to not spam the Wikipedia API), otherwise throw the error
                        if (it !is IllegalArgumentException) return@flatMap Observable.error<Throwable>(it)
                        else return@flatMap Observable.timer(200, TimeUnit.MILLISECONDS)
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap {
                    //Extract all page ids that are used for solutions and get the extracts from Wikipedia ( We can't get extracts for all articles in the original request because the limit is 20 per request)
                    val questions = gameController.setupAndStartGame(it)
                    var ids = ""
                    questions.forEach {
                        ids += it.solution.pageID + "|"
                    }
                    return@flatMap gameRepository.getExtractsForArticles(ids)
                     }.doOnNext {
                    gameController.setSolutionExtracts(it)
                }
                .subscribeWith(object : DisposableObserver<Any>() {
                    override fun onComplete() {
                    }

                    override fun onError(e: Throwable) {
                        gameView.gameError(e)
                    }

                    override fun onNext(articles: Any) {
                        //Everything worked, display first question
                        gameView.showLoading(false)
                        gameView.displayNextQuestion(gameController.currentQuestions[0])
                    }
                }))
    }

    override fun onDestroy() {
        //Cleanup
        compositeDisposable.dispose()
    }

    override fun onCreate(gameView: GameView) {
        this.gameView = gameView
    }
}